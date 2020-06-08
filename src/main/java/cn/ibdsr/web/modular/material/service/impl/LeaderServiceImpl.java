package cn.ibdsr.web.modular.material.service.impl;

import cn.ibdsr.core.util.ToolUtil;
import cn.ibdsr.fastdfs.base.service.FdfsClientService;
import cn.ibdsr.web.common.constant.state.DeleteStatus;
import cn.ibdsr.web.common.constant.state.PublishStatus;
import cn.ibdsr.web.common.constant.state.TopStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.LeaderImage;
import cn.ibdsr.web.common.persistence.model.LeaderWords;
import cn.ibdsr.web.core.shiro.ShiroKit;
import cn.ibdsr.web.core.util.FdfsFileUtil;
import cn.ibdsr.web.modular.material.dao.LeaderDao;
import cn.ibdsr.web.modular.material.dao.LeaderImageDao;
import cn.ibdsr.web.modular.material.transfer.LeaderWordsDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import cn.ibdsr.web.modular.material.service.ILeaderService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 领导关怀管理Service
 * @Version V1.0
 * @CreateDate 2019/8/19 16:37
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                领导关怀管理Service
 */
@Service
public class LeaderServiceImpl implements ILeaderService {

    @Resource
    private LeaderDao leaderDao;
    @Resource
    private LeaderImageDao imageDao;
    @Resource
    private FdfsClientService fdfsClientService;

    /**
     * 查询领导关怀列表
     *
     * @param page      分页参数
     * @param condition 搜索关键字
     * @param isPublish 发布状态
     * @return
     */
    @Override
    public List<Map<String, Object>> list(Page<Map<String, Object>> page, String condition, Integer isPublish) {
        page.setOpenSort(false);
        return leaderDao.list(page, condition, isPublish, page.getOrderByField(), page.isAsc());
    }

