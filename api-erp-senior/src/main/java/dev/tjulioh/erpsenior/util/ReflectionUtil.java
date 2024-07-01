package dev.tjulioh.erpsenior.util;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class ReflectionUtil {

    private static final String ERROR_INVALID_FIELD = "Field does not exist %s";
    private static final String ERROR_INVALID_TYPE = "Invalid type of a list %s";

    public static Class<?> getFieldType(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            return field.getType();
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(ERROR_INVALID_FIELD.formatted(fieldName));
        }
    }

    public static Class<?> getListType(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            Type genericType = field.getGenericType();

            if (genericType instanceof ParameterizedType parameterizedType) {
                List<Type> actualTypeArguments = Arrays.asList(parameterizedType.getActualTypeArguments());
                if (!actualTypeArguments.isEmpty()) {
                    if (actualTypeArguments.getFirst() instanceof Class<?> classType) {
                        return classType;
                    }
                }
            }
            throw new IllegalArgumentException(ERROR_INVALID_TYPE.formatted(fieldName));
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException(ERROR_INVALID_FIELD.formatted(fieldName));
        }
    }

    public static Class<?> getInnerType(Class<?> clazz, List<String> fieldParts) {
        Class<?> innerClass = clazz;
        for (String fieldPart : fieldParts) {
            Class<?> fieldType = getFieldType(innerClass, fieldPart);
            switch (fieldType.getName()) {
                case ConstantUtil.LIST_TYPE -> innerClass = getListType(innerClass, fieldPart);
                default -> innerClass = fieldType;
            }
        }
        return innerClass;
    }
}