package cn.ibdsr.web.modular.material.dao;

import cn.ibdsr.web.common.persistence.dao.EventImageMapper;
import cn.ibdsr.web.common.persistence.model.EventImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 大事记图片管理Dao
 * @Version V1.0
 * @CreateDate 2019/8/19 16:38
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                大事记图片管理Dao
 */
public interface EventImageDao extends EventImageMapper {

    /**
     * 批量新增大事记图片
     *
     * @param imageList
     */
    void insertBatch(@Param("images") List<EventImage> imageList);
}
