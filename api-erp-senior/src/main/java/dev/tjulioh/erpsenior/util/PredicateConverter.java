package dev.tjulioh.erpsenior.util;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PredicateConverter {

    private static final String ERROR_INVALID_FIELD_TYPE = "Invalid field type for %s condition: %s";
    private static final String ERROR_INVALID_CONDITION = "Invalid condition";
    private static final String ERROR_INVALID_OPERATOR = "Invalid operator: %s";
    private static final String ERROR_INVALID_FILTER = "Invalid filter string";
    private static final String ERROR_INVALID_VALUE = "Invalid value string";

    private final Class<?> clazz;

    public PredicateConverter(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Predicate toPredicate(String source) {
        List<String> parts = splitString(source);
        if (parts.isEmpty() || !isInSequence(parts.size())) {
            throw new IllegalArgumentException(ERROR_INVALID_FILTER);
        }

        PathBuilder<?> pathBuilder = null;
        BooleanExpression predicate = null;
        BooleanExpression newPredicate = null;
        String field = null;
        String value = null;
        String operator = null;
        String fieldTypeName = "";

        for (int i = 0; i < parts.size(); i++) {
            String part = parts.get(i);

            switch (part.toUpperCase()) {
                case ConstantUtil.AND, ConstantUtil.OR -> operator = part;
                case ConstantUtil.EQ -> newPredicate = handleEqualsCondition(pathBuilder, field, fieldTypeName, value);
                case ConstantUtil.NEQ, ConstantUtil.NE ->
                        newPredicate = handleNotEqualsCondition(pathBuilder, field, fieldTypeName, value);
                case ConstantUtil.CONTAINS -> newPredicate = handleContainsCondition(pathBuilder, field, value);
                case ConstantUtil.LIKE -> newPredicate = handleLikeCondition(pathBuilder, field, value);
                case ConstantUtil.STARTS -> newPredicate = handleStartsWithCondition(pathBuilder, field, value);
                case ConstantUtil.ENDS -> newPredicate = handleEndsWithCondition(pathBuilder, field, value);
                case ConstantUtil.LTE, ConstantUtil.LOE ->
                        newPredicate = handleLessThanOrEqualsCondition(pathBuilder, field, fieldTypeName, value);
                case ConstantUtil.LT ->
                        newPredicate = handleLessThanCondition(pathBuilder, field, fieldTypeName, value);
                case ConstantUtil.GT ->
                        newPredicate = handleGreaterThanCondition(pathBuilder, field, fieldTypeName, value);
                case ConstantUtil.GOE, ConstantUtil.GTE ->
                        newPredicate = handleGreaterThanOrEqualsCondition(pathBuilder, field, fieldTypeName, value);
                default -> {
                    List<String> fieldParts = List.of(part.split("\\."));
                    pathBuilder = PathUtil.handlePathBuilder(clazz, fieldParts);
                    field = fieldParts.getLast();
                    Class<?> fieldType = ReflectionUtil.getInnerType(clazz, fieldParts);
                    fieldTypeName = fieldType.getName();
                    value = sanitizeString(parts.get(i + 2));
                }
            }
            if (Objects.nonNull(newPredicate)) {
                predicate = handleOperator(newPredicate, predicate, operator);
                newPredicate = null;
                i++;
            }
        }

        return predicate;
    }

    public static boolean isInSequence(int x) {
        if (x == 3) {
            return true;
        }
        int a1 = 7;
        int d = 4;
        int n = (x - a1) / d + 1;
        return n > 0 && x == a1 + (n - 1) * d;
    }

    public List<String> splitString(String source) {
        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();
        boolean insideDoubleQuotes = false;
        boolean insideSingleQuotes = false;

        for (char c : source.toCharArray()) {
            if (c == ' ' && !insideDoubleQuotes && !insideSingleQuotes) {
                parts.add(currentPart.toString());
                currentPart = new StringBuilder();
            } else if (c == '"') {
                insideDoubleQuotes = !insideDoubleQuotes;
                currentPart.append(c);
            } else if (c == '\'') {
                insideSingleQuotes = !insideSingleQuotes;
                currentPart.append(c);
            } else {
                currentPart.append(c);
            }
        }

        if (!currentPart.isEmpty()) {
            parts.add(currentPart.toString());
        }
        return parts;
    }

    public String sanitizeString(String source) {
        if (Objects.isNull(source)) {
            throw new IllegalArgumentException(ERROR_INVALID_VALUE);
        }
        if (source.startsWith("\"") && source.endsWith("\"") || source.startsWith("'") && source.endsWith("'")) {
            source = source.substring(1, source.length() - 1);
        }
        return source;
    }

    private BooleanExpression handleContainsCondition(PathBuilder<?> pathBuilder, String field, String value) {
        return PathUtil.handlePathString(pathBuilder, field).containsIgnoreCase(value);
    }

    private BooleanExpression handleLikeCondition(PathBuilder<?> pathBuilder, String field, String value) {
        return PathUtil.handlePathString(pathBuilder, field).likeIgnoreCase(value);
    }

    private BooleanExpression handleStartsWithCondition(PathBuilder<?> pathBuilder, String field, String value) {
        return PathUtil.handlePathString(pathBuilder, field).startsWithIgnoreCase(value);
    }

    private BooleanExpression handleEndsWithCondition(PathBuilder<?> pathBuilder, String field, String value) {
        return PathUtil.handlePathString(pathBuilder, field).endsWithIgnoreCase(value);
    }


    private BooleanExpression handleEqualsCondition(PathBuilder<?> pathBuilder, String field, String fieldTypeName, String value) {
        return switch (fieldTypeName) {
            case ConstantUtil.STRING_TYPE -> PathUtil.handlePathString(pathBuilder, field).equalsIgnoreCase(value);
            case ConstantUtil.UUID_TYPE ->
                    PathUtil.handlePathComparable(pathBuilder, field, UUID.class).eq(UUID.fromString(value));
            case ConstantUtil.INTEGER_TYPE ->
                    PathUtil.handlePathNumber(pathBuilder, field, Integer.class).eq(Integer.valueOf(value));
            default ->
                    throw new IllegalArgumentException(ERROR_INVALID_FIELD_TYPE.formatted(ConstantUtil.EQ, fieldTypeName));
        };
    }

    private BooleanExpression handleNotEqualsCondition(PathBuilder<?> pathBuilder, String field, String fieldTypeName, String value) {
        return switch (fieldTypeName) {
            case ConstantUtil.STRING_TYPE -> PathUtil.handlePathString(pathBuilder, field).notEqualsIgnoreCase(value);
            case ConstantUtil.UUID_TYPE ->
                    PathUtil.handlePathComparable(pathBuilder, field, UUID.class).ne(UUID.fromString(value));
            case ConstantUtil.INTEGER_TYPE ->
                    PathUtil.handlePathNumber(pathBuilder, field, Integer.class).ne(Integer.valueOf(value));
            default ->
                    throw new IllegalArgumentException(ERROR_INVALID_FIELD_TYPE.formatted(ConstantUtil.NE.concat("|").concat(ConstantUtil.NEQ), fieldTypeName));
        };
    }

    private BooleanExpression handleLessThanOrEqualsCondition(PathBuilder<?> pathBuilder, String field, String fieldTypeName, String value) {
        return switch (fieldTypeName) {
            case ConstantUtil.INTEGER_TYPE ->
                    PathUtil.handlePathNumber(pathBuilder, field, Integer.class).loe(Integer.valueOf(value));
            case ConstantUtil.BIG_DECIMAL_TYPE ->
                    PathUtil.handlePathNumber(pathBuilder, field, BigDecimal.class).loe(BigDecimal.valueOf(Double.parseDouble(value)));
            case ConstantUtil.LOCAL_DATE_TYPE ->
                    PathUtil.handlePathDate(pathBuilder, field, LocalDate.class).loe(LocalDate.parse(value));
            case ConstantUtil.LOCAL_DATE_TIME_TYPE ->
                    PathUtil.handlePathDate(pathBuilder, field, LocalDateTime.class).loe(LocalDateTime.parse(value));
            default ->
                    throw new IllegalArgumentException(ERROR_INVALID_FIELD_TYPE.formatted(ConstantUtil.LTE.concat("|").concat(ConstantUtil.LOE), fieldTypeName));
        };
    }

    private BooleanExpression handleLessThanCondition(PathBuilder<?> pathBuilder, String field, String fieldTypeName, String value) {
        return switch (fieldTypeName) {
            case ConstantUtil.INTEGER_TYPE ->
                    PathUtil.handlePathNumber(pathBuilder, field, Integer.class).lt(Integer.parseInt(value));
            case ConstantUtil.BIG_DECIMAL_TYPE ->
                    PathUtil.handlePathNumber(pathBuilder, field, BigDecimal.class).lt(BigDecimal.valueOf(Double.parseDouble(value)));
            case ConstantUtil.LOCAL_DATE_TYPE ->
                    PathUtil.handlePathDate(pathBuilder, field, LocalDate.class).lt(LocalDate.parse(value));
            case ConstantUtil.LOCAL_DATE_TIME_TYPE ->
                    PathUtil.handlePathDate(pathBuilder, field, LocalDateTime.class).lt(LocalDateTime.parse(value));
            default ->
                    throw new IllegalArgumentException(ERROR_INVALID_FIELD_TYPE.formatted(ConstantUtil.LTE.concat("|").concat(ConstantUtil.LOE), fieldTypeName));
        };
    }

    private BooleanExpression handleGreaterThanCondition(PathBuilder<?> pathBuilder, String field, String fieldTypeName, String value) {
        return switch (fieldTypeName) {
            case ConstantUtil.INTEGER_TYPE ->
                    PathUtil.handlePathNumber(pathBuilder, field, Integer.class).gt(Integer.parseInt(value));
            case ConstantUtil.BIG_DECIMAL_TYPE ->
                    PathUtil.handlePathNumber(pathBuilder, field, BigDecimal.class).gt(BigDecimal.valueOf(Double.parseDouble(value)));
            case ConstantUtil.LOCAL_DATE_TYPE ->
                    PathUtil.handlePathDate(pathBuilder, field, LocalDate.class).gt(LocalDate.parse(value));
            case ConstantUtil.LOCAL_DATE_TIME_TYPE ->
                    PathUtil.handlePathDate(pathBuilder, field, LocalDateTime.class).gt(LocalDateTime.parse(value));
            default ->
                    throw new IllegalArgumentException(ERROR_INVALID_FIELD_TYPE.formatted(ConstantUtil.GT, fieldTypeName));
        };
    }

    private BooleanExpression handleGreaterThanOrEqualsCondition(PathBuilder<?> pathBuilder, String field, String fieldTypeName, String value) {
        return switch (fieldTypeName) {
            case ConstantUtil.INTEGER_TYPE ->
                    PathUtil.handlePathNumber(pathBuilder, field, Integer.class).goe(Integer.parseInt(value));
            case ConstantUtil.BIG_DECIMAL_TYPE ->
                    PathUtil.handlePathNumber(pathBuilder, field, BigDecimal.class).goe(BigDecimal.valueOf(Double.parseDouble(value)));
            case ConstantUtil.LOCAL_DATE_TYPE ->
                    PathUtil.handlePathDate(pathBuilder, field, LocalDate.class).goe(LocalDate.parse(value));
            case ConstantUtil.LOCAL_DATE_TIME_TYPE ->
                    PathUtil.handlePathDate(pathBuilder, field, LocalDateTime.class).goe(LocalDateTime.parse(value));
            default ->
                    throw new IllegalArgumentException(ERROR_INVALID_FIELD_TYPE.formatted(ConstantUtil.GOE.concat("|").concat(ConstantUtil.GTE), fieldTypeName));
        };
    }

    private BooleanExpression handleOperator(BooleanExpression newPredicate, BooleanExpression predicate, String operator) {
        if (Objects.isNull(predicate)) {
            return newPredicate;
        }
        if (Objects.isNull(operator)) {
            throw new IllegalArgumentException(ERROR_INVALID_CONDITION);
        }
        return switch (operator.toUpperCase()) {
            case ConstantUtil.AND -> predicate.and(newPredicate);
            case ConstantUtil.OR -> predicate.or(newPredicate);
            default -> throw new IllegalArgumentException(ERROR_INVALID_OPERATOR.formatted(operator));
        };
    }
}