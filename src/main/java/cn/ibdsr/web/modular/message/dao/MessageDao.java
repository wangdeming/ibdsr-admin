package cn.ibdsr.web.modular.message.dao;

import cn.ibdsr.web.common.persistence.dao.MessageMapper;
import cn.ibdsr.web.modular.message.transfer.MessagePoiVo;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Description 留言管理Dao
 * @Version V1.0
 * @CreateDate 2019/8/19 16:27
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                留言管理Dao
 */
public interface MessageDao extends MessageMapper {

    /**
     * 获取留言列表
     *
     * @param page          分页参数
     * @param isRead        阅读状态
     * @param messageSource 留言来源
     * @param orderField    排序字段
     * @param isAsc         排序方式
     * @return
     */
    List<Map<String, Object>> list(@Param("page") Page<Map<String, Object>> page,
                                   @Param("isRead") Integer isRead,
                                   @Param("messageSource") Integer messageSource,
                                   @Param("orderField") String orderField,
                                   @Param("isAsc") boolean isAsc);

    /**
     * 获取留言导出列表
     *
     * @param messageIds    留言id数组
     * @return
     */
    List<MessagePoiVo> getExportList(@Param("messageIds") Long... messageIds);
}
