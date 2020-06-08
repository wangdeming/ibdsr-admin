package cn.ibdsr.web.modular.material.service.impl;

import cn.ibdsr.core.util.DateUtil;
import cn.ibdsr.core.util.ToolUtil;
import cn.ibdsr.fastdfs.base.service.FdfsClientService;
import cn.ibdsr.web.common.constant.Const;
import cn.ibdsr.web.common.constant.state.DeleteStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.EventImage;
import cn.ibdsr.web.common.persistence.model.Events;
import cn.ibdsr.web.core.shiro.ShiroKit;
import cn.ibdsr.web.core.util.FdfsFileUtil;
import cn.ibdsr.web.modular.material.dao.EventImageDao;
import cn.ibdsr.web.modular.material.dao.EventsDao;
import cn.ibdsr.web.modular.material.transfer.EventsDTO;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import cn.ibdsr.web.modular.material.service.IEventsService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
@Service
public class EventsServiceImpl implements IEventsService {

    @Resource
    private EventsDao eventsDao;
    @Resource
    private EventImageDao imageDao;
    @Resource
    private FdfsClientService fdfsClientService;

    /**
     * 获取大事记列表
     *
     * @param page      分页参数
     * @param condition 搜索关键字
     * @return
     */
    @Override
    public List<Map<String, Object>> list(Page<Map<String, Object>> page, String condition) {
        return eventsDao.list(page, condition);
    }

    /**
     * 添加大事记
     *
     * @param param  大事记参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addEvents(EventsDTO param) {
        checkEventDate(param.getEventYear(), param.getEventMonth(), param.getEventDay());
        List<String> imageList = param.getImages();
        checkImages(imageList);

        Events addEvent = new Events();
        BeanUtils.copyProperties(param, addEvent);
        Date operateTime = new Date();
        Long operateUser = ShiroKit.getUser().getId();
        addEvent.setCreatedTime(operateTime);
        addEvent.setCreatedUser(operateUser);
        addEvent.setModifiedTime(operateTime);
        addEvent.setModifiedUser(operateUser);
        addEvent.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        eventsDao.insert(addEvent);

        if (imageList != null && imageList.size() > 0) {
            //构建领导关怀关联图片对象
            addEventImages(operateTime, operateUser, param.getImages(), addEvent);
        }
    }

    /**
     * 修改大事记
     *
     * @param param 大事记参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEvents(EventsDTO param) {
        //参数校验
        List<String> images = param.getImages();
        Events updateEvent = checkId(param.getId());
        checkEventDate(param.getEventYear(), param.getEventMonth(), param.getEventDay());
        checkImages(images);
        for (int i=0; i < images.size(); i++) {
            String imagePath = images.get(i);
            if (imagePath.indexOf(FdfsFileUtil.PREFIX_IMAGE_URL) != -1) {
                images.set(i, FdfsFileUtil.cutFileURL(imagePath));
            }
        }

        //更新大事记数据
        BeanUtils.copyProperties(param, updateEvent);
        Date operateTime = new Date();
        Long operateUser = ShiroKit.getUser().getId();
        updateEvent.setModifiedTime(operateTime);
        updateEvent.setModifiedUser(operateUser);
        eventsDao.updateById(updateEvent);

        //操作大事记关联图片
        List<EventImage> imageList = this.getImageListByEventId(param.getId());
        List<String> oldImages = new ArrayList<>();
        for (EventImage image : imageList) {
            oldImages.add(FdfsFileUtil.cutFileURL(image.getPath()));
        }
        //需要删除的图片
        List<String> deleteImages = new ArrayList<>();
        for (String imagePath : oldImages) {
            if (!images.contains(imagePath)) {
                deleteImages.add(imagePath);
            }
        }
        if (deleteImages.size() > 0) {
            EventImage updateImage = new EventImage();
            updateImage.setIsDeleted(DeleteStatus.DELETED.getCode());
            imageDao.update(updateImage, new EntityWrapper<EventImage>().eq("is_deleted", DeleteStatus.UN_DELETED.getCode()).eq("event_id", updateEvent.getId()).in("path", deleteImages));
        }
        deleteImageFile(deleteImages);

        //需要新增的图片
        List<String> addImages = new ArrayList<>();
        for (String imagePath : images) {
            if (!oldImages.contains(imagePath)) {
                addImages.add(imagePath);
            }
        }
        if (addImages.size() > 0) {
            //构建领导关怀关联图片对象
            addEventImages(operateTime, operateUser, addImages, updateEvent);
        }
    }

    /**
     * 获取大事记图片列表
     *
     * @param eventId 大事记id
     * @return
     */
    @Override
    public List<EventImage> getImageListByEventId(Long eventId) {
        List<EventImage> images = imageDao.selectList(new EntityWrapper<EventImage>().eq("event_id", eventId).eq("is_deleted", DeleteStatus.UN_DELETED.getCode()));
        for (EventImage temp : images) {
            String imagePath = temp.getPath();
            if (StringUtils.isNotEmpty(imagePath) && imagePath.indexOf(FdfsFileUtil.PREFIX_IMAGE_URL) == -1) {
                imagePath = FdfsFileUtil.setFileURL(imagePath);
                temp.setPath(imagePath);
            }
        }
        return images;
    }

