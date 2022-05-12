package ptg.rest.common.restutils;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FilterProcessor {
    @NotNull
    BooleanExpression process(@NotNull String filter);
}
