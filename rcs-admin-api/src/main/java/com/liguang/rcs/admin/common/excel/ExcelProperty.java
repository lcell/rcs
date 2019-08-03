package com.liguang.rcs.admin.common.excel;


import com.liguang.rcs.admin.common.excel.cellhook.NonCellHook;

import javax.swing.text.Style;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelProperty {

    String column(); //对应字段名称

    Class<? extends CellHook> cellHook() default NonCellHook.class ; //cell单元处理钩子

    String extMsg() default ""; //额外信息，用于提供给hook使用的

}
