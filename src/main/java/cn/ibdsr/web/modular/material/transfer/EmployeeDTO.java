package cn.ibdsr.web.modular.material.transfer;

import cn.ibdsr.core.base.dto.BaseDTO;
import cn.ibdsr.core.check.Verfication;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description 员工风采DTO
 * @Version V1.0
 * @CreateDate 2019/5/13 13:36
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/5/13      lvyou                员工风采DTO
 */
public class EmployeeDTO extends BaseDTO {

    /**
     * 主键id
     */
	private Long id;
    /**
     * 标题
     */
    @Verfication(name = "标题", notNull = true, minlength = 2, maxlength = 80)
	private String title;
	/**
	 * 正文内容
	 */
	@Verfication(name = "正文内容", notNull = true)
	private String mainContent;
    /**
     * 展示时间
     */
	@Verfication(name = "展示时间")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date showTime;
    /**
     * 封面图片
     */
	@Verfication(name = "封面图片", notNull = true)
	private String coverImage;
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
     * 是否发布（0:未发布;1:已发布;）
     */
	private Integer isPublish;
    /**
     * 是否删除（0:未删除;1:已删除;）
     */
	private Integer isDeleted;
	/**
	 * 旧封面图片
	 */
	private String oldCoverImage;


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

	public String getMainContent() {
		return mainContent;
	}

	public void setMainContent(String mainContent) {
		this.mainContent = mainContent;
	}

	public Date getShowTime() {
		return showTime;
	}

	public void setShowTime(Date showTime) {
		this.showTime = showTime;
	}

	public String getCoverImage() {
		return coverImage;
	}

	public void setCoverImage(String coverImage) {
		this.coverImage = coverImage;
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

	public Integer getIsPublish() {
		return isPublish;
	}

	public void setIsPublish(Integer isPublish) {
		this.isPublish = isPublish;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getOldCoverImage() {
		return oldCoverImage;
	}

	public void setOldCoverImage(String oldCoverImage) {
		this.oldCoverImage = oldCoverImage;
	}
}
