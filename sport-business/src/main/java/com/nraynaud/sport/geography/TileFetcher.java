package com.nraynaud.sport.geography;

public interface TileFetcher {
    /**
     * @param name
     * @return the JPEG content of the tile.
     */
    byte[] fetchTile(String name);
}
