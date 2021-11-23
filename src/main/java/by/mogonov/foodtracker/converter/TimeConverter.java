package by.mogonov.foodtracker.converter;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TimeConverter {
    public static LocalDateTime microsecondsToLocalDateTime(Long timeMicros) {
        Instant instant = Instant.EPOCH.plus(timeMicros, ChronoUnit.MICROS);
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }
}
