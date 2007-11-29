package com.nraynaud.sport.web.converter;

import com.opensymphony.xwork2.util.TypeConversionException;
import org.apache.struts2.util.StrutsTypeConverter;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"RawUseOfParameterizedType"})
public class DurationConverter extends StrutsTypeConverter {
    public static final Pattern DURATION_PATTERN = Pattern.compile("^(?:(\\d+)h)?(\\d+)?'?(?:(\\d+)(?:'')?)?\\z");


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
        return formatDuration((Long) o);
    }

    public static Long parseDuration(final String duration) throws IllegalArgumentException {
        final Matcher matcher = DURATION_PATTERN.matcher(duration);
        if (!matcher.matches())
            throw new IllegalArgumentException("invalid duration");
        final long hours = parseIntToZero(matcher.group(1));
        final long minutes = parseIntToZero(matcher.group(2));
        final long seconds = parseIntToZero(matcher.group(3));
        return Long.valueOf(hours * 3600 + minutes * 60 + seconds);
    }

    public static long parseIntToZero(final String string) throws NumberFormatException {
        if (string == null)
            return 0;
        return Long.parseLong(string);
    }

    public static String formatDuration(final Long duration) {
        final long dur = duration.longValue();
        final long hours = dur / 3600;
        final long minutes = (dur % 3600) / 60;
        final long seconds = (dur % 3600) % 60;
        return String.valueOf(hours) + 'h' + minutes + '\'' + seconds + "''";
    }
}
