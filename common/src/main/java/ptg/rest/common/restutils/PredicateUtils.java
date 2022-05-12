package ptg.rest.common.restutils;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import org.jetbrains.annotations.NotNull;

public class PredicateUtils {

    public static <T extends Number & Comparable<?>> @NotNull BooleanExpression createPredicateForNumericType(@NotNull NumberExpression<T> path, @NotNull FilterDataForComparableType<T> filterData) {
        if (filterData.isNull()) {
            return path.isNull();
        }
        if (filterData.getEqualTo() != null) {
            return path.eq(filterData.getEqualTo());
        }
        if (filterData.getGoeThan() != null && filterData.getLoeThan() != null) {
            return path.between(filterData.getGoeThan(), filterData.getLoeThan());
        }
        if (filterData.getGoeThan() != null) {
            return path.goe(filterData.getGoeThan());
        }
        if (filterData.getLoeThan() != null) {
            return path.loe(filterData.getLoeThan());
        }
        throw new IllegalArgumentException("Illegal filter for comparable type");
    }

    public static <T extends Comparable> @NotNull BooleanExpression createPredicateForComparableType(@NotNull ComparableExpression<T> path, @NotNull FilterDataForComparableType<T> filterData) {
        if (filterData.isNull()) {
            return path.isNull();
        }
        if (filterData.getEqualTo() != null) {
            return path.eq(filterData.getEqualTo());
        }
        if (filterData.getGoeThan() != null && filterData.getLoeThan() != null) {
            return path.between(filterData.getGoeThan(), filterData.getLoeThan());
        }
        if (filterData.getGoeThan() != null) {
            return path.goe(filterData.getGoeThan());
        }
        if (filterData.getLoeThan() != null) {
            return path.loe(filterData.getLoeThan());
        }
        throw new IllegalArgumentException("Illegal filter for comparable type");
    }

    public static <T> @NotNull BooleanExpression createPredicateForSimpleType(@NotNull SimpleExpression<T> path, @NotNull FilterDataForSimpleType<T> filterData) {
        if (filterData.isNull()) {
            return path.isNull();
        }
        if (filterData.getInValues().isEmpty()) {
            throw new IllegalArgumentException("In value must not be empty");
        }
        if (filterData.isNotIn()) {
            return path.notIn(filterData.getInValues());
        }
        return path.in(filterData.getInValues());
    }
}
