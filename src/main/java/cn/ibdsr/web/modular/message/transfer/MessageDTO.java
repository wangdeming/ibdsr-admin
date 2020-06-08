package cn.ibdsr.web.modular.message.transfer;

import cn.ibdsr.core.base.dto.BaseDTO;
import cn.ibdsr.core.check.Verfication;
import cn.ibdsr.web.core.util.RegUtil;

import java.util.Date;

/**
 * @Description 留言DTO
 * @Version V1.0
 * @CreateDate 2019/5/13 13:36
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/5/13      lvyou                留言DTO
 */
public class MessageDTO extends BaseDTO {

    /**
     * 主键id
     */
    private Long id;
    /**
     * 留言来源
     */
    @Verfication(name = "留言来源", notNull = true)
    private Integer messageSource;
    /**
     * 留言人姓名
     */
    @Verfication(name = "留言人姓名", maxlength = 4, minlength = 2, notNull = true, regx = {RegUtil.CONCAT_NAME, "格式有误"})
    private String name;
    /**
     * 公司名称
     */
    @Verfication(name = "公司名称", notNull = true, regx = {RegUtil.COMPANY_NAME, "格式有误"})
    private String company;
    /**
     * 留言内容
     */
    @Verfication(name = "留言内容", maxlength = 300, notNull = true)
    private String content;
    /**
     * 是否已读（0:未读;1:已读;）
     */
    private Integer isRead;
    /**
     * 联系电话
     */
    @Verfication(name = "联系电话", notNull = true, regx = {RegUtil.MOBILE_PHONE, "格式有误"})
    private String phone;
    /**
     * 创建人
     */
    private Long createdUser;
    /**
     * 创建时间
     */
    private Date createdTime;
    /**
     * 修改人
     */
    private Long modifiedUser;
    /**
     * 修改时间
     */
    private Date modifiedTime;
    /**
     * 是否删除（0:未删除;1:已删除;）
     */
    private Integer isDeleted;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMessageSource() {
        return messageSource;
    }

    public void setMessageSource(Integer messageSource) {
        this.messageSource = messageSource;
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

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(Long createdUser) {
        this.createdUser = createdUser;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Long getModifiedUser() {
        return modifiedUser;
    }

    public void setModifiedUser(Long modifiedUser) {
        this.modifiedUser = modifiedUser;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
