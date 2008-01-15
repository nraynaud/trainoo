package com.nraynaud.sport.web.converter;

import com.opensymphony.xwork2.util.TypeConversionException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class DurationConverterTest {
    @Test
    public void testVariousConvertions() {
        checkConvertion(0, 2, 0, "2");
        checkConvertion(3, 0, 0, "3h");
        checkConvertion(0, 0, 4, "4''");
        checkConvertion(0, 5, 4, "5'4");
        checkConvertion(0, 1, 4, "1'4''");
        checkConvertion(0, 5, 4, "5m4");
        checkConvertion(0, 1, 4, "1m4''");
        checkConvertion(3, 1, 4, "3h1'4");
        checkConvertion(3, 1, 4, "3h1m4");
        checkConvertion(3, 1, 4, "3h1'4''");
        checkConvertion(3, 1, 4, "3h1'4''");
        checkConvertion(3, 1, 0, "3h1");
        checkConvertion(3, 1, 0, "3h1'");
        checkConvertion(34, 15, 46, "34h15'46''");
    }

    @Test
    public void testFormat() {
        checkFormat("10''", 0, 0, 10);
        checkFormat("23'11''", 0, 23, 11);
        checkFormat("45h23'11''", 45, 23, 11);
        assertEquals("", format(null));
    }

    @Test
    public void testIllegalInput() {
        try {
            convert("lol");
            fail();
        } catch (TypeConversionException e) {
            //OK
        }
    }

    private static void checkFormat(final String expected, final int hours, final int minutes, final int seconds) {
        final String result = format(Long.valueOf(hoursMinutesSeconds(hours, minutes, seconds)));
        assertEquals(expected, result);
    }

    private static String format(final Long in) {
        final DurationConverter converter = new DurationConverter();
        return converter.convertToString(null, in);
    }

    private static void checkConvertion(final int hours, final int minutes, final int seconds, final String input) {
        final Object result = convert(input);
        assertEquals(Long.valueOf(hoursMinutesSeconds(hours, minutes, seconds)), result);
    }

    private static Object convert(final String input) {
        final DurationConverter converter = new DurationConverter();
        return converter.convertFromString(null, new String[]{input}, Long.class);
    }

    private static long hoursMinutesSeconds(final int hours, final int minutes, final int seconds) {
        return hours * 3600 + minutes * 60 + seconds;
    }
}
