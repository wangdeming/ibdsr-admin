package cn.ibdsr.web.modular.hire.transfer;

import cn.ibdsr.core.base.dto.BaseDTO;
import cn.ibdsr.core.check.Verfication;
import cn.ibdsr.web.core.util.RegUtil;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;

/**
 * <p>
 * 简历信息表
 * </p>
 *
 * @author lvyou
 * @since 2019-08-21
 */
public class ResumeDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;
    /**
     * 投递岗位id
     */
    @Verfication(name = "投递岗位id", notNull = true)
	private Long jobId;
    /**
     * 应聘人员名称
     */
	@Verfication(name = "姓名", notNull = true, regx = {RegUtil.CONCAT_NAME, "格式有误"})
	private String name;
    /**
     * 性别(1:男;2:女;)
     */
	@Verfication(name = "姓名", notNull = true, regx = {RegUtil.NUMBER})
	private Integer sex;
    /**
     * 出生年月
     */
	@Verfication(name = "出生年月", notNull = true)
	@DateTimeFormat(pattern = "yyyy-MM")
	private Date birthday;
    /**
     * 联系电话
     */
	@Verfication(name = "联系电话", notNull = true, regx = {RegUtil.MOBILE_PHONE})
	private String phone;
    /**
     * 电子邮箱
     */
	@Verfication(name = "电子邮箱", notNull = true, regx = {RegUtil.EMAIL})
	private String email;
    /**
     * 附件存储路径
     */
	private String resumePath;
	/**
	 * 附件存储路径
	 */
	private String resumeName;
    /**
     * 备注信息
     */
	@Verfication(name = "备注信息", maxlength = 300)
	private String remark;
    /**
     * 是否已读（0:未读;1:已读;）
     */
	private Integer isRead;
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
	/**
	 * 简历
	 */
	@Verfication(name = "简历", notNull = true)
	private MultipartFile resumeFile;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getResumePath() {
		return resumePath;
	}

	public void setResumePath(String resumePath) {
		this.resumePath = resumePath;
	}

	public String getResumeName() {
		return resumeName;
	}

	public void setResumeName(String resumeName) {
		this.resumeName = resumeName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getIsRead() {
		return isRead;
	}

	public void setIsRead(Integer isRead) {
		this.isRead = isRead;
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

	public MultipartFile getResumeFile() {
		return resumeFile;
	}

	public void setResumeFile(MultipartFile resumeFile) {
		this.resumeFile = resumeFile;
	}
}
