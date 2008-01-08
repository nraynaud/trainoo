package com.nraynaud.sport.web.converter;

import com.opensymphony.xwork2.util.TypeConversionException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverterTest {
    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    @Test
    public void testSimpleConvertion() throws ParseException {
        final Date expected = FORMAT.parse("23/12/2006");
        checkDate(expected, "23/12/2006");
        checkDate(expected, "23/12/06");
    }

    @Test
    public void testConvertDayAndMonth() throws ParseException {
        checkPartialDate("dd/MM");
    }

    @Test
    public void testConvertDayOnly() throws ParseException {
        final String format = "dd";
        checkPartialDate(format);
    }

    @Test
    public void testStringFormatter() throws ParseException {
        final DateConverter converter = new DateConverter();
        final Date input = FORMAT.parse("23/11/2004");
        final String result = converter.convertToString(null, input);
        assertEquals("23/11/04", result);
        assertEquals("", converter.convertToString(null, null));
    }

    @Test
    public void testInputError() throws ParseException {
        final DateConverter converter = new DateConverter();
        try {
            converter.convertFromString(null, new String[]{"lol"}, Date.class);
            fail();
        } catch (TypeConversionException e) {
            //ok
        }
    }


    private static void checkPartialDate(final String format) {
        final Date expected = new Date();
        final String input = new SimpleDateFormat(format).format(expected);
        checkDate(expected, input);
    }

    private static void checkDate(final Date expected, final String input) {
        final DateConverter converter = new DateConverter();
        final Object result = converter.convertFromString(null, new String[]{input}, Date.class);
        assertEquals(FORMAT.format(expected), FORMAT.format(result));
    }
}