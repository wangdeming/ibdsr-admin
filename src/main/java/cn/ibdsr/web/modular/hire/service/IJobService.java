package cn.ibdsr.web.modular.hire.service;

import cn.ibdsr.web.common.persistence.model.Job;
import cn.ibdsr.web.modular.hire.transfer.JobDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;

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
public interface IJobService {

    /**
     * 获取岗位数据列表
     *
     * @param page      分页参数
     * @param condition 搜索关键字
     * @param jobType   岗位类型
     * @param isPublish 发布状态
     * @return
     */
    List<Map<String,Object>> list(Page<Map<String,Object>> page, String condition, Long jobType, Integer isPublish);

    /**
     * 新增岗位信息
     *
     * @param param     岗位信息
     */
    Job addJob(JobDTO param);

    /**
     * 核查岗位id参数是否合法
     *
     * @param id    岗位id
     * @return
     */
    Job checkJobId(Long id);

    /**
     * 修改岗位信息
     *
     * @param param     岗位信息
     */
    void updateJob(JobDTO param);

    /**
     * 逻辑删除
     *
     * @param id 岗位分类id
     */
    void logicDelete(Long id);

    /**
     * 编辑岗位发布状态
     *
     * @param id    岗位id
     * @param code  发布状态
     */
    void editPublish(Long id, int code);

    /**
     * 前台展示岗位列表
     *
     * @param jobType   岗位分类id
     * @return
     */
    List<JSONObject> indexJobList(Long jobType);
}
