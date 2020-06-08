package cn.ibdsr.web.modular.hire.controller;

import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.core.check.StaticCheck;
import cn.ibdsr.web.common.constant.factory.PageFactory;
import cn.ibdsr.web.common.constant.state.PublishStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.Job;
import cn.ibdsr.web.modular.hire.service.IJobService;
import cn.ibdsr.web.modular.hire.transfer.JobDTO;
import cn.ibdsr.web.modular.hire.warpper.JobWarpper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Description 岗位管理控制器
 * @Version V1.0
 * @CreateDate 2019/8/19 16:44
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                岗位管理控制器
 */
@Controller
@RequestMapping("/job")
public class JobController extends BaseController {

    private String PREFIX = "/hire/job/";

    @Resource
    private IJobService jobService;

    /**
     * 跳转到岗位管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "job.html";
    }

    /**
     * 获取岗位管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "condition", required = false) String condition,
                       @RequestParam(value = "jobType", required = false) Long jobType,
                       @RequestParam(value = "isPublish", required = false) Integer isPublish) {
        Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
        List<Map<String, Object>> records = jobService.list(page, condition, jobType, isPublish);
        page.setRecords((List<Map<String, Object>>) new JobWarpper(records).warp());
        return super.packForBT(page);
    }

    /**
     * 跳转到添加岗位管理
     */
    @RequestMapping("/job_add")
    public String jobAdd() {
        return PREFIX + "job_add.html";
    }

    /**
     * 新增岗位信息
     */
    @PostMapping(value = "/add")
    @ResponseBody
    public Object add(@Valid JobDTO param) {
        StaticCheck.check(param);
        return new SuccessDataTip(jobService.addJob(param).getId());
    }

    /**
     * 跳转到修改岗位管理
     */
    @GetMapping("/job_update/{id}")
    public String jobUpdate(@PathVariable("id") Long id, Model model) {
        Job job = jobService.checkJobId(id);
        if (PublishStatus.PUBLISHED.getCode() == job.getIsPublish().intValue()) {
            throw new BussinessException(BizExceptionEnum.JOB_PUBLISH_CANT_EDIT);
        }
        model.addAttribute("job", job);
        return PREFIX + "job_edit.html";
    }

    /**
     * 修改岗位管理
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public Object update(@Valid JobDTO param) {
        StaticCheck.check(param);
        jobService.updateJob(param);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除岗位管理
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam(value = "id") Long id) {
        jobService.logicDelete(id);
        return SUCCESS_TIP;
    }

    /**
     * 发布岗位
     */
    @PostMapping(value = "/publish")
    @ResponseBody
    public Object publish(@RequestParam(value = "id") Long id) {
        jobService.editPublish(id, PublishStatus.PUBLISHED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 取消发布岗位
     */
    @PostMapping(value = "/cancelPublish")
    @ResponseBody
    public Object cancelPublish(@RequestParam(value = "id") Long id) {
        jobService.editPublish(id, PublishStatus.UNPUBLISHED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 岗位管理详情
     */
    @GetMapping(value = "/job_detail/{id}")
    public String detailPage(@PathVariable(value = "id") Long id, Model model) {
        model.addAttribute("id", id);
        return PREFIX + "job_detail.html";
    }

    /**
     * 岗位管理详情
     */
    @PostMapping(value = "/detail")
    @ResponseBody
    public Object detail(@RequestParam(value = "id") Long id) {
        Job job = jobService.checkJobId(id);
        return new SuccessDataTip(job);
    }
}
