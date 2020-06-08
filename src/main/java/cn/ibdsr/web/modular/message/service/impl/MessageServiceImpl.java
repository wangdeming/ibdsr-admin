package cn.ibdsr.web.modular.message.service.impl;

import cn.ibdsr.core.util.ToolUtil;
import cn.ibdsr.web.common.constant.state.DeleteStatus;
import cn.ibdsr.web.common.constant.state.ReadStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.Job;
import cn.ibdsr.web.common.persistence.model.Message;
import cn.ibdsr.web.core.shiro.ShiroKit;
import cn.ibdsr.web.modular.message.dao.MessageDao;
import cn.ibdsr.web.modular.message.transfer.MessageDTO;
import cn.ibdsr.web.modular.message.transfer.MessagePoiVo;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import cn.ibdsr.web.modular.message.service.IMessageService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
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
@Service
public class MessageServiceImpl implements IMessageService {

    @Resource
    private MessageDao messageDao;

    /**
     * 获取留言列表
     *
     * @param page          分页参数
     * @param isRead        阅读状态
     * @param messageSource 留言来源
     * @return
     */
    @Override
    public List<Map<String, Object>> list(Page<Map<String, Object>> page, Integer isRead, Integer messageSource) {
        page.setOpenSort(false);
        return messageDao.list(page, isRead, messageSource, page.getOrderByField(), page.isAsc());
    }

    /**
     * 查阅留言
     *
     * @param messageIds 留言id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readMessage(Long... messageIds) {
        Message updateMessage = new Message();
        updateMessage.setModifiedTime(new Date());
        updateMessage.setModifiedUser(ShiroKit.getUser().getId());
        updateMessage.setIsRead(ReadStatus.READ.getCode());
        messageDao.update(updateMessage, new EntityWrapper<Message>().in("id", messageIds));
    }

    /**
     * 获取留言导出列表
     *
     * @param messageIds 留言id数组
     * @return
     */
    @Override
    public List<MessagePoiVo> getExportList(Long... messageIds) {
        return messageDao.getExportList(messageIds);
    }

    /**
     * 留言添加
     *
     * @param param 留言参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMessage(MessageDTO param) {
        Message addMessage = new Message();
        BeanUtils.copyProperties(param, addMessage);
        addMessage.setIsRead(ReadStatus.UNREAD.getCode());
        addMessage.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        addMessage.setCreatedTime(new Date());
        messageDao.insert(addMessage);
    }

    /**
     * 核查岗位id参数是否合法
     *
     * @param id 岗位id
     * @return
     */
    @Override
    public Message checkMessageId(Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        Message message = new Message();
        message.setId(id);
        message.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        message = messageDao.selectOne(message);
        if (message == null) {
            throw new BussinessException(BizExceptionEnum.MESSAGE_NOT_EXIST);
        }
        return message;
    }
}
