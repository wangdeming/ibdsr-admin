package cn.ibdsr.web.modular.hire.dao;

import cn.ibdsr.web.common.persistence.dao.ResumeMapper;
import cn.ibdsr.web.modular.hire.transfer.ResumePoiVo;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description 简历管理Dao
 * @Version V1.0
 * @CreateDate 2019/8/19 16:44
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                简历管理Dao
 */
public interface ResumeDao extends ResumeMapper {

    /**
     * 获取简历列表
     *
     * @param page          分页参数
     * @param condition     搜索关键字
     * @param isRead        阅读状态
     * @param jobId         投递岗位id
     * @param orderField    排序字段
     * @param isAsc         排序方式
     * @return
     */
    List<Map<String,Object>> list(@Param("page") Page<Map<String,Object>> page,
                                  @Param("condition") String condition,
                                  @Param("isRead") Integer isRead,
                                  @Param("jobId") Long jobId,
                                  @Param("orderField") String orderField,
                                  @Param("isAsc") boolean isAsc);

    /**
     * 获取简历导出列表
     *
     * @param resumeIds
     * @return
     */
    List<ResumePoiVo> getExportList(@Param("resumeIds") Long[] resumeIds);
}
