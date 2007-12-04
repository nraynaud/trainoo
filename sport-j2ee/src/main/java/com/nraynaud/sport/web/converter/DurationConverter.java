package com.nraynaud.sport.web.converter;

import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"RawUseOfParameterizedType"})
public class DurationConverter extends StrutsTypeConverter {

    private static final int HOUR = 3600;

    private static final int MINUTE = 60;

    private static final int SECOND = 1;

    private enum Parser {
        HOURS_MINUTES_SECONDS_PATTERN("(\\d+)h(\\d+)'(\\d+)(?:'')?", HOUR, MINUTE, SECOND),
        HOURS_MINUTES_PATTERN("(\\d+)h(\\d+)'?", HOUR, MINUTE),
        MINUTES_SECONDS_PATTERN("(\\d+)'(\\d+)(?:'')?", MINUTE, SECOND),
        MINUTES_PATTERN("(\\d+)'?", MINUTE),
        SECONDS_PATTERN("(\\d+)''", SECOND);

        private final Pattern pattern;
        private final int[] multipliers;

        Parser(final String pattern, final int... multipliers) {
            this.pattern = Pattern.compile('^' + pattern + "\\z");
            this.multipliers = multipliers;
        }

        private Long parseInternal(final String text) {
            final Matcher matcher = pattern.matcher(text);
            if (!matcher.matches())
                return null;
            final int count = matcher.groupCount();
            long result = 0;
            for (int i = 0; i < count; i++)
                result += multipliers[i] * Long.parseLong(matcher.group(i + 1));
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
            return parseDuration(values[0]);
        } catch (IllegalArgumentException e) {
            throw new TypeConversionException(e);
        }
    }

    public String convertToString(final Map context, final Object o) {
        if (o == null)
            return "";
        return formatDuration((Long) o, new String[]{"h", "\'", "''"});
    }

    public static Long parseDuration(final String duration) throws IllegalArgumentException {
        return Parser.parse(duration);
    }

    public static String formatDuration(final Long duration, final String[] format) {
        final long dur = duration.longValue();
        final long hours = dur / HOUR;
        final long minutes = (dur % HOUR) / MINUTE;
        final long seconds = (dur % HOUR) % MINUTE;
        return nilIfZero(hours, format[0]) + nilIfZero(minutes, format[1]) + nilIfZero(seconds, format[2]);
    }

    private static String nilIfZero(final long time, final String suffix) {
        return time == 0 ? "" : time + suffix;
    }
}
