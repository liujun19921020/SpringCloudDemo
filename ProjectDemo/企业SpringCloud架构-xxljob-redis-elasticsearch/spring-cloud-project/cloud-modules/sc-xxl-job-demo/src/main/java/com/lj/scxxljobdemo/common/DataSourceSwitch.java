package com.lj.scxxljobdemo.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *  数据源切换
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DataSourceSwitch {
    //顺便设置下默认数据源
    DBTypeEnum value() default DBTypeEnum.cs1db;
}
