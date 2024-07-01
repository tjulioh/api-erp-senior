package dev.tjulioh.erpsenior.util;

import com.querydsl.core.types.dsl.ComparablePath;
import com.querydsl.core.types.dsl.DatePath;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.StringPath;
import dev.tjulioh.erpsenior.domain.AbstractEntity;

import java.util.List;

public class PathUtil {

    protected static PathBuilder<?> handlePathBuilder(Class<?> clazz, List<String> fieldParts) {
        PathBuilder<?> pathBuilder = new PathBuilder<>(clazz, clazz.getSimpleName().toLowerCase());
        Class<?> innerClass = clazz;

        for (String fieldPart : fieldParts) {
            Class<?> fieldType = ReflectionUtil.getFieldType(innerClass, fieldPart);

            if (fieldType.getName().equals(ConstantUtil.LIST_TYPE)) {
                pathBuilder = pathBuilder.getList(fieldPart, clazz).any();
                innerClass =  ReflectionUtil.getListType(innerClass, fieldPart);
            } else if (fieldType.isAssignableFrom(AbstractEntity.class)) {
                pathBuilder = pathBuilder.get(fieldPart);
                innerClass = fieldType;
            }
        }
        return pathBuilder;
    }

    protected static <A extends Number & Comparable<?>> NumberPath<A> handlePathNumber(PathBuilder<?> pathBuilder, String field, Class<A> type) {
        return pathBuilder.getNumber(field, type);
    }

    protected static <A extends Comparable<?>> DatePath<A> handlePathDate(PathBuilder<?> pathBuilder, String field, Class<A> type) {
        return pathBuilder.getDate(field, type);
    }

    protected static <A extends Comparable<?>> ComparablePath<A> handlePathComparable(PathBuilder<?> pathBuilder, String field, Class<A> type) {
        return pathBuilder.getComparable(field, type);
    }

    protected static StringPath handlePathString(PathBuilder<?> pathBuilder, String field) {
        return pathBuilder.getString(field);
    }
}
