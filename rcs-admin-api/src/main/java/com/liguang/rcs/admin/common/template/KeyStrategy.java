package com.liguang.rcs.admin.common.template;

import java.util.List;

/**
 * 根据数据获取模版行的key值
 */
public interface KeyStrategy<D> {
    /**
     * 根据数据返回对应的key
     */
    List<String> getRowKey(D data);

    /**
     * 返回所有的数据key，按照需要打印的顺序返回
     */
    List<String> getAllRowKey();
}
