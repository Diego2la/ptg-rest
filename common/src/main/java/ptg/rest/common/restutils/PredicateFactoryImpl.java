package ptg.rest.common.restutils;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class PredicateFactoryImpl implements PredicateFactory {

    private final Map<String, FilterProcessor> filterProcessorMap;

    private PredicateFactoryImpl(Map<String, FilterProcessor> filterProcessorMap) {
        this.filterProcessorMap = filterProcessorMap;
    }

    @Override
    public @NotNull
    Predicate makePredicate(@NotNull Map<String, String> params) {
        BooleanExpression result = null;
        for (Map.Entry<String, FilterProcessor> entry : filterProcessorMap.entrySet()) {
            String filter = params.get(entry.getKey());
            if (filter != null) {
                BooleanExpression expression = null;
                try {
                    expression = entry.getValue().process(filter);
                } catch (Exception exc) {
                    throw new IllegalQueryException("Exception while making predicate: " + exc.getMessage(), exc);
                }
                result = result == null ? expression : result.and(expression);
            }
        }
        if (result == null) {
            return Expressions.asBoolean(true).isTrue();
        }
        return result;
    }

    public static class Builder {
        Map<String, FilterProcessor> filterProcessorMap = new HashMap<>();

        public Builder bind(@NotNull String filterBy, @NotNull FilterProcessor processor) {
            filterProcessorMap.put(filterBy, processor);
            return this;
        }

        public PredicateFactory build() {
            return new PredicateFactoryImpl(filterProcessorMap);
        }
    }
}
