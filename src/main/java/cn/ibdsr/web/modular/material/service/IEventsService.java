package cn.ibdsr.web.modular.material.service;

import cn.ibdsr.web.common.persistence.model.EventImage;
import cn.ibdsr.web.common.persistence.model.Events;
import cn.ibdsr.web.modular.material.transfer.EventsDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;

import java.util.List;
import java.util.Map;

/**
 * @Description 大事记管理Service
 * @Version V1.0
 * @CreateDate 2019/8/19 16:38
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                大事记管理Service
 */
public interface IEventsService {

    /**
     * 获取大事记列表
     *
     * @param page      分页参数
     * @param condition 搜索关键字
     * @return
     */
    List<Map<String,Object>> list(Page<Map<String,Object>> page, String condition);

    /**
     * 添加大事记
     *
     * @param param     大事记参数
     */
    void addEvents(EventsDTO param);

    /**
     * 修改大事记
     *
     * @param param     大事记参数
     */
    void updateEvents(EventsDTO param);

    /**
     * 校验大事记id
     *
     * @param eventId
     * @return
     */
    Events checkId(Long eventId);

    /**
     * 获取大事记图片列表
     *
     * @param eventId   大事记id
     * @return
     */
    List<EventImage> getImageListByEventId(Long eventId);

    /**
     * 逻辑删除大事记数据
     *
     * @param eventId
     */
    void logicDelete(Long eventId);

    /**
     * 前台展示大事记列表
     *
     * @return
     */
    List<JSONObject> indexGetList();
}
