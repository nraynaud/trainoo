package com.nraynaud.sport.geoportail;

import com.nraynaud.sport.geography.TileFetcher;
import org.apache.commons.io.IOUtils;

import java.io.*;

public class TileCache implements TileFetcher {
    private static final File CACHE_DIRECTORY;

    static {
        final File file = new File(System.getProperty("java.io.tmpdir"), "geoportail");
        if (!file.exists())
            file.mkdirs();
        CACHE_DIRECTORY = file;
    }

    final TileFetcher fetcher = new GeoportailTileFetcher();

    public TileData fetchTile(final String prefix, final int zoom, final int x, final int y, final String suffix) {
        final File file = getFile(prefix, zoom, x, y, suffix);
        final TileData data;
        if (file.exists()) {
            try {
                data = new TileData("", new FileInputStream(file), file.length());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            data = store(prefix, zoom, x, y, suffix, fetcher.fetchTile(prefix, zoom, x, y, suffix));
        }
        return data;
    }

    private static TileData store(final String prefix, final int zoom, final int x, final int y, final String suffix,
                                  final TileData data) {
        final File file = getFile(prefix, zoom, x, y, suffix);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        try {
            final FileOutputStream fileOutputStream = new FileOutputStream(file);
            try {
                try {
                    IOUtils.copy(data.inputStream, fileOutputStream);
                } finally {
                    fileOutputStream.close();
                }
            } finally {
                data.inputStream.close();
            }
            return new TileData(data.mimeType, new FileInputStream(file), data.length);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static File getFile(final String prefix, final int zoom, final int x, final int y,
                                final String suffix) {
        final File directory = new File(new File(CACHE_DIRECTORY, prefix), String.valueOf(zoom));
        return new File(directory, prefix + '_' + zoom + '_' + x + '_' + y + '_' + suffix);
    }
}
