package cn.ibdsr.web.modular.message.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.core.base.tips.SuccessTip;
import cn.ibdsr.web.common.constant.factory.ConstantFactory;
import cn.ibdsr.web.common.constant.factory.PageFactory;
import cn.ibdsr.web.common.constant.state.ReadStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.Message;
import cn.ibdsr.web.core.util.ExcelPoiUtil;
import cn.ibdsr.web.modular.message.service.IMessageService;
import cn.ibdsr.web.modular.message.transfer.MessagePoiVo;
import cn.ibdsr.web.modular.message.warpper.MessageWarpper;
import cn.ibdsr.web.modular.system.dao.DictDao;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Description 留言管理控制器
 * @Version V1.0
 * @CreateDate 2019/8/19 16:27
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                留言管理控制器
 */
@Controller
@RequestMapping("/message")
public class MessageController extends BaseController {

    private String PREFIX = "/message/message/";

    @Resource
    private IMessageService messageService;
    @Resource
    private DictDao dictDao;

    /**
     * 跳转到留言管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "message.html";
    }

    /**
     * 获取留言管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(value = "isRead", required = false) Integer isRead,
                       @RequestParam(value = "messageSource", required = false) Integer messageSource) {
        if (isRead != null && StringUtils.isEmpty(ReadStatus.valueOf(isRead))) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
        List<Map<String, Object>> records = messageService.list(page, isRead, messageSource);
        page.setRecords((List<Map<String, Object>>) new MessageWarpper(records).warp());
        return super.packForBT(page);
    }

    /**
     * 获取留言管理列表
     */
    @GetMapping(value = "/source/list")
    @ResponseBody
    public Object sourceList() {
        return new SuccessDataTip(dictDao.selectChildByCode("留言来源"));
    }

    /**
     * 标记留言为已读
     */
    @PostMapping(value = "/read")
    @ResponseBody
    public Object read(@RequestParam(value = "messageIds") Long... messageIds) {
        if (messageIds == null || messageIds.length <= 0) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        messageService.readMessage(messageIds);
        return new SuccessTip();
    }

    /**
     * 跳转到留言管理首页
     */
    @GetMapping(value = "/message_detail/{id}")
    public String messageDetail(@PathVariable("id") Long id, Model model) {
        model.addAttribute("id", id);
        return PREFIX + "message_detail.html";
    }

    /**
     * 留言管理详情
     */
    @PostMapping(value = "/detail")
    @ResponseBody
    public Object detail(@RequestParam(value = "id") Long id) {
        Message message = messageService.checkMessageId(id);
        messageService.readMessage(message.getId());
        return new SuccessDataTip(message);
    }

    /**
     * 导出留言详情
     */
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, @RequestParam(value = "messageIds") Long... messageIds) {
        if (messageIds == null || messageIds.length <= 0) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        //构建excel导出数据 start
        List<MessagePoiVo> records = messageService.getExportList(messageIds);
        for (MessagePoiVo vo : records) {
            vo.setSourceName(ConstantFactory.me().getDictsByName("留言来源", vo.getMessageSource()));
        }
        Workbook workbook = ExcelPoiUtil.defaultExport(records, MessagePoiVo.class, new ExportParams("留言信息", "留言表", ExcelType.XSSF));
        ExcelPoiUtil.downLoadExcel("留言信息表.xlsx", response, workbook);
    }
}
