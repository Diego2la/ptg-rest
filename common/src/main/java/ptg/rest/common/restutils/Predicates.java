package ptg.rest.common.restutils;

import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.core.types.dsl.StringExpression;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Predicates {

    public static FilterProcessor predicateStringStarts(StringExpression path) {
        return path::startsWithIgnoreCase;
    }

    public static FilterProcessor predicateStringContains(StringExpression path) {
        return path::containsIgnoreCase;
    }

    public static FilterProcessor predicateLong(SimpleExpression<Long> path) {
        return (s) -> PredicateUtils.createPredicateForSimpleType(path, FilterDataForSimpleType.fromString(s, Long::valueOf));
    }

    public static FilterProcessor predicateInteger(SimpleExpression<Integer> path) {
        return (s) -> PredicateUtils.createPredicateForSimpleType(path, FilterDataForSimpleType.fromString(s, Integer::valueOf));
    }

    public static FilterProcessor predicateBigDecimal(NumberExpression<BigDecimal> path) {
        return (s) -> PredicateUtils.createPredicateForNumericType(path, FilterDataForComparableType.fromString(s, BigDecimal::new));
    }

    public static <T> FilterProcessor predicateEnum(SimpleExpression<T> path, FromStringConverter<T> converter) {
        return (s) -> PredicateUtils.createPredicateForSimpleType(path, FilterDataForSimpleType.fromString(s, converter));
    }

    public static FilterProcessor predicateLocalDate(ComparableExpression<LocalDate> path) {
        return (s) -> PredicateUtils.createPredicateForComparableType(path, FilterDataForComparableType.fromString(s, FilterDataForComparableType::localDateConverter));
    }

    public static FilterProcessor predicateLocalDateTime(ComparableExpression<LocalDateTime> path) {
        return (s) -> PredicateUtils.createPredicateForComparableType(path, FilterDataForComparableType.fromString(s, FilterDataForComparableType::localDateTimeConverter));
    }

    public static FilterProcessor predicateLocalTime(ComparableExpression<LocalTime> path) {
        return (s) -> PredicateUtils.createPredicateForComparableType(path, FilterDataForComparableType.fromString(s, FilterDataForComparableType::localTimeConverter));
    }
}
