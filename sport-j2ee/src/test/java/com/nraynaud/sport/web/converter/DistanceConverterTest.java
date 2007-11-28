package com.nraynaud.sport.web.converter;

import com.opensymphony.xwork2.util.TypeConversionException;
import org.junit.Assert;
import org.junit.Test;

public class DistanceConverterTest {

    @Test
    public void testSimpleParsing() {
        final String input = "10,3";
        checkParsing(input, 10.3);
    }

    @Test
    public void testParsingWithDotSeparator() {
        final String input = "10.3";
        final double expected = 10.3;
        checkParsing(input, expected);
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

    private static void checkParsing(final String input, final double expected) {
        final Object result = new DistanceConverter().convertFromString(null, new String[]{input}, Double.class);
        Assert.assertEquals(Double.valueOf(expected), result);
    }
}
