package cn.ibdsr.web.modular.material.transfer;

import cn.ibdsr.core.base.dto.BaseDTO;
import cn.ibdsr.core.check.Verfication;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description 领导关怀DTO
 * @Version V1.0
 * @CreateDate 2019/5/13 13:36
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/5/13      lvyou                领导关怀DTO
 */
public class LeaderWordsDTO extends BaseDTO {
    /**
     * 主键id
     */
    private Long id;
    /**
     * 姓名
     */
    @Verfication(name = "领导/来访者姓名", notNull = true, minlength = 1, maxlength = 40)
    private String leaderName;
    /**
     * 内容
     */
    @Verfication(name = "新闻内容", notNull = true, maxlength = 140)
    private String content;
    /**
     * 是否置顶（0:是;1:否;）
     */
    @Verfication(name = "置顶状态", notNull = true)
    private Integer isTop;
    /**
     * 展示时间
     */
    @Verfication(name = "展示时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date showTime;
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
     * 图片数组
     */
    @Verfication(name = "图片数组", notNull = true)
    private List<String> images;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getIsTop() {
        return isTop;
    }

    public void setIsTop(Integer isTop) {
        this.isTop = isTop;
    }

    public Date getShowTime() {
        return showTime;
    }

    public void setShowTime(Date showTime) {
        this.showTime = showTime;
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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
