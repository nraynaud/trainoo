package com.nraynaud.sport.nikeplus;

import org.xml.sax.InputSource;

import javax.xml.xpath.XPathExpressionException;
import java.io.IOException;
import java.net.URL;

public class UserHelper {
    private UserHelper() {
    }

    public static String getLogin(final String userId) {
        final String url = "http://secure-nikeplus.nike.com/nikeplus/v1/services/app/get_user_data.jsp?userID="
                + userId;
        try {
            final InputSource source = new InputSource(new URL(url).openStream());
            return XPathUtil.XPATH.evaluate("/plusService/userOptions/screenName/text()", source);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
