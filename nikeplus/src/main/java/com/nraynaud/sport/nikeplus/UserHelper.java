package com.nraynaud.sport.nikeplus;

import org.xml.sax.InputSource;

import javax.xml.xpath.XPathExpressionException;
import java.io.File;

public class UserHelper {
    private static final File CACHE_DIRECTORY;
    private static final String NIKE_PAGE = "http://secure-nikeplus.nike.com/nikeplus/v1/services/app/get_user_data.jsp?userID=";

    static {
        final File file = new File(System.getProperty("java.io.tmpdir"), "nikeusers");
        if (!file.exists())
            file.mkdirs();
        CACHE_DIRECTORY = file;
    }

    private UserHelper() {
    }

    public static String getLogin(final String userId) {
        final String url = NIKE_PAGE + userId;
        try {
            final InputSource source = Util.getCachedSource(url,
                    new File(CACHE_DIRECTORY, userId + ".xml"));
            return Util.XPATH.evaluate("/plusService/userOptions/screenName/text()", source);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
