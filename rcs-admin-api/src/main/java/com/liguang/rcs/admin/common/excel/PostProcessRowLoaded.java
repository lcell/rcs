package com.liguang.rcs.admin.common.excel;

import org.apache.poi.ss.usermodel.Row;

/**
 * excel 行加载完成之后调用的钩子
 * @param <T> 行的数据
 */
public interface PostProcessRowLoaded<T> {

    void doProcess(Row row, T value, String extMsg);
}
