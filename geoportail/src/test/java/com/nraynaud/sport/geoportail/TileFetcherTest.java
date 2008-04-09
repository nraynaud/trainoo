package com.nraynaud.sport.geoportail;

import org.junit.Test;

public class TileFetcherTest {

    @Test
    public void testFetchTile() {
        final String uri = "http://visu-2d.geoportail.fr/geoweb/maps8u6m2G0zV.jpg";
        GeoportailTileFetcher.getTile(uri);
    }
}
