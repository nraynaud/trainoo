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
    }

    @Test
    public void testParsingWithDotSeparator() {
    }

    @Test
    public void testParsingWithError() {
        final DistanceConverter converter = new DistanceConverter();
        try {
            final String input = "10+3";
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
