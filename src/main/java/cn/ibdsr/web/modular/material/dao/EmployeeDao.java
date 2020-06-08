package cn.ibdsr.web.modular.material.dao;

import cn.ibdsr.web.common.persistence.dao.EmployeeMapper;
import cn.ibdsr.web.common.persistence.model.Employee;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description 员工数风采管理Dao
 * @Version V1.0
 * @CreateDate 2019/8/19 16:38
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                员工风采管理Dao
 */
public interface EmployeeDao extends EmployeeMapper {

    /**
     * 获取员工风采列表
     *
     * @param page          分页参数
     * @param condition     搜索关键字
     * @param isPublish     发布状态
     * @param orderField    搜索排序字段
     * @param isAsc         排序方式
     * @return
     */
    List<Map<String,Object>> list(@Param("page") Page<Map<String, Object>> page,
                                  @Param("condition") String condition,
                                  @Param("isPublish") Integer isPublish,
                                  @Param("orderField") String orderField,
                                  @Param("isAsc") boolean isAsc);

    /**
     * 添加员工风采信息
     *
     * @param employee
     */
    void add(@Param("addObj") Employee employee);

    /**
     * 修改员工风采信息
     *
     * @param employee
     */
    void edit(@Param("updateObj") Employee employee);

    /**
     * 前端首页员工风采列表
     *
     * @return
     */
    List<JSONObject> indexGetList();

    /**
     * 前端首页员工风采详情
     *
     * @param employeeId 员工风采id
     * @return
     */
    JSONObject indexEmployeeDetail(@Param("employeeId") Long employeeId);
}
