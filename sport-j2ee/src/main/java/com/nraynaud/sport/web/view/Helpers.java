package com.nraynaud.sport.web.view;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.TextUtils;

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

    public static String property(final String property) {
        return (String) ActionContext.getContext().getValueStack().findValue(property, String.class);
    }
}
