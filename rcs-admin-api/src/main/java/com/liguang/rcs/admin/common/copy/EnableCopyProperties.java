package com.liguang.rcs.admin.common.copy;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableCopyProperties {
    //是否拷贝父类的字段值
    boolean copyParent() default false;
}
