package cn.ibdsr.web.modular.hire.service.impl;

import cn.ibdsr.core.util.ToolUtil;
import cn.ibdsr.web.common.constant.state.DeleteStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.Job;
import cn.ibdsr.web.common.persistence.model.JobType;
import cn.ibdsr.web.core.shiro.ShiroKit;
import cn.ibdsr.web.modular.hire.dao.JobDao;
import cn.ibdsr.web.modular.hire.dao.JobtypeDao;
import cn.ibdsr.web.modular.hire.transfer.JobTypeDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import org.springframework.stereotype.Service;
import cn.ibdsr.web.modular.hire.service.IJobtypeService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 岗位分类管理Service
 * @Version V1.0
 * @CreateDate 2019/8/19 16:43
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                岗位分类管理Service
 */
@Service
public class JobtypeServiceImpl implements IJobtypeService {

    @Resource
    private JobtypeDao jobtypeDao;
    @Resource
    private JobDao jobDao;

    /**
     * 获取岗位分类列表
     *
     * @return
     */
    @Override
    public List<Map<String, Object>> list() {
        return jobtypeDao.list();
    }

    /**
     * 新增岗位分类管理
     *
     * @param param 岗位分类参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addJobType(JobTypeDTO param) {
        JobType addJobType = new JobType();
        addJobType.setName(param.getName());
        addJobType.setCreatedUser(ShiroKit.getUser().getId());
        addJobType.setCreatedTime(new Date());
        addJobType.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        Integer max = jobtypeDao.getMaxSort();
        if (max == null || max.intValue() == 0) {
            addJobType.setSort(1);
        } else {
            addJobType.setSort(max + 1);
        }
        jobtypeDao.insert(addJobType);
    }

    /**
     * 修改岗位分类管理
     *
     * @param param 岗位分类参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobType(JobTypeDTO param) {
        JobType updateJobType = checkJobTypeId(param.getId());
        updateJobType.setName(param.getName());
        updateJobType.setModifiedTime(new Date());
        updateJobType.setModifiedUser(ShiroKit.getUser().getId());
        jobtypeDao.updateById(updateJobType);
    }

    /**
     * 逻辑删除
     *
     * @param jobTypeId 岗位分类id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(Long jobTypeId) {
        JobType jobType = checkJobTypeId(jobTypeId);
        Integer count = jobDao.selectCount(new EntityWrapper<Job>().eq("job_type_id", jobTypeId).eq("is_deleted", DeleteStatus.UN_DELETED.getCode()));
        if (count != null && count.intValue() > 0) {
            throw new BussinessException(BizExceptionEnum.JOB_TYPE_CONTAIN_JOB);
        }
        jobType.setModifiedUser(ShiroKit.getUser().getId());
        jobType.setModifiedTime(new Date());
        jobType.setIsDeleted(DeleteStatus.DELETED.getCode());
        jobtypeDao.updateById(jobType);
        //重置岗位分类排序
        Integer sort = jobType.getSort();
        jobtypeDao.resetSortBySort(sort);
    }

    /**
     * 岗位分类排序加一
     *
     * @param id 岗位分类id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void upSort(Long id) {
        JobType jobType = checkJobTypeId(id);
        //获取当前岗位类型最大排序
        Integer min = jobtypeDao.getMinSort();
        if (min.equals(jobType.getSort())) {
            throw new BussinessException(BizExceptionEnum.JOB_TYPE_SORT_IS_MAX);
        }
        //与前一个岗位分类调换排序值
        Integer oldSort = jobType.getSort();
        Integer newSort = oldSort - 1;

        JobType param = new JobType();
        param.setSort(oldSort);
        jobtypeDao.update(param, new EntityWrapper<JobType>().eq("sort", newSort));

        jobType.setSort(newSort);
        jobType.setModifiedTime(new Date());
        jobType.setModifiedUser(ShiroKit.getUser().getId());
        jobtypeDao.updateById(jobType);
    }

    /**
     * 岗位分类排序减一
     *
     * @param id 岗位分类id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void downSort(Long id) {
        JobType jobType = checkJobTypeId(id);
        //获取当前岗位类型最小排序
        Integer max = jobtypeDao.getMaxSort();
        if (max.equals(jobType.getSort())) {
            throw new BussinessException(BizExceptionEnum.JOB_TYPE_SORT_IS_MIN);
        }
        //与后一个岗位分类调换排序值
        Integer oldSort = jobType.getSort();
        Integer newSort = oldSort + 1;

        JobType param = new JobType();
        param.setSort(oldSort);
        jobtypeDao.update(param, new EntityWrapper<JobType>().eq("sort", newSort));

        jobType.setSort(newSort);
        jobType.setModifiedTime(new Date());
        jobType.setModifiedUser(ShiroKit.getUser().getId());
        jobtypeDao.updateById(jobType);
    }

    /**
     * 前台展示岗位分类列表
     *
     * @return
     */
    @Override
    public List<JSONObject> indexJobTypeList() {
        return jobtypeDao.indexJobTypeList();
    }

    /**
     * 核查岗位类别id参数是否合法
     *
     * @param jobTypeId 岗位分类id
     * @return
     */
    @Override
    public JobType checkJobTypeId(Long jobTypeId) {
        if (ToolUtil.isEmpty(jobTypeId)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        JobType jobType = new JobType();
        jobType.setId(jobTypeId);
        jobType.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        jobType = jobtypeDao.selectOne(jobType);
        if (jobType == null) {
            throw new BussinessException(BizExceptionEnum.JOB_TYPE_NOT_EXIST);
        }
        return jobType;
    }
}
