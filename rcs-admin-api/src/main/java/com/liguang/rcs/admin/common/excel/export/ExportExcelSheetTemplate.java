package com.liguang.rcs.admin.common.excel.export;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

public class ExportExcelSheetTemplate {
    private final String templatePath;
    private Map<String, Long> cellKeyMapper;
    private long currentRowNum = 0L;
//    pr

    public ExportExcelSheetTemplate(String templatePath) {
        this.templatePath = templatePath;
    }

    void init(long titleRowNum) {
        //初始化头



    }

    private void readExcelTemplate() {
        try {
            FileInputStream fis = new FileInputStream(this.templatePath);
            try {
                HSSFWorkbook workbook = new HSSFWorkbook(fis);


            } finally {
                fis.close();
            }
        } catch (Exception ex) {

        }
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
