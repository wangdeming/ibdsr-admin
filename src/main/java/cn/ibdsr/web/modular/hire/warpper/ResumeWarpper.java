package cn.ibdsr.web.modular.hire.warpper;

import cn.ibdsr.core.base.warpper.BaseControllerWarpper;
import cn.ibdsr.web.common.constant.factory.ConstantFactory;
import cn.ibdsr.web.common.constant.state.ReadStatus;
import cn.ibdsr.web.core.util.FdfsFileUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 简历的包装类
 *
 * @author lvyou
 * @date 2019年8月27日 下午10:47:03
 */
public class ResumeWarpper extends BaseControllerWarpper {

    public ResumeWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    protected void warpTheMap(Map<String, Object> map) {
        map.put("readStatus", ReadStatus.valueOf((Integer) map.get("isRead")));
        map.put("sexName", ConstantFactory.me().getSexName((Integer) map.get("sex")));
        String resumePath = (String) map.get("resumePath");
        if (StringUtils.isNotEmpty(resumePath)) {
            map.put("resumePath", FdfsFileUtil.setFileURL(resumePath));
        }
    }
}
