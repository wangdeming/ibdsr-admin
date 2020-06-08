package cn.ibdsr.web.modular.material.controller;

import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.core.check.StaticCheck;
import cn.ibdsr.web.common.constant.factory.PageFactory;
import cn.ibdsr.web.common.constant.state.PublishStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.Employee;
import cn.ibdsr.web.core.util.FdfsFileUtil;
import cn.ibdsr.web.modular.material.service.IEmployeeService;
import cn.ibdsr.web.modular.material.transfer.EmployeeDTO;
import cn.ibdsr.web.modular.material.warpper.EmployeeWarpper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Description 员工数风采管理控制器
 * @Version V1.0
 * @CreateDate 2019/8/19 16:38
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                员工风采管理控制器
 */
@Controller
@RequestMapping("/employee")
public class EmployeeController extends BaseController {

    private String PREFIX = "/material/employee/";

    @Resource
    private IEmployeeService employeeService;

    /**
     * 跳转到员工风采管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "employee.html";
    }

    /**
     * 获取员工风采列表
     */
    @GetMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "condition", required = false) String condition,
                       @RequestParam(value = "isPublish", required = false) Integer isPublish) {
        if (isPublish != null && StringUtils.isEmpty(PublishStatus.valueOf(isPublish))) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
        List<Map<String, Object>> records = employeeService.list(page, condition, isPublish);
        page.setRecords((List<Map<String, Object>>) new EmployeeWarpper(records).warp());
        return super.packForBT(page);
    }

    /**
     * 跳转到添加员工风采
     */
    @RequestMapping("/employee_add")
    public String employeeAdd() {
        return PREFIX + "employee_add.html";
    }

    /**
     * 新增员工风采
     */
    @PostMapping(value = "/add")
    @ResponseBody
    public Object add(@Valid @RequestBody EmployeeDTO param) {
        StaticCheck.check(param);
        return new SuccessDataTip(employeeService.addEmployee(param).getId());
    }

    /**
     * 跳转到修改员工风采
     */
    @RequestMapping("/employee_update/{employeeId}")
    public String employeeUpdate(@PathVariable Long employeeId, Model model) {
        model.addAttribute("employeeId", employeeId);
        return PREFIX + "employee_edit.html";
    }

    /**
     * 修改员工风采
     */
    @PostMapping(value = "/update")
    @ResponseBody
    public Object update(@Valid @RequestBody EmployeeDTO param) {
        StaticCheck.check(param);
        employeeService.updateEmployee(param);
        return super.SUCCESS_TIP;
    }

    /**
     * 跳转到修改员工风采
     */
    @RequestMapping("/employee_detail/{employeeId}")
    public String employeeDetail(@PathVariable Long employeeId, Model model) {
        model.addAttribute("employeeId", employeeId);
        return PREFIX + "employee_detail.html";
    }

    /**
     * 员工风采详情
     */
    @PostMapping(value = "/detail")
    @ResponseBody
    public Object detail(@RequestParam(value = "employeeId") Long employeeId) {
        Employee employee = employeeService.checkId(employeeId);
        String coverImage = employee.getCoverImage();
        if (StringUtils.isNotEmpty(coverImage) && coverImage.indexOf(FdfsFileUtil.PREFIX_IMAGE_URL) == -1) {
            coverImage = FdfsFileUtil.setFileURL(coverImage);
        }
        employee.setCoverImage(coverImage);
        return new SuccessDataTip(employee);
    }

    /**
     * 发布员工风采
     */
    @PostMapping(value = "/publish")
    @ResponseBody
    public Object publish(@RequestParam(value = "employeeId") Long employeeId) {
        employeeService.editPublish(employeeId, PublishStatus.PUBLISHED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 取消发布员工风采
     */
    @PostMapping(value = "/cancelPublish")
    @ResponseBody
    public Object cancelPublish(@RequestParam(value = "employeeId") Long employeeId) {
        employeeService.editPublish(employeeId, PublishStatus.UNPUBLISHED.getCode());
        return SUCCESS_TIP;
    }

    /**
     * 删除员工风采管理
     */
    @PostMapping(value = "/delete")
    @ResponseBody
    public Object delete(@RequestParam("employeeId") Long employeeId) {
        employeeService.logicDelete(employeeId);
        return SUCCESS_TIP;
    }

}
