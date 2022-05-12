package ptg.rest.common.restutils;

import com.querydsl.core.types.Predicate;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface PredicateFactory {
    @NotNull
    Predicate makePredicate(@NotNull Map<String, String> params);
}
