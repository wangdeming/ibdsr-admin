package cn.ibdsr.web.modular.hire.transfer;

import cn.ibdsr.core.base.dto.BaseDTO;
import cn.ibdsr.core.check.Verfication;
import cn.ibdsr.web.common.persistence.model.Job;
import cn.ibdsr.web.core.util.RegUtil;

import java.util.Date;

/**
 * <p>
 * 岗位表DTO
 * </p>
 *
 * @author lvyou
 * @since 2019-08-21
 */
public class JobDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;
    /**
     * 岗位类别id
     */
    @Verfication(name = "岗位分类", notNull = true)
	private Long jobTypeId;
    /**
     * 岗位名称
     */
	@Verfication(name = "岗位名称", notNull = true, maxlength = 40, minlength = 5)
	private String name;
    /**
     * 招聘数量(0:若干;1、2、3、4、5)
     */
	@Verfication(name = "招聘数量", notNull = true, max = 5, min = 0, regx = {RegUtil.NUMBER})
	private Integer num;
    /**
     * 岗位职责
     */
    @Verfication(name = "岗位职责", notNull = true)
	private String jobDuty;
    /**
     * 岗位要求
     */
	@Verfication(name = "岗位要求", notNull = true)
	private String jobRequire;
    /**
     * 排序权重
     */
	@Verfication(name = "排序权重", notNull = true, max = 10, min = 1)
	private Integer sort;
    /**
     * 是否发布（0:未发布;1:已发布;）
     */
	private Integer isPublish;
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


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getJobTypeId() {
		return jobTypeId;
	}

	public void setJobTypeId(Long jobTypeId) {
		this.jobTypeId = jobTypeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getJobDuty() {
		return jobDuty;
	}

	public void setJobDuty(String jobDuty) {
		this.jobDuty = jobDuty;
	}

	public String getJobRequire() {
		return jobRequire;
	}

	public void setJobRequire(String jobRequire) {
		this.jobRequire = jobRequire;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}

	public Integer getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Integer isPublish) {
		this.isPublish = isPublish;
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
