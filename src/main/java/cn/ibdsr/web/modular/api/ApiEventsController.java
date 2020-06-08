package cn.ibdsr.web.modular.api;

import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.web.modular.material.service.IEventsService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 前台大事记控制器
 * @Version V1.0
 * @CreateDate 2019/9/5 9:32
 * <p>
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/29      lvyou                前台大事记控制器
 */
@RestController
@RequestMapping(value = "/api/events")
public class ApiEventsController extends BaseController {

    @Resource
    private IEventsService eventsService;

    /**
     * 前台展示大事记列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public Object eventList(){
        List<JSONObject> records = eventsService.indexGetList();
        return new SuccessDataTip(records);
    }

}
