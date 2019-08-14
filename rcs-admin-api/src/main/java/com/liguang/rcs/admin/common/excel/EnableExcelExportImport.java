package com.liguang.rcs.admin.common.excel;


import com.liguang.rcs.admin.common.excel.rowprocess.NonPostProcessRowLoaded;
import com.liguang.rcs.admin.common.excel.rowprocess.NonPreProcessRowLoaded;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//使能excel的导入导出功能
public @interface EnableExcelExportImport {

    //设置处理行信息的前置钩子，
    Class<? extends PreProcessRowLoaded> preProcess() default NonPreProcessRowLoaded.class ;

    //设置处理行信息的后置钩子， 用于设置row的样式使用
    Class<? extends PostProcessRowLoaded> postProcess() default NonPostProcessRowLoaded.class ;

}
