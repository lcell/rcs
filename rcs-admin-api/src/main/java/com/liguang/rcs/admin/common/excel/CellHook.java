package com.liguang.rcs.admin.common.excel;

import org.apache.poi.ss.usermodel.Cell;

public interface CellHook {

    void handleCell(Cell cell, Object value, String extMsg);
}
