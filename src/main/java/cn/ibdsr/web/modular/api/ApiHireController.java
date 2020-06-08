package cn.ibdsr.web.modular.api;

import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.core.base.tips.SuccessTip;
import cn.ibdsr.core.check.StaticCheck;
import cn.ibdsr.web.modular.hire.service.IJobService;
import cn.ibdsr.web.modular.hire.service.IJobtypeService;
import cn.ibdsr.web.modular.hire.service.IResumeService;
import cn.ibdsr.web.modular.hire.transfer.ResumeDTO;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description 前台招聘控制器
 * @Version V1.0
 * @CreateDate 2019/9/5 9:32
 * <p>
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/29      lvyou                前台招聘控制器
 */
@RestController
@RequestMapping(value = "/api/hire")
public class ApiHireController extends BaseController {

    @Resource
    private IJobtypeService jobtypeService;
    @Resource
    private IJobService jobService;
    @Resource
    private IResumeService resumeService;

    /**
     * 前台展示岗位分类列表
     *
     * @return
     */
    @GetMapping(value = "/jobtype/list")
    public Object jobtypeList(){
        List<JSONObject> records = jobtypeService.indexJobTypeList();
        int jonTotal = 0;
        for (JSONObject jobtype : records) {
            int jobCount = jobtype.getInteger("jobCount");
            jonTotal += jobCount;
        }
        JSONObject total = new JSONObject();
        total.put("id", 0);
        total.put("name", "全部");
        total.put("jobCount", jonTotal);
        records.add(0, total);
        return new SuccessDataTip(records);
    }

    /**
     * 前台展示岗位列表
     *
     * @return
     */
    @GetMapping(value = "/job/list")
    public Object jobList(@RequestParam(value = "jobType") Long jobType){
        List<JSONObject> records = jobService.indexJobList(jobType);
        return new SuccessDataTip(records);
    }

    /**
     * 前台展示岗位列表
     *
     * @return
     */
    @PostMapping(value = "/resume/commit")
    public Object resumeCommit(ResumeDTO param){
        StaticCheck.check(param);
        return resumeService.resumeCommit(param);
    }
}
