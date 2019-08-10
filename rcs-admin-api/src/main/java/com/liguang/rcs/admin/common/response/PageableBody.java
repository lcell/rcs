package com.liguang.rcs.admin.common.response;

import com.liguang.rcs.admin.web.contract.ContractVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 分页使用的body报文体
 */
@Data
@ApiModel("分页body报文")
public class PageableBody<T> {
    @ApiModelProperty(value = "所有数据条目", dataType = "int")
    private long totalSize;

    @ApiModelProperty(value = "当前页面", dataType = "int")
    private int currentPage;

    @ApiModelProperty(value = "每页数据条数", dataType = "int")
    private int pageSize;

    @ApiModelProperty(value = "数据列表", dataType = "List<T>")
    private List<T> dataList;


    public static <T, R> PageableBody<R> buildFrom(Page<T> dataPage, Function<T, R> mapper) {
        PageableBody<R> pageBody = new PageableBody<>();
        pageBody.setCurrentPage(dataPage.getNumber() + 1);
        pageBody.setPageSize(dataPage.getNumberOfElements());
        pageBody.setDataList(dataPage.getContent().stream().map(mapper).collect(Collectors.toList()));
        pageBody.setTotalSize(dataPage.getTotalElements());
        return pageBody;
    }
    public static <T> PageableBody<T> buildFrom(Page<T> dataPage) {
        PageableBody<T> pageBody = new PageableBody<>();
        pageBody.setCurrentPage(dataPage.getNumber() + 1);
        pageBody.setPageSize(dataPage.getNumberOfElements());
        pageBody.setDataList(dataPage.getContent());
        pageBody.setTotalSize(dataPage.getTotalElements());
        return pageBody;
    }
}
