package com.nraynaud.sport.web.converter;

import com.opensymphony.xwork2.util.TypeConversionException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

public class DurationConverterTest {
    @Test
    public void testVariousConvertions() {
        checkConvertion(10 * 60, "10");
        checkConvertion(10 * 60 + 43, "10'43");
        checkConvertion(3 * 3600 + 10 * 60 + 43, "3h10'43");
        checkConvertion(3 * 3600 + 10 * 60, "3h10");
        checkConvertion(11, "11''");
    }

    @Test
    public void testFormat() {
        checkFormat("10''", 10);
        checkFormat("23'11''", 23 * 60 + 11);
        checkFormat("45h23'11''", 45 * 3600 + 23 * 60 + 11);
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

    private static void checkFormat(final String expected, final long input) {
        final String result = format(Long.valueOf(input));
        assertEquals(expected, result);
    }

    private static String format(final Long in) {
        final DurationConverter converter = new DurationConverter();
        return converter.convertToString(null, in);
    }

    private static void checkConvertion(final long expected, final String input) {
        final Object result = convert(input);
        assertEquals(Long.valueOf(expected), result);
    }

    private static Object convert(final String input) {
        final DurationConverter converter = new DurationConverter();
        return converter.convertFromString(null, new String[]{input}, Long.class);
    }
}
