package com.nraynaud.sport.web.converter;

import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.Map;

public class DateConverter extends StrutsTypeConverter {
    private static final Parser FULL_FORMAT_PARSER = new Parser() {
        public Date parse(final String source) throws IllegalArgumentException {
            return DateTimeFormat.forPattern("dd/MM/yy").parseDateTime(source).toDate();
        }
    };
    private static final Parser DAY_MOUTH_PARSER = new Parser() {
        public Date parse(final String source) throws IllegalArgumentException {
            final DateTime dateTime = DateTimeFormat.forPattern("dd/MM").parseDateTime(source);
            return dateTime.withYear(new DateTime().getYear()).toDate();
        }
    };
    private static final Parser DAY_PARSER = new Parser() {
        public Date parse(final String source) throws IllegalArgumentException {
            final DateTime dateTime = DateTimeFormat.forPattern("dd").parseDateTime(source);
            final DateTime now = new DateTime();
            return dateTime.withYear(now.getYear()).withMonthOfYear(now.getMonthOfYear()).toDate();
        }
    };

    public static final Parser TODAY_PARSER = new Parser() {
        public Date parse(final String source) throws IllegalArgumentException {
            if (source.equalsIgnoreCase("aujourd'hui"))
                return new Date();
            throw new IllegalArgumentException();
        }
    };

    private static final Parser[] PARSERS = {FULL_FORMAT_PARSER, DAY_MOUTH_PARSER, DAY_PARSER, TODAY_PARSER};

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        final String source = values[0];
        return parseDate(source);
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    public String convertToString(final Map context, final Object o) {
        if (o instanceof Date) {
            return DateTimeFormat.forPattern("dd/MM/yy").print(new DateTime(o));
        }
        return "";
    }

    public static Date parseDate(final String source) {
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

    private interface Parser {
        Date parse(final String source) throws IllegalArgumentException;
    }
}
