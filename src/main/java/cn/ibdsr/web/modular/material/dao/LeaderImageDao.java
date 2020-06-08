package cn.ibdsr.web.modular.material.dao;

import cn.ibdsr.web.common.persistence.dao.LeaderImageMapper;
import cn.ibdsr.web.common.persistence.model.LeaderImage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Description 领导关怀图片管理Dao
 * @Version V1.0
 * @CreateDate 2019/8/19 16:37
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                领导关怀图片管理Dao
 */
public interface LeaderImageDao extends LeaderImageMapper {

    /**
     * 批量插入
     *
     * @param images
     */
    void insertBatch(@Param("images") List<LeaderImage> images);
}