    /**
     * 新增领导关怀
     *
     * @param param  领导关怀数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LeaderWords addLeaderWords(LeaderWordsDTO param) {
        if (ToolUtil.isEmpty(param.getShowTime())) {
            param.setShowTime(new Date());
        }

        //校验参数
        checkAdd(param, param.getImages());

        //构建领导关怀对象
        LeaderWords addObj = new LeaderWords();
        BeanUtils.copyProperties(param, addObj);
        Date operateTime = new Date();
        Long operateUser = ShiroKit.getUser().getId();
        addObj.setCreatedTime(operateTime);
        addObj.setCreatedUser(operateUser);
        addObj.setModifiedTime(operateTime);
        addObj.setModifiedUser(operateUser);
        addObj.setIsPublish(PublishStatus.UNPUBLISHED.getCode());
        addObj.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        leaderDao.insert(addObj);

        //构建领导关怀关联图片对象
        addLeaderImages(operateTime, operateUser, param.getImages(), addObj);

        return addObj;
    }

    /**
     * 校验id参数
     *
     * @param leaderId  领导关怀id
     * @return
     */
    @Override
    public LeaderWords checkId(Long leaderId) {
        if (ToolUtil.isEmpty(leaderId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        LeaderWords leaderWords = new LeaderWords();
        leaderWords.setId(leaderId);
        leaderWords.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        leaderWords = leaderDao.selectOne(leaderWords);
        if (leaderWords == null) {
            throw new BussinessException(BizExceptionEnum.LEADERS_INFO_NOT_EXIST);
        }
        return leaderWords;
    }

    /**
     * 获取图片列表
     *
     * @param leaderId 领导关怀id
     * @return
     */
    @Override
    public List<LeaderImage> getImageListByLeaderId(Long leaderId) {
        Wrapper<LeaderImage> wrapper = new EntityWrapper<>();
        wrapper.eq("leader_words_id", leaderId);
        wrapper.eq("is_deleted", DeleteStatus.UN_DELETED.getCode());
        List<LeaderImage> imageList = imageDao.selectList(wrapper);
        for (LeaderImage temp : imageList) {
            String imagePath = temp.getPath();
            if (StringUtils.isNotEmpty(imagePath) && imagePath.indexOf(FdfsFileUtil.PREFIX_IMAGE_URL) == -1) {
                temp.setPath(FdfsFileUtil.setFileURL(temp.getPath()));
            }
        }
        return imageList;
    }

    /**
     * 新增领导关怀
     *
     * @param param 领导关怀数据
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLeaderWords(LeaderWordsDTO param) {
        if (ToolUtil.isEmpty(param.getShowTime())) {
            param.setShowTime(new Date());
        }

        //校验参数
        List<String> images = param.getImages();
        LeaderWords updateObj = checkUpdate(param, images);
        for (int i=0; i < images.size(); i++) {
            String imagePath = images.get(i);
            if (imagePath.indexOf(FdfsFileUtil.PREFIX_IMAGE_URL) != -1) {
                images.set(i, FdfsFileUtil.cutFileURL(imagePath));
            }
        }
        //更新领导关怀数据
        if (PublishStatus.PUBLISHED.getCode() == updateObj.getIsPublish().intValue()) {
            throw new BussinessException(BizExceptionEnum.LEADERS_INFO_PUBLISH_CANT_OPERATE);
        }
        BeanUtils.copyProperties(param, updateObj);
        Date operateTime = new Date();
        Long operateUser = ShiroKit.getUser().getId();
        updateObj.setModifiedUser(operateUser);
        updateObj.setModifiedTime(operateTime);
        leaderDao.updateById(updateObj);

        //操作领导关怀关联图片
        List<LeaderImage> imageList = this.getImageListByLeaderId(param.getId());
        List<String> oldImages = new ArrayList<>();
        for (LeaderImage image : imageList) {
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
            LeaderImage updateImage = new LeaderImage();
            updateImage.setIsDeleted(DeleteStatus.DELETED.getCode());
            imageDao.update(updateImage, new EntityWrapper<LeaderImage>().eq("is_deleted", DeleteStatus.UN_DELETED.getCode()).eq("leader_words_id", updateObj.getId()).in("path", deleteImages));
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
            addLeaderImages(operateTime, operateUser, addImages, updateObj);
        }
    }

    /**
     * 逻辑删除领导关怀信息
     *
     * @param leaderId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(Long leaderId) {
        //删除领导关怀信息
        LeaderWords leaderWords = checkId(leaderId);
        if (PublishStatus.PUBLISHED.getCode() == leaderWords.getIsPublish().intValue()) {
            throw new BussinessException(BizExceptionEnum.LEADERS_INFO_PUBLISH_CANT_OPERATE);
        }
        leaderWords.setModifiedUser(ShiroKit.getUser().getId());
        leaderWords.setModifiedTime(new Date());
        leaderWords.setIsDeleted(DeleteStatus.DELETED.getCode());
        leaderDao.updateById(leaderWords);

        //删除领导关怀图片
        List<LeaderImage> imageList = getImageListByLeaderId(leaderId);
        List<String> deleteImages = new ArrayList<>();
        for (LeaderImage image : imageList) {
            deleteImages.add(image.getPath());
        }
        deleteImageFile(deleteImages);

        LeaderImage deleteObj = new LeaderImage();
        deleteObj.setIsDeleted(DeleteStatus.DELETED.getCode());
        imageDao.update(deleteObj, new EntityWrapper<LeaderImage>().eq("is_deleted", DeleteStatus.UN_DELETED.getCode()).eq("leader_words_id", leaderId));
    }

    /**
     * 编辑领导关怀发布状态
     *
     * @param leaderId
     * @param isPublish
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPublish(Long leaderId, int isPublish) {
        LeaderWords leaderWords = checkId(leaderId);
        leaderWords.setModifiedUser(ShiroKit.getUser().getId());
        leaderWords.setModifiedTime(new Date());
        leaderWords.setIsPublish(isPublish);
        leaderDao.updateById(leaderWords);
    }

    /**
     * 前端展示领导关怀列表
     *
     * @return
     */
    @Override
    public List<JSONObject> indexGetList() {
        return leaderDao.indexGetList();
    }

    /**
     * 前端展示领导关怀列表
     *
     * @param leaderId
     * @return
     */
    @Override
    public JSONObject indexLeaderDetail(Long leaderId) {
        return leaderDao.indexLeaderDetail(leaderId);
    }

    /**
     * 批量添加领导关怀关联图片
     * @param operateTime
     * @param operateUser
     * @param images
     * @param addObj
     */
    private void addLeaderImages(Date operateTime, Long operateUser, List<String> images, LeaderWords addObj) {
        //构建领导关怀关联图片对象
        List<LeaderImage> imageList = new ArrayList<>();
        for (String imagePath : images) {
            LeaderImage temp = new LeaderImage();
            temp.setCreatedTime(operateTime);
            temp.setCreatedUser(operateUser);
            temp.setLeaderWordsId(addObj.getId());
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
     * 校验新增参数
     *
     * @param param
     * @param images
     */
    private void checkAdd(LeaderWordsDTO param, List<String> images) {
        if (images == null || images.size() <= 0) {
            throw new BussinessException(BizExceptionEnum.LEADERS_IMAGE_CANT_NULL);
        }
        if (images.size() > 9) {
            throw new BussinessException(BizExceptionEnum.LEADERS_IMAGE_CANT_LARGER);
        }
        checkParam(param);
    }

    /**
     * 校验修改参数
     *
     * @param param
     */
    private LeaderWords checkUpdate(LeaderWordsDTO param, List<String> images) {
        if (images == null || images.size() <= 0) {
            throw new BussinessException(BizExceptionEnum.LEADERS_IMAGE_CANT_NULL);
        }
        if (images.size() > 9) {
            throw new BussinessException(BizExceptionEnum.LEADERS_IMAGE_CANT_LARGER);
        }
        checkParam(param);
        return this.checkId(param.getId());
    }

    /**
     * 校验参数
     *
     * @param param
     */
    private void checkParam(LeaderWordsDTO param) {
        if (StringUtils.isEmpty(TopStatus.valueOf(param.getIsTop()))) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        Date now = new Date();
        if (now.before(param.getShowTime())) {
            throw new BussinessException(BizExceptionEnum.LEADERS_INFO_SHOW_TIME_ERROR);
        }
    }

    /**
     * 删除领导关怀图片文件
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
