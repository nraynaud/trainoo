package com.nraynaud.sport.geography;

import java.io.InputStream;

public interface TileFetcher {

    TileData fetchTile(String prefix, int zoom, int x, int y, String suffix);

    public class TileData {
        public final String mimeType;
        public final InputStream inputStream;
        public final long length;

        public TileData(final String mimeType, final InputStream inputStream, final long length) {
            this.mimeType = mimeType;
            this.inputStream = inputStream;
            this.length = length;
        }
    }
}
