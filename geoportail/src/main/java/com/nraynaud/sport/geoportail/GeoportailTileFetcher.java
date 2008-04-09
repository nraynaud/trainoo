package com.nraynaud.sport.geoportail;

import com.nraynaud.sport.geography.TileFetcher;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GeoportailTileFetcher implements TileFetcher {
    private static final String UA_STRING = "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en-US; rv:1.8.1.13)";
    private static final char[] LONG_TABLE = new char[]{'X', 'u', 'P', '4', 'N', 'G', 'Z', '8', 'n', 'g', 'I', 'c', 'j',
            'K', 'M', '7', 'W', 'Q', 'T', 'b', '2', 'q', 'C', '1', 'e', 'h', 'O', 'o', 't', 'L', 'H', '9', 'z', 's',
            'm', 'a', 'w', 'J', 'S', 'Y', 'l', 'A', 'i', 'f', 'U', 'v', 'y', 'r', 'k', 'E', 'D', 'x', '3', '6', '5',
            'F', 'p', '0', 'V', 'R', 'd', 'B'};
    private static final char[] SHORT_TABLE = new char[]{'x', 'G', '3', 'r', '8', 'k', 'T', 'Q', 'm', 'Y', 'I', 'c',
            'j', 'K', 'M', '7', 'W', 'b', '2', 'q', 't', 'L', 'H', '9', 'f'};
    private static final char[] SIGN_TABLE = new char[]{'2', 'A', '9', 'r'};
    private static volatile String[][] cookies = {};

    static TileData getTile(final String uri) {
        final HttpClient client = new HttpClient();
        final GetMethod method = new GetMethod(uri);
        try {
            method.setRequestHeader("User-Agent", UA_STRING);
            fetchImageAndCookie(client, method);
            final byte[] body = method.getResponseBody();
            return new TileData(method.getResponseHeader("Content-Type").getValue(), new ByteArrayInputStream(body),
                    body.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static int fetchImageAndCookie(final HttpClient client, final GetMethod method) throws IOException {
        final int result = reallyFetchImage(client, method);
        if (result == 403) {
            fetchCookie(client);
            return reallyFetchImage(client, method);
        } else
            return result;
    }

    static int reallyFetchImage(final HttpClient client, final GetMethod method) throws IOException {
        client.getState().clearCookies();
        final String[][] cookies = GeoportailTileFetcher.cookies;
        for (final String[] cookie : cookies) {
            client.getState().addCookie(new Cookie("visu-2d.geoportail.fr", cookie[0], cookie[1], "/", -1, false));
        }
        final int result = client.executeMethod(method);
        if (result == 200)
            method.getResponseBody();
        method.releaseConnection();
        return result;
    }

    static void fetchCookie(final HttpClient client) {
        final GetMethod getCookie = new GetMethod("http://www.geoportail.fr/imgs/visu/empty.gif");
        getCookie.setRequestHeader("User-Agent", UA_STRING);
        getCookie.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        try {
            client.executeMethod(getCookie);
            getCookie.releaseConnection();
            final Cookie[] fetchedCookies = client.getState().getCookies();
            storeCookies(fetchedCookies);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void storeCookies(final Cookie[] fetchedCookies) {
        final String[][] cookies = new String[fetchedCookies.length][];
        for (int i = 0; i < fetchedCookies.length; i++) {
            final Cookie cookie = fetchedCookies[i];
            cookies[i] = new String[]{cookie.getName(), cookie.getValue()};
        }
        GeoportailTileFetcher.cookies = cookies;
    }

    static String encodeTile(final String prefix, final int x, final int y, final int zoom) {
        final char sign = SIGN_TABLE[(x * y >= 0 ? x < 0 ? 3 : 0 : x < 0 ? 2 : 1)];
        final String encodedX = geoEncode(x, "");
        final String encodedY = geoEncode(y, "");
        return prefix + SHORT_TABLE[zoom] + sign + SHORT_TABLE[encodedX.length()] + encodedX + encodedY;
    }

    static String geoEncode(final int number, final String suffix) {
        final int abs = Math.abs(number);
        final int base = 62;
        final int r = abs % base;
        final String currentValue = LONG_TABLE[r] + suffix;
        return abs - r == 0 ? currentValue : geoEncode((abs - r) / base, currentValue);
    }

    public TileData fetchTile(final String name) {
        return getTile("http://visu-2d.geoportail.fr/geoweb/" + name);
    }

    public TileData fetchTile(final String prefix, int zoom, int x, int y, String suffix) {
        final String s = "maps" + encodeTile(prefix, x, y, zoom) + suffix;
        return fetchTile(s);
    }
}
