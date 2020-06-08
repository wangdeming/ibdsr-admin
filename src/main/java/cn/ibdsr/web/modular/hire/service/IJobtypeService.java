package cn.ibdsr.web.modular.hire.service;

import cn.ibdsr.web.common.persistence.model.JobType;
import cn.ibdsr.web.modular.hire.transfer.JobTypeDTO;
import com.alibaba.fastjson.JSONObject;

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
public interface IJobtypeService {

    /**
     * 获取岗位分类列表
     *
     * @return
     */
    List<Map<String, Object>> list();

    /**
     * 新增岗位分类管理
     *
     * @param param     岗位分类参数
     */
    void addJobType(JobTypeDTO param);

    /**
     * 修改岗位分类管理
     *
     * @param param     岗位分类参数
     */
    void updateJobType(JobTypeDTO param);

    /**
     * 核查岗位类别id参数是否合法
     *
     * @param jobTypeId 岗位分类id
     * @return
     */
    JobType checkJobTypeId(Long jobTypeId);

    /**
     * 逻辑删除
     *
     * @param jobTypeId 岗位分类id
     */
    void logicDelete(Long jobTypeId);

    /**
     * 岗位分类排序加一
     *
     * @param id    岗位分类id
     */
    void upSort(Long id);

    /**
     * 岗位分类排序减一
     *
     * @param id    岗位分类id
     */
    void downSort(Long id);

    /**
     * 前台展示岗位分类列表
     *
     * @return
     */
    List<JSONObject> indexJobTypeList();
}
