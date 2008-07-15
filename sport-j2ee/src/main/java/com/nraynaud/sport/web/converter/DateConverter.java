package com.nraynaud.sport.web.converter;

import com.nraynaud.sport.web.DateHelper;
import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;
import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Map;

public class DateConverter extends StrutsTypeConverter {
    private static final DateTimeFormatter DATE_FORMATTER = DateHelper.pattern("dd/MM/yy");
    private static final Parser FULL_FORMAT_PARSER = new Parser() {
        public DateTime parse(final String source) throws IllegalArgumentException {
            return DATE_FORMATTER.parseDateTime(source);
        }
    };

    private static final Parser DAY_MOUTH_PARSER = new Parser() {
        public DateTime parse(final String source) throws IllegalArgumentException {
            return parseAndComplete(source, "dd/MM");
        }
    };

    private static final Parser DAY_PARSER = new Parser() {
        public DateTime parse(final String source) throws IllegalArgumentException {
            return parseAndComplete(source, "dd");
        }
    };

    private static DateTime parseAndComplete(final String source, final String pattern) {
        final MutableDateTime date = new MutableDateTime(DateHelper.today());
        final int result = DateHelper.pattern(pattern).parseInto(date, source, 0);
        if (result != source.length())
            throw new IllegalArgumentException();
        return new DateTime(date);
    }

    public static final Parser WORD_PARSER = new Parser() {
        public DateTime parse(final String source) throws IllegalArgumentException {
            if (source.length() > 0) {
                if ("aujourd'hui".startsWith(source.toLowerCase()))
                    return DateHelper.today();
                if ("hier".startsWith(source.toLowerCase()))
                    return DateHelper.today().minusDays(1);
                if ("avant-hier".startsWith(source.toLowerCase()))
                    return DateHelper.today().minusDays(2);
            }
            throw new IllegalArgumentException();
        }
    };

    private static final Parser[] PARSERS = {FULL_FORMAT_PARSER, DAY_MOUTH_PARSER, DAY_PARSER, WORD_PARSER};

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
        final String pattern = "EEEE dd/MM/yy";
        return DateHelper.humanizePastDate(parseDateTime(source).toDate(),
                "'Aujourd''hui ('" + pattern + "')'",
                "'Hier ('" + pattern + "')'",
                "'Avant-hier ('" + pattern + "')'",
                pattern);
    }

    private interface Parser {
        DateTime parse(final String source) throws IllegalArgumentException;
    }
}