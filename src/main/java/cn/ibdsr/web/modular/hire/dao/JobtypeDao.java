package cn.ibdsr.web.modular.hire.dao;

import cn.ibdsr.web.common.persistence.dao.JobTypeMapper;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * @Description 岗位分类管理Dao
 * @Version V1.0
 * @CreateDate 2019/8/19 16:43
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                岗位分类管理Dao
 */
public interface JobtypeDao extends JobTypeMapper {

    /**
     * 获取岗位分类列表
     *
     * @return
     */
    List<Map<String,Object>> list();

    /**
     * 获取当前岗位类型最大排序
     *
     * @return
     */
    Integer getMaxSort();

    /**
     * 获取当前岗位类型最小排序
     *
     * @return
     */
    Integer getMinSort();

    /**
     * 重置岗位分类排序
     *
     * @param sort
     */
    void resetSortBySort(Integer sort);

    /**
     * 前台展示岗位分类列表
     *
     * @return
     */
    List<JSONObject> indexJobTypeList();
}
