package cn.ibdsr.web.modular.material.controller;

import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.core.check.StaticCheck;
import cn.ibdsr.web.common.constant.factory.PageFactory;
import cn.ibdsr.web.common.persistence.model.EventImage;
import cn.ibdsr.web.common.persistence.model.Events;
import cn.ibdsr.web.modular.material.service.IEventsService;
import cn.ibdsr.web.modular.material.transfer.EventsDTO;
import cn.ibdsr.web.modular.material.warpper.EventsWarpper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 大事记管理控制器
 * @Version V1.0
 * @CreateDate 2019/8/19 16:38
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                大事记管理控制器
 */
@Controller
@RequestMapping("/events")
public class EventsController extends BaseController {

    private String PREFIX = "/material/events/";

    @Resource
    private IEventsService eventsService;

    /**
     * 跳转到大事记管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "events.html";
    }

    /**
     * 获取大事记列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(String condition) {
        Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
        List<Map<String, Object>> records = eventsService.list(page, condition);
        page.setRecords((List<Map<String, Object>>) new EventsWarpper(records).warp());
        return super.packForBT(page);
    }

    /**
     * 跳转到添加大事记
     */
    @RequestMapping("/events_add")
    public String eventsAdd() {
        return PREFIX + "events_add.html";
    }

    /**
     * 新增大事记
     */
    @PostMapping(value = "/add")
    @ResponseBody
    public Object add(@Valid EventsDTO param) {
        StaticCheck.check(param);
        eventsService.addEvents(param);
        return super.SUCCESS_TIP;
    }

    /**
     * 跳转到修改大事记
     */
    @RequestMapping("/events_update/{eventId}")
    public String eventsUpdate(@PathVariable Long eventId, Model model) {
        model.addAttribute("eventId", eventId);
        return PREFIX + "events_edit.html";
    }

    /**
     * 修改大事记
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public Object update(@Valid EventsDTO param) {
        StaticCheck.check(param);
        eventsService.updateEvents(param);
        return super.SUCCESS_TIP;
    }

    /**
     * 跳转到修改大事记
     */
    @RequestMapping("/events_detail/{eventId}")
    public String eventsDetail(@PathVariable Long eventId, Model model) {
        model.addAttribute("eventId", eventId);
        return PREFIX + "events_detail.html";
    }

    /**
     * 大事记详情
     */
    @PostMapping(value = "/detail")
    @ResponseBody
    public Object detail(@RequestParam("eventId") Long eventId) {
        Events event = eventsService.checkId(eventId);
        List<EventImage> imageList = eventsService.getImageListByEventId(eventId);
        Map<String, Object> data = new HashMap<>();
        data.put("event", event);
        data.put("imageList", imageList);
        return new SuccessDataTip(data);
    }

    /**
     * 删除大事记
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam("eventId") Long eventId) {
        eventsService.logicDelete(eventId);
        return SUCCESS_TIP;
    }
}