    /**
     * 逻辑删除大事记数据
     *
     * @param eventId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(Long eventId) {
        //逻辑删除大事记数据
        Events deleteEvent = checkId(eventId);
        deleteEvent.setModifiedTime(new Date());
        deleteEvent.setModifiedUser(ShiroKit.getUser().getId());
        deleteEvent.setIsDeleted(DeleteStatus.DELETED.getCode());
        eventsDao.updateById(deleteEvent);

        //删除关联图片文件
        List<EventImage> images = this.getImageListByEventId(eventId);
        List<String> deleteImages = new ArrayList<>();
        for (EventImage image : images) {
            deleteImages.add(image.getPath());
        }
        deleteImageFile(deleteImages);

        EventImage deleteImage = new EventImage();
        deleteImage.setIsDeleted(DeleteStatus.DELETED.getCode());
        imageDao.update(deleteImage, new EntityWrapper<EventImage>().eq("is_deleted", DeleteStatus.UN_DELETED.getCode()).eq("event_id", eventId));
    }

    /**
     * 批量添加领导关怀关联图片
     * @param operateTime
     * @param operateUser
     * @param images
     * @param addObj
     */
    private void addEventImages(Date operateTime, Long operateUser, List<String> images, Events addObj) {
        //构建领导关怀关联图片对象
        List<EventImage> imageList = new ArrayList<>();
        for (String imagePath : images) {
            EventImage temp = new EventImage();
            temp.setCreatedTime(operateTime);
            temp.setCreatedUser(operateUser);
            temp.setEventId(addObj.getId());
            temp.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
            if (imagePath.indexOf(FdfsFileUtil.PREFIX_IMAGE_URL) != -1) {
                imagePath = FdfsFileUtil.cutFileURL(imagePath);
            }
            temp.setPath(imagePath);
            imageList.add(temp);
        }
        //批量插入
        imageDao.insertBatch(imageList);
    }

    /**
     * 校验大事记id
     *
     * @param eventId
     * @return
     */
    @Override
    public Events checkId(Long eventId) {
        if (ToolUtil.isEmpty(eventId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        Events events = new Events();
        events.setId(eventId);
        events.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        events = eventsDao.selectOne(events);
        if (events == null) {
            throw new BussinessException(BizExceptionEnum.EVENTS_INFO_NOT_EXIST);
        }
        return events;
    }

    /**
     * 前台展示大事记列表
     *
     * @return
     */
    @Override
    public List<JSONObject> indexGetList() {
        List<JSONObject> records = eventsDao.indexGetList();
        for (JSONObject event : records) {
            String eventDate = constructEventDate(event.getInteger("eventYear"), event.getInteger("eventMonth"), event.getInteger("eventDay"));
            event.put("eventDate", eventDate);
            JSONArray imageList = event.getJSONArray("imageList");
            for (int i = 0; i < imageList.size(); i++) {
                JSONObject image = imageList.getJSONObject(i);
                String imagePath = image.getString("imagePath");
                image.put("imagePath", FdfsFileUtil.setFileURL(imagePath));
            }
        }
        return records;
    }

    /**
     * 校验图片数量
     *
     * @param images
     */
    private void checkImages(List<String> images) {
        int imageMaxSize = 9;
        if (images != null && images.size() > imageMaxSize) {
            throw new BussinessException(BizExceptionEnum.EVENTS_IMAGE_CANT_LARGER);
        }
    }

    /**
     * 构建大事记日期
     *
     * @param eventYear
     * @param eventMonth
     * @param eventDay
     * @return
     */
    private String constructEventDate(Integer eventYear, Integer eventMonth, Integer eventDay){
        StringBuilder eventDate = new StringBuilder();
        if (eventMonth != null) {
            eventDate.append(eventYear).append("年");
        }
        if (eventMonth != null) {
            eventDate.append(eventMonth).append("月");
        }
        if (eventDay != null) {
            eventDate.append(eventDay).append("日");
        }
        return eventDate.toString();
    }

    /**
     * 校验大大事记日期格式
     *
     * @param eventYear
     * @param eventMonth
     * @param eventDay
     */
    private void checkEventDate(Integer eventYear, Integer eventMonth, Integer eventDay) {
        StringBuilder eventDate = new StringBuilder();
        Date date = null;
        SimpleDateFormat format = null;
        if (eventYear == null || eventYear < Const.EVENTS_START_YEAR || eventYear > Integer.parseInt(DateUtil.getYear())) {
            throw new BussinessException(BizExceptionEnum.EVENTS_DATE_ERROR);
        }
        eventDate.append(eventYear);
        if (eventMonth != null) {
            eventDate.append("-").append(eventMonth);
        }
        try {
            if (eventDay != null) {
                eventDate.append("-").append(eventDay);
                format = new SimpleDateFormat("yyyy-MM-dd");
                format.setLenient(false);
                date = format.parse(eventDate.toString());
            } else {
                format = new SimpleDateFormat("yyyy-MM");
                format.setLenient(false);
                date = format.parse(eventDate.toString());
            }
            if (date == null || date.after(new Date())) {
                throw new BussinessException(BizExceptionEnum.EVENTS_DATE_ERROR);
            }
        } catch (Exception e) {
            throw new BussinessException(BizExceptionEnum.EVENTS_DATE_ERROR);
        }
    }

    /**
     * 删除大事记图片文件
     *
     * @param images
     */
    private void deleteImageFile(List<String> images) {
        for (String imagePath : images) {
            if (StringUtils.isNotEmpty(imagePath)) {
                if (imagePath.indexOf(FdfsFileUtil.PREFIX_IMAGE_URL) != -1) {
                    imagePath = FdfsFileUtil.cutFileURL(imagePath);
                }
                fdfsClientService.deleteFile(imagePath);
            }
        }
    }
}
