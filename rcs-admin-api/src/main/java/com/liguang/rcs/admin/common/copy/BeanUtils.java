package com.liguang.rcs.admin.common.copy;

import com.liguang.rcs.admin.common.copy.exception.CopyPropertiesFailException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    /**
     * 反向拷贝，将target里面的数据值覆盖到source里面
     * @param source
     * @param target
     * @throws CopyPropertiesFailException
     */
    public static void reverseCopyProperties(Object source, Object target) throws CopyPropertiesFailException {
        Class<?> clazz = source.getClass();
        EnableCopyProperties copyProperties = clazz.getAnnotation(EnableCopyProperties.class);
        if (copyProperties == null) {
            throw new CopyPropertiesFailException();
        }
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                CopyProperty fieldAnnotation = field.getAnnotation(CopyProperty.class);
                if (fieldAnnotation != null) {
                    Object value = getTargetValue(fieldAnnotation.targetField(), target, fieldAnnotation);
                    if (value != null || !fieldAnnotation.ignoreNull()) {
                        setValue(field, source, value);
                    }
                }
            }
        } catch (Exception ex) {
            log.error("[Copy_Properties] Inner Err, Exception:", ex);
            throw new CopyPropertiesFailException(ex);
        }
    }

    public static <T> T invokeStaticMethodWithoutParams(Method method) throws InvocationTargetException, IllegalAccessException {
        boolean access = method.isAccessible();
        try {
            return (T) method.invoke(null);
        } finally {
            method.setAccessible(access);
        }
    }

    public static Object getTargetValue(String fieldName, Object obj, CopyProperty property) throws Exception {
        Field field = obj.getClass().getDeclaredField(fieldName);
        TypeConverter converter = property.typeCovertClass().newInstance();
        return converter.reverseConvert(getValue(field, obj), property.extMsg(), property.extClass());
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
        setValue(fieldStr, obj, convert.convert(value, property.extMsg(), property.extClass()));
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
