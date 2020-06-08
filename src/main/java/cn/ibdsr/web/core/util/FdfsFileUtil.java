/**
 * Copyright (c) 2015-2020, ShangRao Institute of Big Data co.,LTD and/or its
 * affiliates. All rights reserved.
 */
package cn.ibdsr.web.core.util;

import cn.ibdsr.core.util.SpringContextHolder;
import cn.ibdsr.fastdfs.config.FastdfsProperties;
import org.apache.commons.lang3.StringUtils;

/**
 * @Description 系统图片工具类
 * @Version V1.0
 * @CreateDate 2018/4/26 8:59
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2018/4/26      ZhangLin               类说明
 *
 */
public class FdfsFileUtil {


    /**图片访问的前缀*/
    public static String PREFIX_IMAGE_URL =
            SpringContextHolder.getBean(FastdfsProperties.class).getVisit();

    /**
     * @Description 裁剪 图片的URL
     * 比如将http://172.16.1.200:10080/group1/M00/00/00/rBAByFmDy-WAT7FXAALo7BXRELY084.jpg
     * 截取为 group1/M00/00/00/rBAByFmDy-WAT7FXAALo7BXRELY084.jpg
     * @Date 2017/8/17 15:33
     * @param targetImageURL 目标的图片的URL
     * @throws
     * @return
     */
    public static String cutFileURL(String targetImageURL) {

        if (StringUtils.isEmpty(targetImageURL)) {
            return targetImageURL;
        }
        int imageIndex = targetImageURL.indexOf(PREFIX_IMAGE_URL);
        if (imageIndex < 0) {
            return targetImageURL;
        }
        return targetImageURL.substring(imageIndex + PREFIX_IMAGE_URL.length());
    }

    /**
     * @Description 补全 图片的URL
     * 比如将/group1/M00/00/00/rBAByFmDy-WAT7FXAALo7BXRELY084.jpg
     * 补全为 http://172.16.1.200:10080/group1/M00/00/00/rBAByFmDy-WAT7FXAALo7BXRELY084.jpg
     * @Date 2017/8/17 15:33
     * @param targetImageURL 目标的图片的URL
     * @throws
     * @return
     */
    public static String setFileURL(String targetImageURL) {

        if (StringUtils.isEmpty(targetImageURL) || "null".equals(targetImageURL)) {
            return "";
        }
        int imageIndex = targetImageURL.indexOf(PREFIX_IMAGE_URL);
        if (imageIndex > -1) {
            return targetImageURL;
        }
        return PREFIX_IMAGE_URL + targetImageURL;
    }

}
