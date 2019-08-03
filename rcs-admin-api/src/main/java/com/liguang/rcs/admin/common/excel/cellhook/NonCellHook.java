package com.liguang.rcs.admin.common.excel.cellhook;

import com.liguang.rcs.admin.common.excel.CellHook;
import org.apache.poi.ss.usermodel.Cell;

public class NonCellHook implements CellHook {
    @Override
    public void handleCell(Cell cell, Object value, String extMsg) {

    }
}
