package com.nraynaud.sport.web;

import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.DateTime;
import org.joda.time.DateMidnight;
import org.joda.time.Days;

import java.util.Locale;
import java.util.Date;

public class DateHelper {
    private DateHelper() {
    }

    public static DateTimeFormatter pattern(final String pattern) {
        return DateTimeFormat.forPattern(pattern).withLocale(Locale.FRANCE);
    }

    public static DateTime today() {
        return new DateTime(new DateMidnight());
    }

    public static String humanizePastDate(final Date date, final String ...patterns) {
        final DateTime dateTime = new DateTime(date);
        final int days = Days.daysBetween(dateTime, today()).getDays();
        final int patternLength = patterns.length;
        final String pattern = days >= 0 && days < patternLength ? patterns[days] : patterns[patternLength - 1];
        return pattern(pattern).print(dateTime);
    }

    public static String printDate(final String pattern, final Date date) {
        return pattern(pattern).print(date.getTime());
    }
}
