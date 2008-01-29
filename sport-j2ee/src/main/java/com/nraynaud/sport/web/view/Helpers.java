package com.nraynaud.sport.web.view;

import com.nraynaud.sport.User;
import com.nraynaud.sport.web.SportRequest;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.CreateIfNull;
import com.opensymphony.xwork2.util.TextUtils;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Include;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Helpers {
    private static final String HEX_CHARS = "0123456789ABCDEF";
    private static final String OVERRIDES_KEY = "overrides";

    private Helpers() {
    }

    public static String formatUrl(final String url, final String ifNull) {
        if (url == null)
            return ifNull;
        else {
            final StringBuilder builder = new StringBuilder();
            builder.append("<a href='");
            escape(url, builder);
            builder.append("'>");
            escape(url, builder);
            builder.append("</a>");
            return builder.toString();
        }
    }

    public static String stringProperty(final String expression) {
        return (String) stack().findValue(expression, String.class);
    }

    public static Object property(final String expression) {
        return stack().findValue(expression, Object.class);
    }

    public static Object parameter(final String expression) {
        return property("parameters." + expression);
    }

    /**
     * null is false
     */
    public static boolean boolParam(final String expression) {
        final Boolean param = (Boolean) stack().findValue("parameters." + expression, Boolean.class);
        return param != null && param.booleanValue();
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
        return result == null ? ifNull : escaped(result);
    }

    public static String escapedOrNull(final String string, final String ifNull) {
        return string == null ? ifNull : escaped(string);
    }

    public static String escapedOrNullmultilines(final String string, final String ifNull) {
        return string == null ? ifNull : multilineText(string);
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

    public static void call(final PageContext context,
                            final String template,
                            final Object stackTop,
                            final Object... arguments) throws Exception {
        push(new Object() {
            public Map<String, Object> parameters = new HashMap<String, Object>(1) {
                public Object put(final String key, final Object value) {
                    return super.put(key, value);
                }
            };

            {
                for (int i = 0; i < arguments.length; i += 2) {
                    final Object arg = arguments[i + 1];
                    final Object value = arg instanceof String ? stack().findValue((String) arg) : arg;
                    parameters.put((String) arguments[i], value);
                }
            }

            @CreateIfNull(false)
            Map<String, Object> getParameters() {
                return parameters;
            }
        });
        try {
            push(stackTop);
            try {
                call(context, template);
            } finally {
                pop();
            }
        } finally {
            pop();
        }
    }

    public static void call(final PageContext context, final String template) throws Exception {
        saveAndUnplugOverrides();
        try {
            final HttpServletResponse httpServletResponse = (HttpServletResponse) context.getResponse();
            Include.include("/template/simple/" + template, context.getOut(), context.getRequest(),
                    httpServletResponse);
        } finally {
            unplugOverridesIfNecessary();
        }
    }

    public static void allowOverrides() {
        final Map overrides = (Map) ActionContext.getContext().get(OVERRIDES_KEY);
        if (overrides != null)
            stack().setExprOverrides(overrides);
    }

    private static void unplugOverridesIfNecessary() {
        final Map exprOverrides = stack().getExprOverrides();
        if (exprOverrides != null)
            exprOverrides.clear();
    }

    private static void saveAndUnplugOverrides() {
        final ActionContext context = ActionContext.getContext();
        final ValueStack stack = context.getValueStack();
        final Map overrides = stack.getExprOverrides();
        if (overrides != null) {
            final Map overridesCopy = new HashMap(overrides);
            if (context.get(OVERRIDES_KEY) == null)
                context.put(OVERRIDES_KEY, overridesCopy);
            unplugOverridesIfNecessary();
        }
    }

    public static String literal(final String string) {
        return '\'' + string + '\'';
    }
}
