package com.liguang.rcs.admin.common.copy;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.liguang.rcs.admin.common.copy.exception.CopyPropertiesFailException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Slf4j
public class BeanUtils {

    public static void copyProperties(Object source, Object target) throws CopyPropertiesFailException {
        try {
            doConvert(source.getClass(), target.getClass(), (sourceField, targetField, property)-> {
                Object value = getValue(sourceField, source);
                if (value == null && property.ignoreNull()) {
                    return;
                }
                setValue(targetField, target, property.typeCovertClass().newInstance().convert(value, property.extMsg(), property.extClass()));
            });

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

        try {
            doConvert(source.getClass(), target.getClass(), (sourceField, targetField, property)-> {
                Object value = getValue(targetField, target);
                if (value == null && property.ignoreNull()) {
                    return;
                }
                setValue(sourceField, source, property.typeCovertClass().newInstance().reverseConvert(value, property.extMsg(), property.extClass()));
            });

        } catch (Exception ex) {
            log.error("[Copy_Properties] Inner Err, Exception:", ex);
            throw new CopyPropertiesFailException(ex);
        }
    }

    private static void doConvert(Class<?> sourceClazz, Class<?> targetClass, Executer executer) throws CopyPropertiesFailException {
        EnableCopyProperties copyProperties = sourceClazz.getAnnotation(EnableCopyProperties.class);
        if (copyProperties == null) {
            throw new CopyPropertiesFailException();
        }
        List<Field> fields = getFields(sourceClazz, copyProperties.copyParent());
        try {
            for (Field sourceField : fields) {
                CopyProperty property = sourceField.getAnnotation(CopyProperty.class);
                if (property != null) {
                    String fieldStr = Strings.isNullOrEmpty(property.targetField()) ? sourceField.getName() : property.targetField();
                    Field targetField = getField(fieldStr, targetClass);
                    executer.execute(sourceField, targetField, property);
                }
            }
        } catch (Exception ex) {
            log.error("[Copy_Properties] Inner Err, Exception:", ex);
            throw new CopyPropertiesFailException(ex);
        }

    }

    private static Field getField(String fieldName, Class<?> clazz) throws Exception {
        try {
            return clazz.getDeclaredField(fieldName);
        } catch (Exception ex) {
            if (clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
                return getField(fieldName, clazz.getSuperclass());
            }
            throw ex;
        }
    }


    private static List<Field> getFields(Class<?> clazz, boolean copyParent) {
        List<Field> fieldList = Lists.newArrayList();
        if (copyParent && clazz.getSuperclass() != null && clazz.getSuperclass() != Object.class) {
            fieldList.addAll(getFields(clazz.getSuperclass(), true));
        }
        Field[] fields = clazz.getDeclaredFields();
        if (fields != null) {
            fieldList.addAll(Lists.newArrayList(fields));
        }
        return fieldList;
    }

    public static <T> T invokeStaticMethodWithoutParams(Method method) throws InvocationTargetException, IllegalAccessException {
        boolean access = method.isAccessible();
        try {
            return (T) method.invoke(null);
        } finally {
            method.setAccessible(access);
        }
    }


    private static Object getValue(Field field, Object obj) throws IllegalAccessException {
        boolean isAccess = field.isAccessible();
        try {
            field.setAccessible(true);
            return field.get(obj);
        } finally {
            field.setAccessible(isAccess);
        }
    }


    private static void setValue(Field field, Object obj, Object value) throws IllegalAccessException {
        boolean isAccess = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(obj, value);
        } finally {
            field.setAccessible(isAccess);
        }
    }

    private interface Executer {
        void execute(Field sourceField, Field targetField, CopyProperty property) throws Exception ;
    }
}
