package cn.ibdsr.web.modular.hire.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.ibdsr.core.base.controller.BaseController;
import cn.ibdsr.core.base.tips.SuccessDataTip;
import cn.ibdsr.core.util.ToolUtil;
import cn.ibdsr.web.common.constant.factory.PageFactory;
import cn.ibdsr.web.common.constant.state.DeleteStatus;
import cn.ibdsr.web.common.exception.BizExceptionEnum;
import cn.ibdsr.web.common.exception.BussinessException;
import cn.ibdsr.web.common.persistence.model.Resume;
import cn.ibdsr.web.core.util.ExcelPoiUtil;
import cn.ibdsr.web.core.util.FdfsFileUtil;
import cn.ibdsr.web.modular.hire.service.IResumeService;
import cn.ibdsr.web.modular.hire.transfer.ResumePoiVo;
import cn.ibdsr.web.modular.hire.warpper.ResumeWarpper;
import com.baomidou.mybatisplus.plugins.Page;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @Description 简历管理控制器
 * @Version V1.0
 * @CreateDate 2019/8/19 16:44
 * <p>
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/8/19      lvyou                简历管理控制器
 */
@Controller
@RequestMapping("/resume")
public class ResumeController extends BaseController {

    private String PREFIX = "/hire/resume/";

    @Resource
    private IResumeService resumeService;

    /**
     * 跳转到简历管理首页
     */
    @RequestMapping("")
    public String index() {
        return PREFIX + "resume.html";
    }

    /**
     * 获取简历管理列表
     */
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(name = "condition", required = false) String condition,
                       @RequestParam(name = "isRead", required = false) Integer isRead,
                       @RequestParam(name = "jobId", required = false) Long jobId) {
        Page<Map<String, Object>> page = new PageFactory<Map<String, Object>>().defaultPage();
        List<Map<String, Object>> records = resumeService.list(page, condition, isRead, jobId);
        page.setRecords((List<Map<String, Object>>) new ResumeWarpper(records).warp());
        return super.packForBT(page);
    }

    /**
     * 获取有简历投递过的岗位列表
     */
    @RequestMapping(value = "/job/list")
    @ResponseBody
    public Object jobList() {
        List<Map<String, Object>> records = resumeService.jobList();
        return new SuccessDataTip(records);
    }

    /**
     * 查阅简历
     */
    @PostMapping(value = "/read")
    @ResponseBody
    public Object read(@RequestParam(value = "resumeIds") Long... resumeIds) {
        if (resumeIds == null || resumeIds.length <= 0) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        resumeService.readResume(resumeIds);
        return SUCCESS_TIP;
    }

    /**
     * 导出简历压缩包
     */
    @GetMapping(value = "/download")
    public void download(@RequestParam("resumeId") Long resumeId, HttpServletResponse response) {
        Resume resume = new Resume();
        resume.setId(resumeId);
        resume.setIsDeleted(DeleteStatus.UN_DELETED.getCode());
        resume = resume.selectById();
        if (ToolUtil.isEmpty(resume) || StringUtils.isEmpty(resume.getResumePath())) {
            return;
        }
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/x-download");
            String fileName = URLEncoder.encode(resume.getResumeName(), "utf-8");

            fileName = fileName.replace("+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            String resumePath = FdfsFileUtil.setFileURL(resume.getResumePath());
            inputStream = new URL(resumePath).openStream();
            outputStream = response.getOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            //读入需要下载的文件的内容，打包到zip文件
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            throw new BussinessException(BizExceptionEnum.DOWNLOAD_ERROR);
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                throw new BussinessException(BizExceptionEnum.DOWNLOAD_ERROR);
            }
        }
    }

    /**
     * 导出简历压缩包
     */
    @GetMapping(value = "/export")
    public void export(HttpServletResponse response, HttpServletRequest request, @RequestParam(value = "resumeIds") Long... resumeIds) {
        if (resumeIds == null || resumeIds.length <= 0) {
            throw new BussinessException(BizExceptionEnum.REQUEST_INVALIDATE);
        }
        ZipOutputStream zipStream = null;
        FileOutputStream outStream = null;
        InputStream inputStream = null;
        File excelFile = null;
        try {
            response.setCharacterEncoding("UTF-8");
            response.setHeader("content-Type", "application/x-download");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("简历文件.zip", "UTF-8"));
            //构建excel导出数据 start
            List<ResumePoiVo> records = resumeService.getExportList(resumeIds);
            List<Map<String, String>> fileList = new ArrayList<>();
            for (int i = 0; i < records.size(); i++) {
                ResumePoiVo temp = records.get(i);
                String filePath = temp.getResumePath();
                if (StringUtils.isNotEmpty(filePath)) {
                    Map<String, String> file = new HashMap<>();
                    filePath = FdfsFileUtil.setFileURL(filePath);
                    temp.setResumePath(filePath);
                    file.put("filePath", filePath);
                    file.put("fileName", (i + 1) + "-" + temp.getResumeName());
                    fileList.add(file);
                }
            }
            //构建excel导出数据 end

            //暂存excel文件 start
            String path = request.getSession().getServletContext().getRealPath("/static/excel/");
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("简历信息表", "简历列表", ExcelType.XSSF), ResumePoiVo.class, records);
            String excelName = "简历信息表.xlsx";
            excelFile = new File(path + excelName);
            outStream = new FileOutputStream(excelFile);
            workbook.write(outStream);
            Map<String, String> excel = new HashMap<>();
            excel.put("filePath", path + excelName);
            excel.put("fileName", excelName);
            fileList.add(excel);
            //暂存excel文件 end

            //构建zip压缩包 start
            byte[] buffer = new byte[1024];
            zipStream = new ZipOutputStream(response.getOutputStream());
            for (Map<String, String> file : fileList) {
                String filePath = file.get("filePath");
                if (filePath.indexOf("http://") != -1) {
                    inputStream = new URL(file.get("filePath")).openStream();
                } else {
                    inputStream = new FileInputStream(new File(filePath));
                }
                zipStream.putNextEntry(new ZipEntry(file.get("fileName")));
                int len;
                //读入需要下载的文件的内容，打包到zip文件
                while ((len = inputStream.read(buffer)) != -1) {
                    zipStream.write(buffer, 0, len);
                }
            }
            //构建zip压缩包 end
        } catch (Exception e) {
            throw new BussinessException(BizExceptionEnum.EXPORT_ERROR);
        } finally {
            try {
                if (zipStream != null) {
                    zipStream.closeEntry();
                    zipStream.close();
                }
                if (outStream != null) {
                    outStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (excelFile != null && excelFile.exists()) {
                    excelFile.delete();
                }
            } catch (IOException e) {
                throw new BussinessException(BizExceptionEnum.EXPORT_ERROR);
            }
        }
    }
}
