package com.nraynaud.sport.formatting;

import java.util.Formatter;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationIO {
    private DurationIO() {
    }

    public static Long parseDuration(final String duration) throws IllegalArgumentException {
        return Parser.parse(duration);
    }

    public static String formatDuration(final Long duration,
                                        final String hoursFmt,
                                        final String minutesFmt,
                                        final String secondsFmt) {
        final long dur = duration.longValue();
        final long hours = dur / DurationIO.Multiplier.HOUR.factor;
        final long hourRemainder = dur % DurationIO.Multiplier.HOUR.factor;
        final long minutes = hourRemainder / DurationIO.Multiplier.MINUTE.factor;
        final long seconds = hourRemainder % DurationIO.Multiplier.MINUTE.factor;
        return nilIfZero(hours, hoursFmt) + nilIfZeroTwoDigits(minutes, minutesFmt) + nilIfZeroTwoDigits(seconds,
                secondsFmt);
    }

    public static String nilIfZero(final long time, final String suffix) {
        return time == 0 ? "" : time + suffix;
    }

    public static String nilIfZeroTwoDigits(final long time, final String suffix) {
        final Formatter formatter = new Formatter(Locale.FRANCE);
        return time == 0 ? "" : formatter.format("%02d", time).toString() + suffix;
    }

    public enum Multiplier {
        HOUR(3600), MINUTE(60), SECOND(1);

        public final int factor;

        Multiplier(final int factor) {
            this.factor = factor;
        }
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public enum Parser {
        HOURS_MINUTES_SECONDS_PATTERN("(\\d+)h(\\d+)'(\\d+)(?:'')?", DurationIO.Multiplier.HOUR,
                DurationIO.Multiplier.MINUTE, DurationIO.Multiplier.SECOND),
        HOURS_MINUTES_M_SECONDS_PATTERN("(\\d+)h(\\d+)m(\\d+)(?:'')?", DurationIO.Multiplier.HOUR,
                DurationIO.Multiplier.MINUTE, DurationIO.Multiplier.SECOND),
        HOURS_MINUTES_PATTERN("(\\d+)h(\\d+)'?", DurationIO.Multiplier.HOUR, DurationIO.Multiplier.MINUTE),
        MINUTES_SECONDS_PATTERN("(\\d+)'(\\d+)(?:'')?", DurationIO.Multiplier.MINUTE,
                DurationIO.Multiplier.SECOND),
        MINUTES_M_SECONDS_PATTERN("(\\d+)m(\\d+)(?:'')?", DurationIO.Multiplier.MINUTE,
                DurationIO.Multiplier.SECOND),
        HOURS_PATTERN("(\\d+)h", DurationIO.Multiplier.HOUR),
        MINUTES_PATTERN("(\\d+)'?", DurationIO.Multiplier.MINUTE),
        SECONDS_PATTERN("(\\d+)''", DurationIO.Multiplier.SECOND);

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
}
