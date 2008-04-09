package com.nraynaud.sport.geoportail;

import org.junit.Assert;
import org.junit.Test;

public class TileFetcherTest {

    @Test
    public void testFetchTile() {
        final String uri = "http://visu-2d.geoportail.fr/geoweb/maps8u6m2G0zV.jpg";
        GeoportailTileFetcher.getTile(uri);
    }

    @Test
    public void testTileEncoding() {
        checkEncoding("8u6M9GPp", "8u6", -2, 56, 14);
        checkEncoding("8u6I2GgKl", "8u6", 9, 846, 10);
    }

    private static void checkEncoding(final String expected, final String prefix, final int x, final int y,
                                      final int zoom) {
        final String encoded = GeoportailTileFetcher.encodeTile(prefix, x, y, zoom);
        Assert.assertEquals(expected, encoded);
    }
}
