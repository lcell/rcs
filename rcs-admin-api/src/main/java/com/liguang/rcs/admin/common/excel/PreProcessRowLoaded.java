package com.liguang.rcs.admin.common.excel;

import org.apache.poi.ss.usermodel.Row;

/**
 * excel 行加载的前置处理
 * @param <T> 行的数据
 */
public interface PreProcessRowLoaded<T> {
    void doProcess(Row row, T value, String extMsg);
}
