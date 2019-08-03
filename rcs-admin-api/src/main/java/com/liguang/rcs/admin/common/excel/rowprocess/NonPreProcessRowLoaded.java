package com.liguang.rcs.admin.common.excel.rowprocess;

import com.liguang.rcs.admin.common.excel.PreProcessRowLoaded;
import org.apache.poi.ss.usermodel.Row;

public class NonPreProcessRowLoaded implements PreProcessRowLoaded {
    @Override
    public void doProcess(Row row, Object value, String extMsg) {
        //do nothing
    }
}
