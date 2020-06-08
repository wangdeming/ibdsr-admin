package cn.ibdsr.web.modular.hire.transfer;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelIgnore;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;

import java.util.Date;

/**
 * @Description 简历信息导出vo类
 * @Version V1.0
 * @CreateDate 2019/8/19 16:44
 * <p>
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                简历信息导出vo类
 */
@ExcelTarget(value = "resumeVo")
public class ResumePoiVo {

    @Excel(name = "序号")
    private Long num;
    @Excel(name = "岗位名称")
    private String jobName;
    @Excel(name = "投递人")
    private String name;
    @Excel(name = "性别", replace = {"男_1", "女_2"})
    private Integer sex;
    @Excel(name = "出生年月")
    private String birthday;
    @Excel(name = "联系方式")
    private String phone;
    @Excel(name = "邮箱")
    private String email;
    @ExcelIgnore
    private String resumePath;
    @Excel(name = "简历附件")
    private String resumeName;
    @Excel(name = "备注")
    private String remark;
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

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
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
