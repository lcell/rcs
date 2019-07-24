package com.liguang.rcs.admin.common.copy;

import com.liguang.rcs.admin.common.copy.converter.NonTypeConverter;
import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CopyProperty {
    //需要拷贝的目的字段
    String targetField();

    //数据类型转换类
    Class<? extends TypeConverter> typeCovertClass() default NonTypeConverter.class;

    //如果为空是否去覆盖，默认不会去将null赋值给目的对象,防止覆盖掉缺省值
    boolean ignoreNull() default true;

    String extMsg() default "";

}
