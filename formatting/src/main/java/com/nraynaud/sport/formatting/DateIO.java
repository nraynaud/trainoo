package com.nraynaud.sport.formatting;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormatter;

import java.text.ParseException;
import java.util.Date;

public class DateIO {
    private static final Parser WORD_PARSER = new Parser() {
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

    public static final DateTimeFormatter DATE_FORMATTER = DateHelper.pattern("dd/MM/yy");
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
    private static final Parser[] PARSERS = {FULL_FORMAT_PARSER, DAY_MOUTH_PARSER, DAY_PARSER, WORD_PARSER};

    private DateIO() {
    }

    private static DateTime parseAndComplete(final String source, final String pattern) {
        final MutableDateTime date = new MutableDateTime(DateHelper.today());
        final int result = DateHelper.pattern(pattern).parseInto(date, source, 0);
        if (result != source.length())
            throw new IllegalArgumentException();
        return new DateTime(date);
    }

    public static Date parseDate(final String source) throws ParseException {
        return parseDateTime(source).toDate();
    }

    private static DateTime parseDateTime(final String source) throws ParseException {
        IllegalArgumentException initialException = null;
        for (final Parser parser : PARSERS)
            try {
                return parser.parse(source);
            } catch (IllegalArgumentException e) {
                if (initialException == null)
                    initialException = e;
            }
        throw new ParseException(source, 0);
    }

    public static String parseAndPrettyPrint(final String source) throws ParseException {
        final String pattern = "EEEE dd/MM/yy";
        return DateHelper.humanizePastDate(parseDateTime(source).toDate(),
                "'Aujourd''hui ('" + pattern + "')'",
                "'Hier ('" + pattern + "')'",
                "'Avant-hier ('" + pattern + "')'",
                "'Le '" + pattern);
    }

    interface Parser {
        DateTime parse(final String source) throws IllegalArgumentException;
    }
}
