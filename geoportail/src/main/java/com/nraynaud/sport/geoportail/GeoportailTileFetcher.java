package com.nraynaud.sport.geoportail;

import com.nraynaud.sport.geography.TileFetcher;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;

import java.io.IOException;

public class GeoportailTileFetcher implements TileFetcher {

    public static volatile String[][] COOKIES = {};

    static byte[] getTile(final String uri) {
        final HttpClient client = new HttpClient();
        final GetMethod getImage = new GetMethod(uri);
        try {
            getImage.setRequestHeader("User-Agent",
                    "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en-US; rv:1.8.1.13) Gecko/20080311 Firefox/2.0.0.13");
            fetchImageAndCookie(client, getImage);
            return getImage.getResponseBody();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static int fetchImageAndCookie(final HttpClient client, final GetMethod getImage) throws IOException {
        final int result = reallyFetchImage(client, getImage);
        if (result == 403) {
            fetchCookie(client);
            return reallyFetchImage(client, getImage);
        } else
            return result;
    }

    static int reallyFetchImage(final HttpClient client, final GetMethod getImage) throws IOException {
        client.getState().clearCookies();
        final String[][] cookies = COOKIES;
        for (final String[] cookie : cookies) {
            client.getState().addCookie(new Cookie("visu-2d.geoportail.fr", cookie[0], cookie[1], "/", -1, false));
        }
        final int result = client.executeMethod(getImage);
        if (result == 200)
            getImage.getResponseBody();
        getImage.releaseConnection();
        return result;
    }

    static void fetchCookie(final HttpClient client) {
        final GetMethod getCookie = new GetMethod("http://www.geoportail.fr/imgs/visu/empty.gif");
        getCookie.setRequestHeader("User-Agent",
                "Mozilla/5.0 (Macintosh; U; Intel Mac OS X; en-US; rv:1.8.1.13) Gecko/20080311 Firefox/2.0.0.13");
        getCookie.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
        try {
            client.executeMethod(getCookie);
            getCookie.releaseConnection();
            final Cookie[] fetchedCookies = client.getState().getCookies();
            final String[][] cookies = new String[fetchedCookies.length][];
            for (int i = 0; i < fetchedCookies.length; i++) {
                final Cookie cookie = fetchedCookies[i];
                cookies[i] = new String[]{cookie.getName(), cookie.getValue()};
            }
            COOKIES = cookies;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] fetchTile(final String name) {
        return getTile("http://visu-2d.geoportail.fr/geoweb/" + name);
    }
}
