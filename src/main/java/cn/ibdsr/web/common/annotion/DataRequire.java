/**
 * Copyright (c) 2015-2020, ShangRao Institute of Big Data co.,LTD and/or its
 * affiliates. All rights reserved.
 */
package cn.ibdsr.web.common.annotion;

import java.lang.annotation.*;

/**
 * @Description 修饰字段必填 注解
 * @Version V1.0
 * @CreateDate 2019/7/5 15:42
 *
 * Date           Author               Description
 * ------------------------------------------------------
 * 2019/7/5      ZhangLin              修饰字段必填 注解
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface DataRequire {
    String name() default "";
}
