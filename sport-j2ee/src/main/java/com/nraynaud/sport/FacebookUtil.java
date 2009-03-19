package com.nraynaud.sport;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookWebappHelper;
import com.google.code.facebookapi.IFacebookRestClient;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FacebookUtil {
    public static final String API_KEY = System.getenv("FACEBOOK_PUBLIC");
    public static final String SECRET_KEY = System.getenv("FACEBOOK_PRIVATE");

    private FacebookUtil() {
    }

    public static IFacebookRestClient<Document> getClient(final HttpServletRequest request,
                                                          final HttpServletResponse response) {
        return FacebookWebappHelper.newInstanceXml(request, response, API_KEY, SECRET_KEY).get_api_client();
    }

    public static Long getFacebookUserId(final HttpServletRequest request, final HttpServletResponse response) throws
            FacebookException {
        final IFacebookRestClient<Document> facebookClient = getClient(request, response);
        return facebookClient.users_getLoggedInUser();
    }
}
