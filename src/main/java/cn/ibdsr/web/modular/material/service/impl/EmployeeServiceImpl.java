package cn.ibdsr.web.modular.material.service.impl;

import cn.ibdsr.core.util.ToolUtil;
import cn.ibdsr.fastdfs.base.service.FdfsClientService;
import cn.ibdsr.web.common.constant.state.DeleteStatus;
import cn.ibdsr.web.common.constant.state.PublishStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.Employee;
import cn.ibdsr.web.core.shiro.ShiroKit;
import cn.ibdsr.web.core.util.FdfsFileUtil;
import cn.ibdsr.web.modular.material.dao.EmployeeDao;
import cn.ibdsr.web.modular.material.transfer.EmployeeDTO;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import cn.ibdsr.web.modular.material.service.IEmployeeService;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Description 员工数风采管理Service
 * @Version V1.0
 * @CreateDate 2019/8/19 16:38
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                员工风采管理Service
 */
@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Resource
    private EmployeeDao employeeDao;
    @Resource
    private FdfsClientService fdfsClientService;

    /**
     * 获取员工风采列表
     *
     * @param page      分页参数
     * @param condition 搜索关键字
     * @param isPublish 发布状态
     * @return
     */
    @Override
    public List<Map<String, Object>> list(Page<Map<String, Object>> page, String condition, Integer isPublish) {
        page.setOpenSort(false);
        return employeeDao.list(page, condition, isPublish, page.getOrderByField(), page.isAsc());
    }

    /**
     * 添加员工风采信息
     *
     * @param param 参数
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Employee addEmployee(EmployeeDTO param) {
        if (ToolUtil.isEmpty(param.getShowTime())) {
            param.setShowTime(new Date());
        }
        checkAdd(param);
        Employee addObj = new Employee();
        BeanUtils.copyProperties(param, addObj);
        String coverImage = addObj.getCoverImage();
        if (coverImage.indexOf(FdfsFileUtil.PREFIX_IMAGE_URL) != -1) {
            coverImage = FdfsFileUtil.cutFileURL(coverImage);
        }
        Date operateTime = new Date();
        Long operateUser = ShiroKit.getUser().getId();
        addObj.setCoverImage(coverImage);
        addObj.setCreatedTime(operateTime);
        addObj.setCreatedUser(operateUser);
        addObj.setModifiedTime(operateTime);
        addObj.setModifiedUser(operateUser);
        addObj.setIsPublish(PublishStatus.UNPUBLISHED.getCode());
        addObj.setIsPublish(DeleteStatus.UN_DELETED.getCode());
        employeeDao.insert(addObj);
        return addObj;
    }

    /**
     * 修改员工风采信息
     *
     * @param param 参数
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateEmployee(EmployeeDTO param) {
        if (ToolUtil.isEmpty(param.getShowTime())) {
            param.setShowTime(new Date());
        }
        Employee updateObj = checkUpdate(param);
        if (PublishStatus.PUBLISHED.getCode() == updateObj.getIsPublish().intValue()) {
            throw new BussinessException(BizExceptionEnum.EMPLOYEE_INFO_PUBLISH_CANT_OPERATE);
        }
        BeanUtils.copyProperties(param, updateObj);
        String coverImage = updateObj.getCoverImage();
        if (coverImage.indexOf(FdfsFileUtil.PREFIX_IMAGE_URL) != -1) {
            coverImage = FdfsFileUtil.cutFileURL(coverImage);
        }
        Date operateTime = new Date();
        Long operateUser = ShiroKit.getUser().getId();
        updateObj.setCoverImage(coverImage);
        updateObj.setModifiedUser(operateUser);
        updateObj.setModifiedTime(operateTime);
        employeeDao.updateById(updateObj);

        //删除旧的员工风采封面
        deleteImageFile(param.getOldCoverImage());
    }

    /**
     * 编辑员工风采发布状态
     *
     * @param employeeId
     * @param isPublish
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void editPublish(Long employeeId, int isPublish) {
        Employee emp = checkId(employeeId);
        emp.setIsPublish(isPublish);
        emp.setModifiedUser(ShiroKit.getUser().getId());
        emp.setModifiedTime(new Date());
        employeeDao.updateById(emp);
    }

    /**
     * 逻辑删除员工风采信息
     *
     * @param employeeId 员工风采id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void logicDelete(Long employeeId) {
        Employee emp = checkId(employeeId);
        emp.setModifiedTime(new Date());
        emp.setModifiedUser(ShiroKit.getUser().getId());
        emp.setIsDeleted(DeleteStatus.DELETED.getCode());
        employeeDao.updateById(emp);

        //删除员工风采封面
        deleteImageFile(emp.getCoverImage());
    }

    /**
     * 前端首页员工风采列表
     *
     * @return
     */
    @Override
    public List<JSONObject> indexGetList() {
        return employeeDao.indexGetList();
    }

    /**
     * 前端首页员工风采详情
     *
     * @param employeeId 员工风采id
     * @return
     */
    @Override
    public JSONObject indexEmployeeDetail(Long employeeId) {
        return employeeDao.indexEmployeeDetail(employeeId);
    }

    /**
     * 校验id参数
     *
     * @param id 员工风采id
     * @return
     */
    @Override
    public Employee checkId(Long id) {
        if (ToolUtil.isEmpty(id)) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        Employee emp = new Employee();
        emp.setId(id);
        emp.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        emp = employeeDao.selectOne(emp);
        if (emp == null) {
            throw new BussinessException(BizExceptionEnum.EMPLOYEE_INFO_NOT_EXIST);
        }
        return emp;
    }

    /**
     * 校验新增参数
     *
     * @param param
     */
    private void checkAdd(EmployeeDTO param) {
        checkParam(param);
    }

    /**
     * 校验修改参数
     *
     * @param param
     */
    private Employee checkUpdate(EmployeeDTO param) {
        checkParam(param);
        return this.checkId(param.getId());
    }

    /**
     * 校验参数
     *
     * @param param
     */
    private void checkParam(EmployeeDTO param) {
        Date now = new Date();
        if (now.before(param.getShowTime())) {
            throw new BussinessException(BizExceptionEnum.EMPLOYEE_INFO_SHOW_TIME_ERROR);
        }
    }

    /**
     * 删除领导关怀图片文件
     *
     * @param imagePath
     */
    private void deleteImageFile(String imagePath) {
        try {
            if (StringUtils.isNotEmpty(imagePath)) {
                if (imagePath.indexOf(FdfsFileUtil.PREFIX_IMAGE_URL) != -1) {
                    imagePath = FdfsFileUtil.cutFileURL(imagePath);
                }
                fdfsClientService.deleteFile(imagePath);
            }
        } catch (Exception e) {

        }
    }
}
