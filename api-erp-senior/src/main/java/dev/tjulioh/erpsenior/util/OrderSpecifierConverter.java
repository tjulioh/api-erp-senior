package dev.tjulioh.erpsenior.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class OrderSpecifierConverter {

    private static final String ERROR_INVALID_ORDER = "Invalid order condition: %s";
    private static final String ERROR_INVALID_TYPE_ORDER = "Invalid type of order %s";

    private final Class<?> clazz;

    public OrderSpecifierConverter(Class<?> clazz) {
        this.clazz = clazz;
    }

    public OrderSpecifier<?>[] toOrderSpecifier(String filter) {
        List<String> parts = Arrays.asList(filter.split(","));
        if (parts.isEmpty() || parts.size() % 2 == 0) {
            throw new IllegalArgumentException(ERROR_INVALID_ORDER.formatted(filter));
        }

        OrderSpecifier<?>[] order = new OrderSpecifier[parts.size()];
        for (int i = 0; i < parts.size(); i++) {
            List<String> condition = Arrays.asList(parts.get(i).trim().split("\\s+"));

            List<String> fieldParts = List.of(condition.getFirst().split("\\."));
            PathBuilder<?> pathBuilder = PathUtil.handlePathBuilder(clazz, fieldParts);
            String field = fieldParts.getLast();
            Class<?> fieldType = ReflectionUtil.getInnerType(clazz, fieldParts);
            String fieldTypeName = fieldType.getName();

            switch (condition.getLast().toUpperCase()) {
                case ConstantUtil.DESC -> order[i] = handleOrderCondition(pathBuilder, field, fieldTypeName, Order.DESC);
                case ConstantUtil.ASC -> order[i] = handleOrderCondition(pathBuilder, field, fieldTypeName, Order.ASC);
                default -> throw new IllegalArgumentException(ERROR_INVALID_ORDER.formatted(condition.getLast()));
            }
        }
        return order;
    }

    private OrderSpecifier<?> handleOrderCondition(PathBuilder<?> pathBuilder, String field, String fieldTypeName, Order order) {
        ComparableExpressionBase<?> comparableExpression;
        switch (fieldTypeName) {
            case ConstantUtil.STRING_TYPE -> comparableExpression = PathUtil.handlePathString(pathBuilder, field);
            case ConstantUtil.UUID_TYPE -> comparableExpression = PathUtil.handlePathComparable(pathBuilder, field, UUID.class);
            case ConstantUtil.INTEGER_TYPE -> comparableExpression = PathUtil.handlePathNumber(pathBuilder, field, Integer.class);
            case ConstantUtil.BIG_DECIMAL_TYPE -> comparableExpression = PathUtil.handlePathNumber(pathBuilder, field, BigDecimal.class);
            case ConstantUtil.LOCAL_DATE_TYPE -> comparableExpression = PathUtil.handlePathDate(pathBuilder, field, LocalDate.class);
            case ConstantUtil.LOCAL_DATE_TIME_TYPE -> comparableExpression = PathUtil.handlePathDate(pathBuilder, field, LocalDateTime.class);
            default -> throw new IllegalArgumentException(ERROR_INVALID_TYPE_ORDER.formatted(fieldTypeName));
        }
        return new OrderSpecifier<>(order, comparableExpression);
    }
}
