package cn.ibdsr.web.common.persistence.model;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 领导关怀——图片表
 * </p>
 *
 * @author lvyou
 * @since 2019-08-21
 */
@TableName("leader_image")
public class LeaderImage extends Model<LeaderImage> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
	@TableId(value="id", type= IdType.AUTO)
	private Long id;
    /**
     * 文件路径
     */
	private String path;
    /**
     * 领导关怀表id
     */
	@TableField("leader_words_id")
	private Long leaderWordsId;
    /**
     * 创建时间
     */
	@TableField("created_time")
	private Date createdTime;
    /**
     * 创建人
     */
	@TableField("created_user")
	private Long createdUser;
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

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Long getLeaderWordsId() {
		return leaderWordsId;
	}

	public void setLeaderWordsId(Long leaderWordsId) {
		this.leaderWordsId = leaderWordsId;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Long getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(Long createdUser) {
		this.createdUser = createdUser;
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
		return "LeaderImage{" +
			"id=" + id +
			", path=" + path +
			", leaderWordsId=" + leaderWordsId +
			", createdTime=" + createdTime +
			", createdUser=" + createdUser +
			", isDeleted=" + isDeleted +
			"}";
	}
}
