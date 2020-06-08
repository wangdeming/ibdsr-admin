package cn.ibdsr.web.modular.material.warpper;

import cn.ibdsr.core.base.warpper.BaseControllerWarpper;
import cn.ibdsr.web.common.constant.state.PublishStatus;
import cn.ibdsr.web.common.constant.state.TopStatus;
import cn.ibdsr.web.core.util.FdfsFileUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 领导关怀的包装类
 *
 * @author lvyou
 * @date 2019年8月28日 下午10:47:03
 */
public class BannerWarpper extends BaseControllerWarpper {

    public BannerWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        String path = (String) map.get("path");
        map.put("path", FdfsFileUtil.setFileURL(path));
    }

}
