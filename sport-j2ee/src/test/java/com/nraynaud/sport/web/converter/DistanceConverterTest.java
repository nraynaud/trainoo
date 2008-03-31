package com.nraynaud.sport.web.converter;

import com.opensymphony.xwork2.util.TypeConversionException;
import org.junit.Assert;
import org.junit.Test;

public class DistanceConverterTest {

    @Test
    public void testSimpleParsing() {
        checkParsing(10.3, "10,3");
        checkParsing(10.3, "10.3");
        checkParsing(10, "10");
        checkParsing(10.3, "10,3km");
        checkParsing(10.3, "10.3km");
        checkParsing(10, "10km");
    }

    @Test
    public void testParsingWithError() {
        checkError("10+3");
        checkError("10kn");
        checkError("10kms");
        checkError("a10");
        checkError("-10");
        checkError("-10,3");
        checkError("-10.3");
        checkError("0");
        checkError("0,01");
    }

    private static void checkError(final String input) {
        final DistanceConverter converter = new DistanceConverter();
        try {
            converter.convertFromString(null, new String[]{input}, Double.class);
            Assert.fail("exception expected");
        } catch (TypeConversionException e) {
            //ok
        }
    }

    @Test
    public void testFormatting() {
        final DistanceConverter converter = new DistanceConverter();
        final String result = converter.convertToString(null, Double.valueOf(10.3));
        Assert.assertEquals("10,3", result);
        Assert.assertEquals("", converter.convertToString(null, null));
    }

    private static void checkParsing(final double expected, final String input) {
        final Object result = new DistanceConverter().convertFromString(null, new String[]{input}, Double.class);
        Assert.assertEquals(Double.valueOf(expected), result);
    }
}
