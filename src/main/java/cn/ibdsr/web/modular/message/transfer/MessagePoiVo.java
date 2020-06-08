package cn.ibdsr.web.modular.message.transfer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

/**
 * @Description 留言信息导出vo类
 * @Version V1.0
 * @CreateDate 2019/8/19 16:44
 * <p>
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                留言信息导出vo类
 */
@ExcelTarget("messageVo")
public class MessagePoiVo {
    @Excel(name = "序号")
    private Long num;
    @ExcelIgnore
    private Integer messageSource;
    @Excel(name = "留言来源")
    private String sourceName;
    @Excel(name = "姓名")
    private String name;
    @Excel(name = "公司")
    private String company;
    @Excel(name = "留言内容")
    private String content;
    @Excel(name = "联系电话")
    private String phone;
    @Excel(name = "投递时间")
    private String createdTime;
    @Excel(name = "状态", replace = {"已读_1", "未读_0"})
    private String isRead;

    public Long getNum() {
        return num;
    }

    public void setNum(Long num) {
        this.num = num;
    }

    public Integer getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(Integer messageSource) {
        this.messageSource = messageSource;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
