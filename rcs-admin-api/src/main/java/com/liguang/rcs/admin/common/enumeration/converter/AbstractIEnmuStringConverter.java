package com.liguang.rcs.admin.common.enumeration.converter;

import com.liguang.rcs.admin.common.copy.BeanUtils;
import com.liguang.rcs.admin.common.copy.TypeConverter;
import com.liguang.rcs.admin.common.enumeration.IEnum;
import com.liguang.rcs.admin.util.EnumUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;

@Slf4j
public abstract  class AbstractIEnmuStringConverter<S, T> implements TypeConverter<S, T> {

    protected  <O extends IEnum> O stringToIEnum(String code, Class clazz) {
        try {
            if (clazz != null && IEnum.class.isAssignableFrom(clazz) && Enum.class.isAssignableFrom(clazz)) {
                Method method = clazz.getDeclaredMethod("values");
                IEnum[] values= BeanUtils.invokeStaticMethodWithoutParams(method);
                return (O) EnumUtils.findByCode(values, code);
            }
        } catch (Exception e) {
            log.error("[Copy] Occur Inner Err, Exception:{}", e);
        }
        log.warn("[COPY] convert to IEnum fail, clazz:{}, code:{}", clazz, code);
        return null;
    }

    protected String ienumToString(IEnum iEnum) {
        return iEnum.getCode();
    }
}
