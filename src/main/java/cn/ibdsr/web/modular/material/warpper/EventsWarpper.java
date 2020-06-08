package cn.ibdsr.web.modular.material.warpper;

import cn.ibdsr.core.base.warpper.BaseControllerWarpper;
import cn.ibdsr.core.util.DateUtil;
import cn.ibdsr.web.common.constant.Const;
import cn.ibdsr.web.common.constant.state.PublishStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 大事记的包装类
 *
 * @author lvyou
 * @date 2019年8月28日 下午10:47:03
 */
public class EventsWarpper extends BaseControllerWarpper {

    public EventsWarpper(List<Map<String, Object>> list) {
        super(list);
    }

    @Override
    public void warpTheMap(Map<String, Object> map) {
        StringBuilder eventDate = new StringBuilder();
        Integer eventYear = (Integer) map.get("eventYear");
        Integer eventMonth = (Integer) map.get("eventMonth");
        Integer eventDay = (Integer) map.get("eventDay");
        if (eventMonth != null) {
            eventDate.append(eventYear).append("年");
        }
        if (eventMonth != null) {
            eventDate.append(eventMonth).append("月");
        }
        if (eventDay != null) {
            eventDate.append(eventDay).append("日");
        }
        map.put("eventDate", eventDate.toString());
    }

}
