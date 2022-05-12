package ptg.rest.common.restutils;

import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Data
public class FilterDataForComparableType<T extends Comparable> {
    private final T goeThan;
    private final T loeThan;
    private final T equalTo;
    private final boolean isNull;

    public static @NotNull LocalDateTime localDateTimeConverter(@NotNull String filter) {
        return LocalDateTime.parse(filter, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static @NotNull LocalTime localTimeConverter(@NotNull String filter) {
        return LocalTime.parse(filter, DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public static @NotNull LocalDate localDateConverter(@NotNull String filter) {
        return LocalDate.parse(filter, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    // Для веремни и даты нужно убедиться, что в текстовом представлении нет :
    public static <K extends Comparable> @NotNull FilterDataForComparableType<K> fromString(@NotNull String filter, @NotNull FromStringConverter<K> converter) {
        K goeThan = null;
        K loeThan = null;
        K equalTo = null;
        boolean isNull = false;
        if (filter.contains("~")) {
            // задан промежуток
            int columnIndex = filter.indexOf('~');
            String goeStr = filter.substring(0, columnIndex);
            String loeStr = filter.substring(columnIndex + 1);
            if (!loeStr.isEmpty()) {
                // задана правая граница
                loeThan = converter.convert(loeStr);
            }
            if (!goeStr.isEmpty()) {
                // задана левая граница
                goeThan = converter.convert(goeStr);
            }
            if (loeThan == null && goeThan == null) {
                throw new IllegalQueryException("Wrong format of filter for comparable type");
            }
        } else if (filter.equals("NULL")) {
            isNull = true;
        } else {
            // задана точная дата
            equalTo = converter.convert(filter);
        }

        return new FilterDataForComparableType<>(goeThan, loeThan, equalTo, isNull);
    }
}
