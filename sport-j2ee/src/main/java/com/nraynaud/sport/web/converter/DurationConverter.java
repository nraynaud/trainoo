package com.nraynaud.sport.web.converter;

import static com.nraynaud.sport.web.converter.DurationConverter.Multiplier.*;
import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.util.Formatter;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"RawUseOfParameterizedType"})
public class DurationConverter extends StrutsTypeConverter {

    enum Multiplier {
        HOUR(3600), MINUTE(60), SECOND(1);

        public final int factor;

        Multiplier(final int factor) {
            this.factor = factor;
        }
    }

    private enum Parser {
        HOURS_MINUTES_SECONDS_PATTERN("(\\d+)h(\\d+)'(\\d+)(?:'')?", HOUR, MINUTE, SECOND),
        HOURS_MINUTES_M_SECONDS_PATTERN("(\\d+)h(\\d+)m(\\d+)(?:'')?", HOUR, MINUTE, SECOND),
        HOURS_MINUTES_PATTERN("(\\d+)h(\\d+)'?", HOUR, MINUTE),
        MINUTES_SECONDS_PATTERN("(\\d+)'(\\d+)(?:'')?", MINUTE, SECOND),
        MINUTES_M_SECONDS_PATTERN("(\\d+)m(\\d+)(?:'')?", MINUTE, SECOND),
        HOURS_PATTERN("(\\d+)h", HOUR),
        MINUTES_PATTERN("(\\d+)'?", MINUTE),
        SECONDS_PATTERN("(\\d+)''", SECOND);

        private final Pattern pattern;
        private final Multiplier[] multipliers;

        Parser(final String pattern, final Multiplier... multipliers) {
            this.pattern = Pattern.compile('^' + pattern + "\\z");
            this.multipliers = multipliers;
        }

        private Long parseInternal(final String text) {
            final Matcher matcher = pattern.matcher(text);
            if (!matcher.matches())
                return null;
            final int count = matcher.groupCount();
            if (count != multipliers.length)
                throw new IllegalStateException("pattern "
                        + pattern.pattern()
                        + " has "
                        + count
                        + " groups but is declared with "
                        + multipliers.length
                        + " multiplers");
            long result = 0;
            for (int i = 0; i < count; i++)
                result += multipliers[i].factor * Long.parseLong(matcher.group(i + 1));
            return Long.valueOf(result);
        }

        public static Long parse(final String text) {
            for (final Parser parser : Parser.values()) {
                final Long result = parser.parseInternal(text);
                if (result != null)
                    return result;
            }
            throw new IllegalArgumentException();
        }
    }

    public Object convertFromString(final Map context, final String[] values, final Class toClass) {
        try {
            final String input = values[0];
            return input.length() == 0 ? null : parseDuration(input);
        } catch (IllegalArgumentException e) {
            throw new TypeConversionException(e);
        }
    }

    public String convertToString(final Map context, final Object o) {
        if (o == null)
            return "";
        return formatDuration((Long) o, "h", "\'", "''");
    }

    public static Long parseDuration(final String duration) throws IllegalArgumentException {
        return Parser.parse(duration);
    }

    public static String formatDuration(final Long duration,
                                        final String hoursFmt,
                                        final String minutesFmt,
                                        final String secondsFmt) {
        final long dur = duration.longValue();
        final long hours = dur / HOUR.factor;
        final long hourRemainder = dur % HOUR.factor;
        final long minutes = hourRemainder / MINUTE.factor;
        final long seconds = hourRemainder % MINUTE.factor;
        return nilIfZero(hours, hoursFmt) + nilIfZeroTwoDigits(minutes, minutesFmt) + nilIfZeroTwoDigits(seconds,
                secondsFmt);
    }

    private static String nilIfZero(final long time, final String suffix) {
        return time == 0 ? "" : time + suffix;
    }

    private static String nilIfZeroTwoDigits(final long time, final String suffix) {
        final Formatter formatter = new Formatter(Locale.FRANCE);
        return time == 0 ? "" : formatter.format("%02d", time).toString() + suffix;
    }
}
