package com.nraynaud.sport.web.view;

import com.nraynaud.sport.User;
import com.nraynaud.sport.web.SportRequest;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.TextUtils;
import com.opensymphony.xwork2.util.ValueStack;

public class Helpers {
    private Helpers() {
    }

    public static String formatUrl(final String url, final String ifNull) {
        if (url == null)
            return ifNull;
        else {
            return "<a href='"
                    + TextUtils.htmlEncode(url)
                    + "'>"
                    + TextUtils.htmlEncode(url)
                    + "</a>";

        }
    }

    public static String property(final String expression) {
        return (String) stack().findValue(expression, String.class);
    }

    private static ValueStack stack() {
        return ActionContext.getContext().getValueStack();
    }


    public static String escapedProperty(final String expression) {
        return escaped(property(expression));
    }

    public static String escaped(final String string) {
        return TextUtils.htmlEncode(string);
    }

    public static String propertyEscapedOrNull(final String expression, final String ifNull) {
        final String result = property(expression);
        return result == null ? ifNull : TextUtils.htmlEncode(result);
    }

    public static String escapedOrNull(final String string, final String ifNull) {
        return string == null ? ifNull : TextUtils.htmlEncode(string);
    }

    public static User currentUser() {
        return SportRequest.getSportRequest().getSportSession().getUser();
    }

    public static Object top() {
        return stack().peek();
    }

    public static boolean isLogged() {
        return SportRequest.getSportRequest().isLogged();
    }
}
