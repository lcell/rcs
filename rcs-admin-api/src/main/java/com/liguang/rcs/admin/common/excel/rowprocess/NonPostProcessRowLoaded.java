package com.liguang.rcs.admin.common.excel.rowprocess;

import com.liguang.rcs.admin.common.excel.PostProcessRowLoaded;

public class NonPostProcessRowLoaded implements PostProcessRowLoaded {
    @Override
    public void doProcess(org.apache.poi.ss.usermodel.Row row, Object value, String extMsg) {
        //do nothing
    }
}
