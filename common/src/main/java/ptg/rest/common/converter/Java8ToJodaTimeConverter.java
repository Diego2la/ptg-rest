package ptg.rest.common.converter;

public class Java8ToJodaTimeConverter {

    public static java.time.LocalDate convert(org.joda.time.LocalDate jodaDate) {
        return java.time.LocalDate.of(jodaDate.getYear(), jodaDate.getMonthOfYear(), jodaDate.getDayOfMonth());
    }

    public static org.joda.time.LocalDate convert(java.time.LocalDate javaDate) {
        return new org.joda.time.LocalDate(javaDate.getYear(), javaDate.getMonthValue(), javaDate.getDayOfMonth());
    }

    public static org.joda.time.LocalDateTime convert(java.time.LocalDateTime javaDateTime) {
        return new org.joda.time.LocalDateTime(javaDateTime.getYear(),
                javaDateTime.getMonthValue(),
                javaDateTime.getDayOfMonth(),
                javaDateTime.getHour(),
                javaDateTime.getMinute(),
                javaDateTime.getSecond());
    }
}
