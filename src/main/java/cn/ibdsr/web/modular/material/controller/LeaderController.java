package cn.ibdsr.web.modular.material.controller;

import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.core.check.StaticCheck;
import cn.ibdsr.web.common.constant.factory.PageFactory;
import cn.ibdsr.web.common.constant.state.PublishStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.LeaderImage;
import cn.ibdsr.web.common.persistence.model.LeaderWords;
import cn.ibdsr.web.modular.material.service.ILeaderService;
import cn.ibdsr.web.modular.material.transfer.LeaderWordsDTO;
import cn.ibdsr.web.modular.material.warpper.LeaderWarpper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 领导关怀管理控制器
 * @Version V1.0
 * @CreateDate 2019/8/19 16:37
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                领导关怀管理控制器
 */
@Controller
@RequestMapping("/leader")
public class LeaderController extends BaseController {

    private String PREFIX = "/material/leader/";

    @Resource
    private ILeaderService leaderService;

    /**
     * 跳转到领导关怀管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "leader.html";
    }

    /**
     * 获取领导关怀管理列表
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "condition", required = false) String condition,
                       @RequestParam(value = "isPublish", required = false) Integer isPublish) {
        if (isPublish != null && StringUtils.isEmpty(PublishStatus.valueOf(isPublish))) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
        List<Map<String, Object>> records = leaderService.list(page, condition, isPublish);
        page.setRecords((List<Map<String, Object>>) new LeaderWarpper(records).warp());
        return super.packForBT(page);
    }

    /**
     * 跳转到添加领导关怀管理
     */
    @RequestMapping("/leader_add")
    public String leaderAdd() {
        return PREFIX + "leader_add.html";
    }

    /**
     * 新增领导关怀管理
     */
    @PostMapping(value = "/add")
    @ResponseBody
    public Object add(@Valid LeaderWordsDTO param) {
        StaticCheck.check(param);
        LeaderWords leaderWords = leaderService.addLeaderWords(param);
        return new SuccessDataTip(leaderWords.getId());
    }

    /**
     * 跳转到修改领导关怀管理
     */
    @RequestMapping("/leader_update/{leaderId}")
    public String leaderUpdate(@PathVariable Long leaderId, Model model) {
        model.addAttribute("leaderId", leaderId);
        return PREFIX + "leader_edit.html";
    }

    /**
     * 修改领导关怀管理
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public Object update(@Valid LeaderWordsDTO param) {
        StaticCheck.check(param);
        leaderService.updateLeaderWords(param);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除领导关怀管理
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam("leaderId") Long leaderId) {
        leaderService.logicDelete(leaderId);
        return SUCCESS_TIP;
    }

    /**
     * 发布领导关怀
     */
    @PostMapping(value = "/publish")
    @ResponseBody
    public Object publish(@RequestParam(value = "leaderId") Long leaderId) {
        leaderService.editPublish(leaderId, PublishStatus.PUBLISHED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 取消发布领导关怀
     */
    @PostMapping(value = "/cancelPublish")
    @ResponseBody
    public Object cancelPublish(@RequestParam(value = "leaderId") Long leaderId) {
        leaderService.editPublish(leaderId, PublishStatus.UNPUBLISHED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 跳转到修改领导关怀管理
     */
    @RequestMapping("/leader_detail/{leaderId}")
    public String leaderDetail(@PathVariable Long leaderId, Model model) {
        model.addAttribute("leaderId", leaderId);
        return PREFIX + "leader_detail.html";
    }

    /**
     * 领导关怀管理详情
     */
    @PostMapping(value = "/detail")
    @ResponseBody
    public Object detail(@RequestParam("leaderId") Long leaderId) {
        LeaderWords leaderWords = leaderService.checkId(leaderId);
        List<LeaderImage> imageList = leaderService.getImageListByLeaderId(leaderId);
        Map<String, Object> data = new HashMap<>();
        data.put("leaderWords", leaderWords);
        data.put("imageList", imageList);
        return new SuccessDataTip(data);
    }
}
