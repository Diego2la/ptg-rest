package ptg.rest.common.restutils;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Data
public class FilterDataForSimpleType<T> {
    private final Collection<T> inValues;
    private final boolean notIn;
    private final boolean isNull;

    public static <K> @NotNull FilterDataForSimpleType<K> fromString(@NotNull String filter, @NotNull FromStringConverter<K> converter) {
        Collection<K> inValues = null;
        boolean notIn = false;
        boolean isNull = false;
        if (filter.equals("NULL")) {
            isNull = true;
        } else {
            if (filter.charAt(0) == '!') {
                notIn = true;
                filter = filter.substring(1);
            }
            inValues = Arrays.stream(filter.split(",")).map(converter::convert).collect(Collectors.toList());
        }

        return new FilterDataForSimpleType<>(inValues, notIn, isNull);
    }
}
