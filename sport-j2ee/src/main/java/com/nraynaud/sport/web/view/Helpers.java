package com.nraynaud.sport.web.view;

import com.nraynaud.sport.Helper;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserString;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.web.SportActionMapper;
import com.nraynaud.sport.web.SportRequest;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.CreateIfNull;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.components.Include;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class Helpers {
    private static final String OVERRIDES_KEY = "overrides";
    public static final SportActionMapper MAPPER = new SportActionMapper();

    private Helpers() {
    }

    public static String formatUrl(final UserString url, final String ifNull) {
        if (url == null)
            return ifNull;
        else {
            final StringBuilder builder = new StringBuilder();
            builder.append("<a href='");
            builder.append(url.toString());
            builder.append("'>");
            builder.append(url.toString());
            builder.append("</a>");
            return builder.toString();
        }
    }

    public static String stringProperty(final String expression) {
        return (String) stack().findValue(expression, String.class);
    }

    public static UserString userStringProperty(final String expression) {
        return (UserString) stack().findValue(expression, UserString.class);
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
        return Helper.escaped(stringProperty(expression));
    }

    public static String propertyEscapedOrNull(final String expression, final String ifNull) {
        final String result = stringProperty(expression);
        return result == null ? ifNull : Helper.escaped(result);
    }

    public static String escapedOrNull(final String string, final String ifNull) {
        return string == null ? ifNull : Helper.escaped(string);
    }

    public static String escapedOrNullmultilines(final UserString string, final String ifNull) {
        return string == null ? ifNull : multilineText(string);
    }

    public static User currentUser() {
        final SportRequest request = SportRequest.getSportRequest();
        return request.isLogged() ? request.getSportSession().getUser() : null;
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

    public static String multilineText(final UserString input) {
        final StringBuilder builder = new StringBuilder((int) (input.nonEscaped().length() * 1.2));
        final StringTokenizer tokenizer = new StringTokenizer(input.nonEscaped(), "\n");
        while (tokenizer.hasMoreTokens()) {
            Helper.escape(tokenizer.nextToken(), builder);
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

    public static String literal(final UserString string) {
        return '\'' + string.toString() + '\'';
    }

    public static PrivateMessageFormConfig privateFormConfig(final Workout workout, final User runner) {
        return new PrivateMessageFormConfig(runner.getName(), workout);
    }

    public static String signupUrl(final String text) {
        final String from = stringProperty("fromAction") == null ? stringProperty(
                "actionDescription") : stringProperty("fromAction");
        return selectableUrl("/", "signup", text, "fromAction", from);
    }

    public static String loginUrl(final String text) {
        final String from = stringProperty("fromAction") == null ? stringProperty(
                "actionDescription") : stringProperty("fromAction");
        return selectableUrl("/", "login", text, "fromAction", from);
    }

    public static String getFirstValueEncoded(final String key) {
        final Object val = ActionContext.getContext().getParameters().get(key);
        if (val != null)
            return ((String[]) val)[0];
        return null;
    }

    public static String selectableUrl(final String namespace, final String action, final String content,
                                       final String... params) {
        final ActionMapping mapping = (ActionMapping) ActionContext.getContext().get("struts.actionMapping");
        final String url = MAPPER.getUriFromActionMapping(new ActionMapping(action, namespace, null, null));
        boolean selected = namespace.equals(mapping.getNamespace()) && action.equals(
                mapping.getName());
        final String query;
        if (params.length > 0) {
            final StringBuilder getParams = new StringBuilder(20);
            getParams.append("?");
            for (int i = 0; i < params.length; i += 2) {
                pushParam(getParams.append("&amp;"), params[i], params[i + 1]);
                selected &= params[i + 1].equals(getFirstValueEncoded(params[i]));
            }
            query = getParams.toString();
        } else
            query = "";
        final String finalUrl = url + query;
        return selectableAnchorTag(content, selected, finalUrl);
    }

    private static String selectableAnchorTag(final String content, final boolean selected, final String finalUrl) {
        return "<a " + (selected ? "class='selected'" : "") + " href='" + finalUrl + "'>" + content + "</a>";
    }

    public static String currenUrlWithoutParam(final String content, final String excludedKey) {
        return currenUrlWithAndWithoutParams(content, excludedKey);
    }

    public static String currenUrlWithParams(final String content, final String... params) {
        return currenUrlWithAndWithoutParams(content, null, params);
    }

    public static String currenUrlWithAndWithoutParams(final String content, final String excludedKey,
                                                       final String... params) {
        final ActionMapping mapping = (ActionMapping) ActionContext.getContext().get("struts.actionMapping");
        final String base = MAPPER.getUriFromActionMapping(
                new ActionMapping(mapping.getName(), mapping.getNamespace(), null, null));
        final Map<String, String[]> queryString = ServletActionContext.getRequest().getParameterMap();
        boolean selected = false;
        final StringBuilder url = new StringBuilder(20);
        url.append(base);
        url.append('?');
        final Map<String, String> newParams = new HashMap<String, String>();
        for (int i = 0; i < params.length; i += 2) {
            if (i > 0)
                url.append("&amp;");
            newParams.put(params[i], params[i + 1]);
            pushParam(url, params[i], params[i + 1]);
        }
        if (excludedKey != null)
            newParams.put(excludedKey, "lol");
        for (final Map.Entry<String, String[]> entry : queryString.entrySet()) {
            if (!newParams.containsKey(entry.getKey()))
                pushParam(url.append("&amp;"), entry.getKey(), entry.getValue()[0]);
            else {
                selected |= newParams.get(entry.getKey()).equals(entry.getValue()[0]);
            }
        }
        return selectableAnchorTag(content, selected, url.toString());
    }

    private static void pushParam(final StringBuilder url, final String key, final String value) {
        try {
            url.append(URLEncoder.encode(key, "ISO-8859-1")).append('=').append(URLEncoder.encode(value, "ISO-8859-1"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String pluralize(final int count, final String one, final String various) {
        return count > 1 ? various : one;
    }

    public static String defaultOrUserClass(final UserString string) {
        return string == null ? "serverDefault" : "userSupplied";
    }
}
