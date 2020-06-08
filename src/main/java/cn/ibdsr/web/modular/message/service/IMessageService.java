package cn.ibdsr.web.modular.message.service;

import cn.ibdsr.web.common.persistence.model.Message;
import cn.ibdsr.web.modular.message.transfer.MessageDTO;
import cn.ibdsr.web.modular.message.transfer.MessagePoiVo;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * @Description 留言管理Service
 * @Version V1.0
 * @CreateDate 2019/8/19 16:27
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                留言管理Service
 */
public interface IMessageService {

    /**
     * 获取留言列表
     *
     * @param page          分页参数
     * @param isRead        阅读状态
     * @param messageSource 留言来源
     * @return
     */
    List<Map<String,Object>> list(Page<Map<String,Object>> page, Integer isRead, Integer messageSource);

    /**
     * 核查岗位id参数是否合法
     *
     * @param id 岗位id
     * @return
     */
    Message checkMessageId(Long id);

    /**
     * 查阅留言
     *
     * @param messageIds 留言id
     */
    void readMessage(Long... messageIds);

    /**
     * 获取留言导出列表
     *
     * @param messageIds    留言id数组
     * @return
     */
    List<MessagePoiVo> getExportList(Long... messageIds);

    /**
     * 留言添加
     *
     * @param param     留言参数
     */
    void addMessage(MessageDTO param);
}
