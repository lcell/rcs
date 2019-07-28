package com.liguang.rcs.admin.common.template;

import javax.annotation.Nullable;

/**
 * 数据转换策略
 */
public interface ConverterStrategy<D, T> {
    /**
     * 将输入的数据转换成目标数据
     * @param key 关键子
     * @param data  输入的数据
     * @param originData 目标数据上次的结果值,可以为空的
     * @return 最终的目标数据
     */
    T convert(String key, D data , @Nullable  T originData);

}
