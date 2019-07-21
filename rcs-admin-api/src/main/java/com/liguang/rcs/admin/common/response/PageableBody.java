package com.liguang.rcs.admin.common.response;

import lombok.Data;

import java.util.List;

/**
 * 分页使用的body报文体
 */
@Data
public class PageableBody<T> {

    private int totalSize;

    private int currentPage;

    private int pageSize;

    private List<T> dataList;
}
