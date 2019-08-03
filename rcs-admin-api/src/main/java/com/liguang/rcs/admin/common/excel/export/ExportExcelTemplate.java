package com.liguang.rcs.admin.common.excel.export;

import org.springframework.http.ResponseEntity;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class ExportExcelTemplate {
    private final String templatePath;
    private Map<String, Long> cellKeyMapper;

    public ExportExcelTemplate(String templatePath) {
        this.templatePath = templatePath;
    }

    void init(long titleRowNum) {
        //初始化头



    }

    /**
     * 添加一行空白
     */
    void addEmptyRow() {

    }

    //填充模版
    public <T> void fillTemplate(T value) {

    }

    public InputStream export() {

        return null;
    }


}
