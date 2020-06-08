package cn.ibdsr.web.modular.hire.service.impl;

import cn.ibdsr.core.util.ToolUtil;
import cn.ibdsr.web.common.constant.state.DeleteStatus;
import cn.ibdsr.web.common.constant.state.PublishStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.Job;
import cn.ibdsr.web.common.persistence.model.JobType;
import cn.ibdsr.web.common.persistence.model.Resume;
import cn.ibdsr.web.core.shiro.ShiroKit;
import cn.ibdsr.web.modular.hire.dao.JobDao;
import cn.ibdsr.web.modular.hire.dao.JobtypeDao;
import cn.ibdsr.web.modular.hire.dao.ResumeDao;
import cn.ibdsr.web.modular.hire.service.IJobtypeService;
import cn.ibdsr.web.modular.hire.transfer.JobDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import cn.ibdsr.web.modular.hire.service.IJobService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 岗位管理Service
 * @Version V1.0
 * @CreateDate 2019/8/19 16:44
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                岗位管理Service
 */
@Service
public class JobServiceImpl implements IJobService {

    @Resource
    private JobDao jobDao;
    @Resource
    private IJobtypeService jobtypeService;
    @Resource
    private ResumeDao resumeDao;

    /**
     * 获取岗位数据列表
     *
     * @param page      分页参数
     * @param condition 搜索关键字
     * @param jobType   岗位类型
     * @param isPublish 发布状态
     * @return
     */
    @Override
    public List<Map<String, Object>> list(Page<Map<String, Object>> page, String condition, Long jobType, Integer isPublish) {
        if (isPublish != null && StringUtils.isEmpty(PublishStatus.valueOf(isPublish))) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        page.setOpenSort(false);
        return jobDao.list(page, condition, jobType, isPublish, page.getOrderByField(), page.isAsc());
    }

    /**
     * 新增岗位信息
     *
     * @param param 岗位信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Job addJob(JobDTO param) {
        jobtypeService.checkJobTypeId(param.getJobTypeId());
        Job addJob = new Job();
        BeanUtils.copyProperties(param, addJob);
        Date operateTime = new Date();
        Long operateUser = ShiroKit.getUser().getId();
        addJob.setModifiedUser(operateUser);
        addJob.setModifiedTime(operateTime);
        addJob.setCreatedTime(operateTime);
        addJob.setCreatedUser(operateUser);
        addJob.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        addJob.setIsPublish(PublishStatus.UNPUBLISHED.getCode());
        jobDao.insert(addJob);
        return addJob;
    }

    /**
     * 修改岗位信息
     *
     * @param param 岗位信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(JobDTO param) {
        jobtypeService.checkJobTypeId(param.getJobTypeId());
        Job updateJob = checkJobId(param.getId());
        if (PublishStatus.PUBLISHED.getCode() == updateJob.getIsPublish().intValue()) {
            throw new BussinessException(BizExceptionEnum.JOB_PUBLISH_CANT_EDIT);
        }
        BeanUtils.copyProperties(param, updateJob);
        Date operateTime = new Date();
        Long operateUser = ShiroKit.getUser().getId();
        updateJob.setModifiedUser(operateUser);
        updateJob.setModifiedTime(operateTime);
        jobDao.updateById(updateJob);
    }

    /**
     * 逻辑删除
     *
     * @param id 岗位分类id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(Long id) {
        Job job = checkJobId(id);
        if (PublishStatus.PUBLISHED.getCode() == job.getIsPublish().intValue()) {
            throw new BussinessException(BizExceptionEnum.JOB_PUBLISH_CANT_DELETE);
        }
        Integer resumeCount = resumeDao.selectCount(new EntityWrapper<Resume>().eq("job_id", id));
        if (resumeCount != null && resumeCount.intValue() > 0) {
            throw new BussinessException(BizExceptionEnum.JOB_REL_RESUME_CANT_DELETE);
        }
        job.setId(id);
        job.setIsDeleted(DeleteStatus.DELETED.getCode());
        jobDao.updateById(job);
    }

    /**
     * 编辑岗位发布状态
     *
     * @param id   岗位id
     * @param code 发布状态
     */
    @Override
    public void editPublish(Long id, int code) {
        Job job = checkJobId(id);
        job.setIsPublish(code);
        job.setModifiedTime(new Date());
        job.setModifiedUser(ShiroKit.getUser().getId());
        jobDao.updateById(job);
    }

    /**
     * 前台展示岗位列表
     *
     * @param jobType 岗位分类id
     * @return
     */
    @Override
    public List<JSONObject> indexJobList(Long jobType) {
        return jobDao.indexJobList(jobType);
    }

    /**
     * 核查岗位id参数是否合法
     *
     * @param id 岗位id
     * @return
     */
    @Override
    public Job checkJobId(Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        Job job = new Job();
        job.setId(id);
        job.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        job = jobDao.selectOne(job);
        if (job == null) {
            throw new BussinessException(BizExceptionEnum.JOB_NOT_EXIST);
        }
        return job;
    }
}
