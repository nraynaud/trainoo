package com.nraynaud.sport.web.converter;

import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;
import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.DateTimeFieldType;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DateConverter extends StrutsTypeConverter {
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("dd/MM/yy");
    private static final Parser FULL_FORMAT_PARSER = new Parser() {
        public DateTime parse(final String source) throws IllegalArgumentException {
            return DATE_FORMATTER.parseDateTime(source);
        }
    };

    private static final Parser DAY_MOUTH_PARSER = new Parser() {
        public DateTime parse(final String source) throws IllegalArgumentException {
            return parseAndComplete(source, "dd/MM", DateTimeFieldType.monthOfYear());
        }
    };

    private static final Parser DAY_PARSER = new Parser() {
        public DateTime parse(final String source) throws IllegalArgumentException {
            return parseAndComplete(source, "dd", DateTimeFieldType.dayOfMonth());
        }
    };

    private static DateTime parseAndComplete(final String source, final String pattern,
                                             final DateTimeFieldType fieldType) {
        return withPastField(DateTimeFormat.forPattern(pattern).parseDateTime(source), now(), fieldType);
    }

    private static DateTime withPastField(final DateTime dateTime, final DateTime now, final DateTimeFieldType field) {
        final DateTimeFieldType next = NEXT.get(field);
        if (next == null)
            return dateTime;
        final int thisField = now.get(next);
        final int fieldValue = dateTime.get(field) > now.get(field) ? thisField - 1 : thisField;
        return withPastField(dateTime.withField(next, fieldValue), now, next);
    }

    private static final Map<DateTimeFieldType, DateTimeFieldType> NEXT = new HashMap<DateTimeFieldType, DateTimeFieldType>() {
        {
            put(DateTimeFieldType.monthOfYear(), DateTimeFieldType.year());
            put(DateTimeFieldType.dayOfMonth(), DateTimeFieldType.monthOfYear());
        }
    };

    public static final Parser WORD_PARSER = new Parser() {
        public DateTime parse(final String source) throws IllegalArgumentException {
            if (source.length() > 0) {
                if ("aujourd'hui".startsWith(source.toLowerCase()))
                    return now();
                if ("hier".startsWith(source.toLowerCase()))
                    return now().minusDays(1);
                if ("avant-hier".startsWith(source.toLowerCase()))
                    return now().minusDays(2);
            }
            throw new IllegalArgumentException();
        }
    };

    private static final Parser[] PARSERS = {FULL_FORMAT_PARSER, DAY_MOUTH_PARSER, DAY_PARSER, WORD_PARSER};

    private static DateTime now() {
        return new DateTime();
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        final String source = values[0];
        return parseDate(source);
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public String convertToString(final Map context, final Object o) {
        if (o instanceof Date) {
            return DATE_FORMATTER.print(new DateTime(o));
        }
        return "";
    }

    public static Date parseDate(final String source) {
        return parseDateTime(source).toDate();
    }

    private static DateTime parseDateTime(final String source) {
        IllegalArgumentException initialException = null;
        for (final Parser parser : PARSERS)
            try {
                return parser.parse(source);
            } catch (IllegalArgumentException e) {
                if (initialException == null)
                    initialException = e;
            }
        throw new TypeConversionException(initialException);
    }

    public static String parseAndPrettyPrint(final String source) {
        final DateMidnight now = new DateMidnight();
        final DateMidnight date = parseDateTime(source).toDateMidnight();
        final String formatted = DateTimeFormat.forPattern("EEEE dd/MM/yy").print(date);
        if (now.equals(date))
            return "Aujourd'hui (" + formatted + ")";
        if (now.minusDays(1).equals(date))
            return "Hier (" + formatted + ")";
        if (now.minusDays(2).equals(date))
            return "Avant-hier (" + formatted + ")";
        return formatted;
    }

    private interface Parser {
        DateTime parse(final String source) throws IllegalArgumentException;
    }
}