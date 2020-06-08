package cn.ibdsr.web.modular.api;

import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.SuccessTip;
import cn.ibdsr.core.check.StaticCheck;
import cn.ibdsr.web.modular.message.service.IMessageService;
import cn.ibdsr.web.modular.message.transfer.MessageDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @Description 前台留言控制器
 * @Version V1.0
 * @CreateDate 2019/9/5 9:32
 * <p>
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/29      lvyou                前台留言控制器
 */
@RestController
@RequestMapping(value = "/api/message")
public class ApiMessageController extends BaseController {

    @Resource
    private IMessageService messageService;

    /**
     * 提交留言信息
     *
     * @param param     留言参数
     * @return
     */
    @PostMapping(value = "/commit")
    public Object commitMessage(@Valid @RequestBody MessageDTO param) {
        StaticCheck.check(param);
        messageService.addMessage(param);
        return new SuccessTip();
    }

}
