package cn.ibdsr.web.modular.hire.service.impl;

import cn.ibdsr.core.base.tips.ErrorTip;
import cn.ibdsr.core.base.tips.SuccessTip;
import cn.ibdsr.core.base.tips.Tip;
import cn.ibdsr.core.util.ToolUtil;
import cn.ibdsr.fastdfs.base.service.FdfsClientService;
import cn.ibdsr.web.common.constant.state.DeleteStatus;
import cn.ibdsr.web.common.constant.state.ReadStatus;
import cn.ibdsr.web.common.constant.state.ResumeExtType;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.Resume;
import cn.ibdsr.web.core.shiro.ShiroKit;
import cn.ibdsr.web.modular.hire.dao.JobDao;
import cn.ibdsr.web.modular.hire.dao.ResumeDao;
import cn.ibdsr.web.modular.hire.transfer.ResumeDTO;
import cn.ibdsr.web.modular.hire.transfer.ResumePoiVo;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import cn.ibdsr.web.modular.hire.service.IResumeService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 简历管理Service
 * @Version V1.0
 * @CreateDate 2019/8/19 16:44
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      xxx            类说明
 */
@Service
public class ResumeServiceImpl implements IResumeService {

    @Resource
    private ResumeDao resumeDao;
    @Resource
    private JobDao jobDao;
    @Resource
    private FdfsClientService fdfsClientService;


    /**
     * 获取简历列表
     *
     * @param page      分页参数
     * @param condition 搜索关键字
     * @param isRead    阅读状态
     * @param jobId     投递岗位id
     * @return
     */
    @Override
    public List<Map<String, Object>> list(Page<Map<String, Object>> page, String condition, Integer isRead, Long jobId) {
        if (isRead != null && StringUtils.isEmpty(ReadStatus.valueOf(isRead))) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        page.setOpenSort(false);//关闭自动排序
        return resumeDao.list(page, condition, isRead, jobId, page.getOrderByField(), page.isAsc());
    }

    /**
     * 获取有简历投递过的岗位列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> jobList() {
        return jobDao.resumeList();
    }

    /**
     * 查阅简历
     *
     * @param resumeIds 简历id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readResume(Long[] resumeIds) {
        Resume updateResume = new Resume();
        updateResume.setModifiedTime(new Date());
        updateResume.setModifiedUser(ShiroKit.getUser().getId());
        updateResume.setIsRead(ReadStatus.READ.getCode());
        resumeDao.update(updateResume, new EntityWrapper<Resume>().in("id", resumeIds));
    }

    /**
     * 获取简历导出列表
     *
     * @param resumeIds
     * @return
     */
    @Override
    public List<ResumePoiVo> getExportList(Long[] resumeIds) {
        return resumeDao.getExportList(resumeIds);
    }

    /**
     * 简历投递
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Tip resumeCommit(ResumeDTO param) {
        MultipartFile resumeFile = param.getResumeFile();
        String resumeName = resumeFile.getOriginalFilename();
        String ext = resumeName.substring(resumeName.lastIndexOf(".") + 1);
        try {
            if (ToolUtil.isEmpty(ResumeExtType.objOf(ext))) {
                return new ErrorTip(BizExceptionEnum.RESUME_TYPE_ERROR.getCode(), BizExceptionEnum.RESUME_TYPE_ERROR.getMessage());
            }
        } catch (Exception e) {
            return new ErrorTip(BizExceptionEnum.RESUME_TYPE_ERROR.getCode(), BizExceptionEnum.RESUME_TYPE_ERROR.getMessage());
        }
        if (resumeFile.getSize() > 3 * 1024 * 1024) {
            return new ErrorTip(BizExceptionEnum.RESUME_FILE_LARGER.getCode(), BizExceptionEnum.RESUME_FILE_LARGER.getMessage());
        }
        String resumePath = null;
        try {
            resumePath = fdfsClientService.uploadFile(resumeFile.getBytes(), FilenameUtils.getExtension(resumeFile.getOriginalFilename()));
        } catch (Exception e) {
        }
        if (StringUtils.isEmpty(resumePath)) {
            return new ErrorTip(BizExceptionEnum.UPLOAD_ERROR.getCode(), BizExceptionEnum.UPLOAD_ERROR.getMessage());
        }
        param.setResumePath(resumePath);
        param.setResumeName(resumeName);
        param.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        param.setIsRead(ReadStatus.UNREAD.getCode());
        param.setCreatedTime(new Date());
        Resume addResume = new Resume();
        BeanUtils.copyProperties(param, addResume);
        resumeDao.insert(addResume);
        return new SuccessTip();
    }
}
