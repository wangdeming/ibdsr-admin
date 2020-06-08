package cn.ibdsr.web.modular.hire.controller;

import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.core.check.StaticCheck;
import cn.ibdsr.web.common.persistence.model.JobType;
import cn.ibdsr.web.modular.hire.service.IJobtypeService;
import cn.ibdsr.web.modular.hire.transfer.JobTypeDTO;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * @Description 岗位分类管理控制器
 * @Version V1.0
 * @CreateDate 2019/8/19 16:43
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                岗位分类管理控制器
 */
@Controller
@RequestMapping("/jobtype")
public class JobtypeController extends BaseController {

    private String PREFIX = "/hire/jobtype/";

    @Resource
    private IJobtypeService jobtypeService;

    /**
     * 跳转到岗位分类管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "jobtype.html";
    }

    /**
     * 获取岗位分类管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list() {
        Page<Map<String, Object>> page = new Page<>();
        page.setRecords(jobtypeService.list());
        return super.packForBT(page);
    }

    /**
     * 跳转到添加岗位分类管理
     */
    @RequestMapping("/jobtype_add")
    public String jobtypeAdd() {
        return PREFIX + "jobtype_add.html";
    }

    /**
     * 新增岗位分类管理
     *
     * @param param     岗位分类参数
     * @return
     */
    @PostMapping(value = "/add")
    @ResponseBody
    public Object add(@Valid JobTypeDTO param) {
        StaticCheck.check(param);
        jobtypeService.addJobType(param);
        return super.SUCCESS_TIP;
    }

    /**
     * 跳转到修改岗位分类页面
     */
    @RequestMapping("/jobtype_update/{id}")
    public String jobtypeUpdate(@PathVariable("id") Long id, Model model) {
        JobType jobType = jobtypeService.checkJobTypeId(id);
        model.addAttribute("jobType", jobType);
        return PREFIX + "jobtype_edit.html";
    }

    /**
     * 修改岗位分类信息
     *
     * @param param     岗位分类参数
     * @return
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public Object update(@Valid JobTypeDTO param) {
        StaticCheck.check(param);
        jobtypeService.updateJobType(param);
        return super.SUCCESS_TIP;
    }

    /**
     * 删除岗位分类管理
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam(value = "id") Long id) {
        jobtypeService.logicDelete(id);
        return SUCCESS_TIP;
    }

    /**
     * 岗位分类排序上调
     */
    @PostMapping(value = "/up_sort")
    @ResponseBody
    public Object upSort(@RequestParam(value = "id") Long id) {
        jobtypeService.upSort(id);
        return SUCCESS_TIP;
    }

    /**
     * 岗位分类排序下调
     */
    @PostMapping(value = "/down_sort")
    @ResponseBody
    public Object downSort(@RequestParam(value = "id") Long id) {
        jobtypeService.downSort(id);
        return SUCCESS_TIP;
    }

}
