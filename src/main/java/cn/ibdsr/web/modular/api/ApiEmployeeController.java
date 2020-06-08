package cn.ibdsr.web.modular.api;

import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.ErrorTip;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.core.util.ToolUtil;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.core.util.FdfsFileUtil;
import cn.ibdsr.web.modular.material.service.IEmployeeService;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 前台员工风采控制器
 * @Version V1.0
 * @CreateDate 2019/9/5 9:32
 * <p>
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/29      lvyou                前台员工风采控制器
 */
@RestController
@RequestMapping(value = "/api/employee")
public class ApiEmployeeController extends BaseController {

    @Resource
    private IEmployeeService employeeService;

    /**
     * 前端首页员工风采列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public Object newsInfoList() {
        List<JSONObject> records = employeeService.indexGetList();
        for (JSONObject temp : records) {
            String coverImage = temp.getString("coverImage");
            temp.put("coverImage", FdfsFileUtil.setFileURL(coverImage));
        }
        return new SuccessDataTip(records);
    }

    /**
     * 前端首页新闻资讯列表
     *
     * @return
     */
    @PostMapping(value = "/detail")
    public Object newsInfoDetail (Long employeeId) {
        if (ToolUtil.isEmpty(employeeId)) {
            return new ErrorTip(BizExceptionEnum.EMPLOYEE_INFO_ID_IS_NULL.getCode(), BizExceptionEnum.EMPLOYEE_INFO_ID_IS_NULL.getMessage());
        }
        JSONObject employeeDetail = employeeService.indexEmployeeDetail(employeeId);
        if (employeeDetail == null) {
            return new ErrorTip(BizExceptionEnum.EMPLOYEE_INFO_NOT_EXIST.getCode(), BizExceptionEnum.EMPLOYEE_INFO_NOT_EXIST.getMessage());
        }
        String coverImage = employeeDetail.getString("coverImage");
        employeeDetail.put("coverImage", FdfsFileUtil.setFileURL(coverImage));
        return new SuccessDataTip(employeeDetail);
    }

}
