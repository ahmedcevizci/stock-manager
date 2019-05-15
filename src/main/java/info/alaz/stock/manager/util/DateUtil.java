package info.alaz.stock.manager.util;

import info.alaz.stock.manager.dto.TimeSpan;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class DateUtil {

    public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    public static ZonedDateTime getStartDateOfTimeSpanFromNow(TimeSpan timeSpan) {
        LocalDate localNow = LocalDate.now();
        return getMidnightOfStartOfTimeSpan(timeSpan, localNow);
    }

    public static ZonedDateTime getStartDateOfTimeSpanFrom(TimeSpan timeSpan, ZonedDateTime startingFrom) {
        return getMidnightOfStartOfTimeSpan(timeSpan, startingFrom.toLocalDate());
    }

    private static ZonedDateTime getMidnightOfStartOfTimeSpan(TimeSpan timeSpan, LocalDate localDate) {
        if (TimeSpan.TODAY.equals(timeSpan)) {
            return localDate.atStartOfDay(ZoneId.systemDefault());

        } else if (TimeSpan.LAST_MONTH.equals(timeSpan)) {
            return localDate.atStartOfDay(ZoneId.systemDefault()).minusMonths(1);
        } else {
            throw new IllegalStateException("New time span has been defined. Please update this code snippet.");
        }
    }

}
