package cn.ibdsr.web.modular.message.warpper;

import cn.ibdsr.core.base.warpper.BaseControllerWarpper;
import cn.ibdsr.web.common.constant.factory.ConstantFactory;
import cn.ibdsr.web.common.constant.state.ReadStatus;

import java.util.List;
import java.util.Map;

/**
 * 留言信息的包装类
 *
 * @author lvyou
 * @date 2019年8月28日 下午10:47:03
 */
public class MessageWarpper extends BaseControllerWarpper {

    public MessageWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        map.put("readStatus", ReadStatus.valueOf((Integer) map.get("isRead")));
        map.put("sourceName", ConstantFactory.me().getDictsByName("留言来源", (Integer) map.get("messageSource")));
    }
}
