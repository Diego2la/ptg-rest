package ptg.rest.common.restutils;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface FromStringConverter<T> {
    @NotNull
    T convert(@NotNull String value);
}
