package cn.ibdsr.web.modular.material.service;

import cn.ibdsr.web.common.persistence.model.Employee;
import cn.ibdsr.web.modular.material.transfer.EmployeeDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * @Description 员工数风采管理Service
 * @Version V1.0
 * @CreateDate 2019/8/19 16:38
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                员工风采管理Service
 */
public interface IEmployeeService {

    /**
     * 获取员工风采列表
     *
     * @param page      分页参数
     * @param condition 搜索关键字
     * @param isPublish 发布状态
     * @return
     */
    List<Map<String,Object>> list(Page<Map<String,Object>> page, String condition, Integer isPublish);

    /**
     * 添加员工风采信息
     *
     * @param param     参数
     * @return
     */
    Employee addEmployee(EmployeeDTO param);

    /**
     * 校验id参数
     *
     * @param id    员工风采id
     * @return
     */
    Employee checkId(Long id);

    /**
     * 修改员工风采信息
     *
     * @param param     参数
     * @return
     */
    void updateEmployee(EmployeeDTO param);

    /**
     * 编辑员工风采发布状态
     *
     * @param employeeId
     * @param isPublish
     */
    void editPublish(Long employeeId, int isPublish);

    /**
     * 逻辑删除员工风采信息
     *
     * @param employeeId      员工风采id
     */
    void logicDelete(Long employeeId);

    /**
     * 前端首页员工风采列表
     *
     * @return
     */
    List<JSONObject> indexGetList();

    /**
     * 前端首页员工风采详情
     *
     * @param employeeId        员工风采id
     * @return
     */
    JSONObject indexEmployeeDetail(Long employeeId);
}
