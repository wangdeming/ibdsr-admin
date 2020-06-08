package cn.ibdsr.web.modular.material.service;

import cn.ibdsr.web.common.persistence.model.LeaderImage;
import cn.ibdsr.web.common.persistence.model.LeaderWords;
import cn.ibdsr.web.modular.material.transfer.LeaderWordsDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;

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
public interface ILeaderService {

    /**
     * 查询领导关怀列表
     *
     * @param page      分页参数
     * @param condition 搜索关键字
     * @param isPublish 发布状态
     * @return
     */
    List<Map<String,Object>> list(Page<Map<String,Object>> page, String condition, Integer isPublish);

    /**
     *  新增领导关怀
     *
     * @param param     领导关怀数据
     */
    LeaderWords addLeaderWords(LeaderWordsDTO param);

    /**
     * 校验id参数
     *
     * @param leaderId  领导关怀id
     * @return
     */
    LeaderWords checkId(Long leaderId);

    /**
     * 获取图片列表
     *
     * @param leaderId  领导关怀id
     * @return
     */
    List<LeaderImage> getImageListByLeaderId(Long leaderId);

    /**
     *  新增领导关怀
     *
     * @param param     领导关怀数据
     */
    void updateLeaderWords(LeaderWordsDTO param);

    /**
     * 逻辑删除领导关怀信息
     *
     * @param leaderId
     */
    void logicDelete(Long leaderId);

    /**
     * 编辑领导关怀发布状态
     *
     * @param leaderId
     * @param isPublish
     */
    void editPublish(Long leaderId, int isPublish);

    /**
     * 前端展示领导关怀列表
     *
     * @return
     */
    List<JSONObject> indexGetList();

    /**
     * 前端展示领导关怀详情
     *
     * @return
     */
    JSONObject indexLeaderDetail(Long leaderId);
}
