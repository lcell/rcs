package com.liguang.rcs.admin.common.copy;
/*
  类型转换接口
  只能存在一个无参构造函数
 */
public interface TypeConverter<S, T> {
    T convert(S source, String extMsg);
}
