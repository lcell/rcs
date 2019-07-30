package com.liguang.rcs.admin.common.template;

import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 模版
 * 组成部分为
 * 1. 定位数据属于哪一行，返回key
 * 2. 数据转换，将输入数据转换成需要的数据结构
 */
public class Template<D, T> {
    private final KeyStrategy<D> keyStrategy;
    private final Map<String , T> rowMap; //记录每行的数据
    private final ConverterStrategy<D, T> converterStrategy;

    public Template(KeyStrategy<D> keyStrategy, ConverterStrategy<D, T> converterStrategy) {
        this.keyStrategy = keyStrategy;
        this.rowMap = Maps.newHashMap();
        this.converterStrategy = converterStrategy;
    }

    //增加单个数据
    public void addSingleData(D data) {
        //定位出在哪一行
        List<String> rowKeys = keyStrategy.getRowKey(data);
        if (rowKeys.isEmpty()) {
            return;
        }
        //数据转换
        for (String key : rowKeys) {
            T finalData = converterStrategy.convert(key, data, rowMap.get(key));
            rowMap.put(key, finalData);
        }
    }

    //将数据汇总返回
    public List<T> buildDataList(DefaultBuilder<T> defaultBuilder) {
        return keyStrategy.getAllRowKey().stream()
                .map((key) -> rowMap.getOrDefault(key, defaultBuilder.build(key)))
                .collect(Collectors.toList());
    }

    public <T extends  ConverterStrategy> T getConverterStrategy() {
        return (T)converterStrategy;
    }

    public <T extends KeyStrategy> T getKeyStrategy() {
        return (T)keyStrategy;
    }
}
