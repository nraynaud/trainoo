package com.nraynaud.sport.web.converter;

import org.junit.Assert;
import org.junit.Test;

public class EnergyConverterTest {

    @Test
    public void testSimple() {
        final EnergyConverter converter = new EnergyConverter();
        final Long result = (Long) converter.convertFromString(null, new String[]{"1000"}, Long.class);
        Assert.assertEquals(result.longValue(), 1000L);
    }

    @Test
    public void testRoundtrip() {
        final EnergyConverter converter = new EnergyConverter();
        final String s = converter.convertToString(null, new Long(1000));
        final Long result = (Long) converter.convertFromString(null, new String[]{s}, Long.class);
        Assert.assertEquals(result.longValue(), 1000L);
    }

    @Test
    public void testSpace() {
        final EnergyConverter converter = new EnergyConverter();
        final Long result = (Long) converter.convertFromString(null, new String[]{"1\u00A0404"}, Long.class);
        Assert.assertEquals(result.longValue(), 1404L);
    }
}
