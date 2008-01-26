package com.nraynaud.sport.web.view;

import com.nraynaud.sport.User;
import com.nraynaud.sport.web.SportRequest;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.TextUtils;
import com.opensymphony.xwork2.util.ValueStack;

import java.util.StringTokenizer;

public class Helpers {
    private static final String HEX_CHARS = "0123456789ABCDEF";

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

    public static String stringProperty(final String expression) {
        return (String) stack().findValue(expression, String.class);
    }

    public static Object property(final String expression) {
        return stack().findValue(expression, Object.class);
    }

    private static ValueStack stack() {
        return ActionContext.getContext().getValueStack();
    }


    public static String escapedProperty(final String expression) {
        return escaped(stringProperty(expression));
    }

    public static String escaped(final String string) {
        final String s = TextUtils.noNull(string);
        final StringBuilder str = new StringBuilder();

        escape(s, str);

        return str.toString();
    }

    private static void escape(final String input, final StringBuilder collector) {
        for (int j = 0; j < input.length(); j++) {
            final char c = input.charAt(j);

            // encode standard ASCII characters into HTML entities where needed
            if (c < '\200') {
                switch (c) {
                    case '"':
                        collector.append("&quot;");

                        break;

                    case '&':
                        collector.append("&amp;");

                        break;

                    case '<':
                        collector.append("&lt;");

                        break;

                    case '>':
                        collector.append("&gt;");

                        break;

                    default:
                        collector.append(c);
                }
            }
            // encode 'ugly' characters (ie Word "curvy" quotes etc)
            else if (c < '\377') {
                final int a = c % 16;
                final int b = (c - a) / 16;
                final String hex = "" + HEX_CHARS.charAt(b) + HEX_CHARS.charAt(a);
                collector.append("&#x").append(hex).append(";");
            }
            //add other characters back in - to handle charactersets
            //other than ascii
            else {
                collector.append(c);
            }
        }
    }

    public static String propertyEscapedOrNull(final String expression, final String ifNull) {
        final String result = stringProperty(expression);
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

    public static void push(final Object object) {
        stack().push(object);
    }

    public static Object pop() {
        return stack().pop();
    }

    public static String multilineText(final String input) {
        final StringBuilder builder = new StringBuilder((int) (input.length() * 1.2));
        final StringTokenizer tokenizer = new StringTokenizer(input, "\n");
        while (tokenizer.hasMoreTokens()) {
            escape(tokenizer.nextToken(), builder);
            builder.append("<br>");
        }
        return builder.toString();
    }
}
