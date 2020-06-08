package cn.ibdsr.web.modular.material.dao;

import cn.ibdsr.web.common.persistence.dao.LeaderWordsMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description 领导关怀管理Dao
 * @Version V1.0
 * @CreateDate 2019/8/19 16:37
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                领导关怀管理Dao
 */
public interface LeaderDao extends LeaderWordsMapper {

    /**
     * 查询领导关怀列表
     *
     * @param page          分页参数
     * @param condition     搜索关键字
     * @param isPublish     发布状态
     * @param orderField    排序字段
     * @param isAsc         排序方式
     * @return
     */
    List<Map<String,Object>> list(@Param("page") Page<Map<String,Object>> page,
                                  @Param("condition") String condition,
                                  @Param("isPublish") Integer isPublish,
                                  @Param("orderField") String orderField,
                                  @Param("isAsc") boolean isAsc);

    /**
     * 前端展示领导关怀列表
     *
     * @return
     */
    List<JSONObject> indexGetList();

    /**
     * 前端展示领导关怀详情
     *
     * @return
     */
    JSONObject indexLeaderDetail(@Param("leaderId") Long leaderId);
}
