package cn.ibdsr.web.modular.hire.service;

import cn.ibdsr.core.base.tips.Tip;
import cn.ibdsr.web.modular.hire.transfer.ResumeDTO;
import cn.ibdsr.web.modular.hire.transfer.ResumePoiVo;
import com.baomidou.mybatisplus.plugins.Page;

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
public interface IResumeService {

    /**
     * 获取简历列表
     *
     * @param page          分页参数
     * @param condition     搜索关键字
     * @param isRead        阅读状态
     * @param jobId         投递岗位id
     * @return
     */
    List<Map<String,Object>> list(Page<Map<String,Object>> page, String condition, Integer isRead, Long jobId);

    /**
     * 查阅简历
     *
     * @param resumeIds     简历id
     */
    void readResume(Long[] resumeIds);

    /**
     * 获取简历导出列表
     *
     * @param resumeIds
     * @return
     */
    List<ResumePoiVo> getExportList(Long[] resumeIds);

    /**
     * 获取有简历投递过的岗位列表
     *
     * @return
     */
    List<Map<String,Object>> jobList();

    /**
     * 简历投递
     *
     * @param param
     */
    Tip resumeCommit(ResumeDTO param);
}
