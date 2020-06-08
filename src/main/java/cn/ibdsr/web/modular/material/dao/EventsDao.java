package cn.ibdsr.web.modular.material.dao;

import cn.ibdsr.web.common.persistence.dao.EventsMapper;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description 大事记管理Dao
 * @Version V1.0
 * @CreateDate 2019/8/19 16:38
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                大事记管理Dao
 */
public interface EventsDao extends EventsMapper {

    /**
     * 获取大事记列表
     *
     * @param page      分页参数
     * @param condition 搜索关键字
     * @return
     */
    List<Map<String,Object>> list(@Param("page") Page<Map<String,Object>> page,
                                  @Param("condition") String condition);

    /**
     * 前台展示大事记列表
     *
     * @return
     */
    List<JSONObject> indexGetList();
}
