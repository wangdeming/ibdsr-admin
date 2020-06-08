package cn.ibdsr.web.common.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import java.io.Serializable;

/**
 * <p>
 * 大事记表
 * </p>
 *
 * @author lvyou
 * @since 2019-08-21
 */
public class Events extends Model<Events> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 标题
     */
	private String title;
    /**
     * 内容
     */
	private String content;
    /**
     * 事记时间-年
     */
	@TableField("event_year")
	private Integer eventYear;
    /**
     * 事记时间-日
     */
	@TableField("event_day")
	private Integer eventDay;
    /**
     * 事记时间-月
     */
	@TableField("event_month")
	private Integer eventMonth;
    /**
     * 创建人
     */
	@TableField("created_user")
	private Long createdUser;
    /**
     * 创建时间
     */
	@TableField("created_time")
	private Date createdTime;
    /**
     * 修改人
     */
	@TableField("modified_user")
	private Long modifiedUser;
    /**
     * 修改时间
     */
	@TableField("modified_time")
	private Date modifiedTime;
    /**
     * 是否删除（0:未删除;1:已删除;）
     */
	@TableField("is_deleted")
	private Integer isDeleted;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getEventYear() {
		return eventYear;
	}

	public void setEventYear(Integer eventYear) {
		this.eventYear = eventYear;
	}

	public Integer getEventDay() {
		return eventDay;
	}

	public void setEventDay(Integer eventDay) {
		this.eventDay = eventDay;
	}

	public Integer getEventMonth() {
		return eventMonth;
	}

	public void setEventMonth(Integer eventMonth) {
		this.eventMonth = eventMonth;
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

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "Events{" +
			"id=" + id +
			", title=" + title +
			", content=" + content +
			", eventYear=" + eventYear +
			", eventDay=" + eventDay +
			", eventMonth=" + eventMonth +
			", createdUser=" + createdUser +
			", createdTime=" + createdTime +
			", modifiedUser=" + modifiedUser +
			", modifiedTime=" + modifiedTime +
			", isDeleted=" + isDeleted +
			"}";
	}
}
