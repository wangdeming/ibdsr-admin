package cn.ibdsr.web.modular.api;

import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.ErrorTip;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.core.util.ToolUtil;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.core.util.FdfsFileUtil;
import cn.ibdsr.web.modular.material.service.ILeaderService;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description 前台领导关怀控制器
 * @Version V1.0
 * @CreateDate 2019/9/5 9:32
 * <p>
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/29      lvyou                前台领导关怀控制器
 */
@RestController
@RequestMapping(value = "/api/leader")
public class ApiLeaderController extends BaseController {

    @Resource
    private ILeaderService leaderService;

    /**
     * 前端首页领导关怀列表
     *
     * @return
     */
    @GetMapping(value = "/list")
    public Object leaderWordsList() {
        List<JSONObject> records = leaderService.indexGetList();
        for (JSONObject leader : records) {
            JSONArray imageList = leader.getJSONArray("imageList");
            for (int i = 0; i < imageList.size(); i++) {
                JSONObject image = imageList.getJSONObject(i);
                String imagePath = image.getString("imagePath");
                image.put("imagePath", FdfsFileUtil.setFileURL(imagePath));
            }
        }
        return new SuccessDataTip(records);
    }

    /**
     * 前端首页领导关怀详情
     *
     * @return
     */
    @PostMapping(value = "/detail")
    public Object leaderWordsDetail(@RequestParam("leaderId") Long leaderId) {
        if (ToolUtil.isEmpty(leaderId)) {
            return new ErrorTip(BizExceptionEnum.EMPLOYEE_INFO_ID_IS_NULL.getCode(), BizExceptionEnum.EMPLOYEE_INFO_ID_IS_NULL.getMessage());
        }
        JSONObject leaderDetail = leaderService.indexLeaderDetail(leaderId);
        JSONArray imageList = leaderDetail.getJSONArray("imageList");
        for (int i = 0; i < imageList.size(); i++) {
            JSONObject image = imageList.getJSONObject(i);
            String imagePath = image.getString("imagePath");
            image.put("imagePath", FdfsFileUtil.setFileURL(imagePath));
        }
        if (leaderDetail == null) {
            return new ErrorTip(BizExceptionEnum.LEADERS_INFO_NOT_EXIST.getCode(), BizExceptionEnum.LEADERS_INFO_NOT_EXIST.getMessage());
        }
        return new SuccessDataTip(leaderDetail);
    }
}
