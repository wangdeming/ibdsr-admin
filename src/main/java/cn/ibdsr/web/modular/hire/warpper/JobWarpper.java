package cn.ibdsr.web.modular.hire.warpper;

import cn.ibdsr.core.base.warpper.BaseControllerWarpper;
import cn.ibdsr.web.common.constant.factory.ConstantFactory;
import cn.ibdsr.web.common.constant.state.PublishStatus;
import cn.ibdsr.web.core.shiro.ShiroKit;

import java.util.List;
import java.util.Map;

/**
 * 岗位信息的包装类
 *
 * @author lvyou
 * @date 2019年8月27日 下午10:47:03
 */
public class JobWarpper extends BaseControllerWarpper {

    public JobWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        Integer isPublish = (Integer) map.get("isPublish");
        Long resumeCount = (Long) map.get("resumeCount");
        map.put("publishStatus", PublishStatus.valueOf(isPublish));
        if (isPublish.intValue() == PublishStatus.UNPUBLISHED.getCode()) {
            if (resumeCount <= 0) {
                map.put("deleteBtn", true);
            } else {
                map.put("deleteBtn", false);
            }
            map.put("editBtn", true);
            map.put("detailBtn", false);
        } else {
            map.put("deleteBtn", false);
            map.put("editBtn", false);
            map.put("detailBtn", true);
        }
    }

}
