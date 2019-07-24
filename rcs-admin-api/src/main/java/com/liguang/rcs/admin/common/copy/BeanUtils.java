package com.liguang.rcs.admin.common.copy;

import com.liguang.rcs.admin.common.copy.exception.CopyPropertiesFailException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
@Slf4j
public class BeanUtils {

    public static void copyProperties(Object source, Object target) throws CopyPropertiesFailException {
        Class<?> clazz = source.getClass();
        EnableCopyProperties copyProperties = clazz.getAnnotation(EnableCopyProperties.class);
        if (copyProperties == null) {
            throw new RuntimeException("Not Support");
        }
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                CopyProperty fieldAnnotation = field.getAnnotation(CopyProperty.class);
                if (fieldAnnotation != null) {
                    Object value = getValue(field, source);
                    if (value != null || !fieldAnnotation.ignoreNull()) {
                        setValue(fieldAnnotation.targetField(), target, value, fieldAnnotation);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("[Copy_Properties] Inner Err, Exception:", ex);
            throw new CopyPropertiesFailException(ex);
        }
    }

    public static Object getValue(Field field, Object obj) throws IllegalAccessException {
        boolean isAccess = field.isAccessible();
        try {
            field.setAccessible(true);
            return field.get(obj);
        } finally {
            field.setAccessible(isAccess);
        }
    }

    public static void setValue(String fieldStr, Object obj, Object value, CopyProperty property)
            throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        TypeConverter convert = property.typeCovertClass().newInstance();
        setValue(fieldStr, obj, convert.convert(value, property.extMsg()));
    }

    public static void setValue(String fieldStr, Object obj, Object value) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldStr);
        setValue(field, obj, value);
    }

    public static void setValue(Field field, Object obj, Object value) throws IllegalAccessException {
        boolean isAccess = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } finally {
            field.setAccessible(isAccess);
        }
    }
}
