package com.liguang.rcs.admin.common.excel.cellhook;

import com.liguang.rcs.admin.common.excel.CellHook;

public abstract class AbstractCellHook implements CellHook {
    private static final ThreadLocal<String> EXT_MSG = new ThreadLocal<>();


    protected String getExtMsg() {
        return EXT_MSG.get();
    }
}
