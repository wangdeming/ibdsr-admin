package cn.ibdsr.web.modular.material.transfer;

import cn.ibdsr.core.base.dto.BaseDTO;
import cn.ibdsr.core.check.Verfication;
import cn.ibdsr.web.core.util.RegUtil;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 大事记表
 * </p>
 *
 * @author lvyou
 * @since 2019-08-21
 */
public class EventsDTO extends BaseDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	private Long id;
    /**
     * 标题
     */
    @Verfication(name = "标题", notNull = true, maxlength = 40)
	private String title;
    /**
     * 内容
     */
	@Verfication(name = "内容", maxlength = 140)
	private String content;
    /**
     * 事记时间-年
     */
	@Verfication(name = "事记时间年份", notNull = true, regx = {RegUtil.NUMBER})
	private Integer eventYear;
    /**
     * 事记时间-日
     */
	@Verfication(name = "事记时间年份", regx = {RegUtil.NUMBER})
	private Integer eventDay;
    /**
     * 事记时间-月
     */
	@Verfication(name = "事记时间月份", notNull = true, regx = {RegUtil.NUMBER})
	private Integer eventMonth;
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
	 * 图片数组
	 */
	private List<String> images;


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

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
}
