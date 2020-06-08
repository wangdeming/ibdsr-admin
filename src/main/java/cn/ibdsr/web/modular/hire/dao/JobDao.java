package cn.ibdsr.web.modular.hire.dao;

import cn.ibdsr.web.common.persistence.dao.JobMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description 岗位管理Dao
 * @Version V1.0
 * @CreateDate 2019/8/19 16:44
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                岗位管理Dao
 */
public interface JobDao extends JobMapper {

    /**
     * 获取岗位数据列表
     *
     * @param page          分页参数
     * @param condition     搜索关键字
     * @param jobType       岗位类型
     * @param isPublish     发布状态
     * @param orderByField  排序字段
     * @param asc           排序方式
     * @return
     */
    List<Map<String,Object>> list(@Param("page") Page<Map<String,Object>> page,
                                  @Param("condition") String condition,
                                  @Param("jobType") Long jobType,
                                  @Param("isPublish") Integer isPublish,
                                  @Param("orderField") String orderByField,
                                  @Param("isAsc") boolean asc);

    /**
     * 获取有简历投递过的岗位列表
     *
     * @return
     */
    List<Map<String,Object>> resumeList();

    /**
     * 前台展示岗位列表
     *
     * @param jobType   岗位分类
     * @return
     */
    List<JSONObject> indexJobList(@Param("jobType") Long jobType);
}
