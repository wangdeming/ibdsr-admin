package cn.ibdsr.web.modular.material.warpper;

import cn.ibdsr.core.base.warpper.BaseControllerWarpper;
import cn.ibdsr.web.common.constant.state.PublishStatus;
import cn.ibdsr.web.common.constant.state.TopStatus;

import java.util.List;
import java.util.Map;

/**
 * 领导关怀的包装类
 *
 * @author lvyou
 * @date 2019年8月28日 下午10:47:03
 */
public class LeaderWarpper extends BaseControllerWarpper {

    public LeaderWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        Integer isPublish = (Integer) map.get("isPublish");
        map.put("publishStatus", PublishStatus.valueOf(isPublish));
        if (isPublish.intValue() == PublishStatus.UNPUBLISHED.getCode()) {
            map.put("deleteBtn", true);
            map.put("editBtn", true);
            map.put("detailBtn", false);
        } else {
            map.put("deleteBtn", false);
            map.put("editBtn", false);
            map.put("detailBtn", true);
        }
        Integer isTop = (Integer) map.get("isTop");
        map.put("topStatus", TopStatus.valueOf(isTop));
    }

}
