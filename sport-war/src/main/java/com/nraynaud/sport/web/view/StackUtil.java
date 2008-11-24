package com.nraynaud.sport.web.view;

import com.nraynaud.sport.Helper;
import com.nraynaud.sport.UserString;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.CreateIfNull;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Include;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StackUtil {
    public static final String OVERRIDES_KEY = "overrides";

    private StackUtil() {
    }

    public static String stringProperty(final String expression) {
        return property(expression, String.class);
    }

    public static UserString userStringProperty(final String expression) {
        return property(expression, UserString.class);
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"})
    public static <T> T cast(final Object value, final Class<T> type) {
        return (T) value;
    }

    public static <T> T property(final String expression, final Class<T> type) {
        return cast(stack().findValue(expression, type), type);
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T property(final String expression) {
        return (T) stack().findValue(expression);
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"})
    public static <T> List<T> listProperty(final String name, final Class<T> elementType) {
        return property(name, List.class);
    }

    @SuppressWarnings({"unchecked", "UnusedDeclaration"})
    public static <T> List<T> listProperty(final String name) {
        return property(name, List.class);
    }

    public static <T> T parameter(final String expression, final Class<T> type) {
        return property("parameters." + expression, type);
    }

    /**
     * null is false
     */
    public static boolean boolParam(final String expression) {
        final Boolean param = parameter(expression, Boolean.class);
        return param != null && param.booleanValue();
    }

    public static String stringParam(final String expression) {
        return parameter(expression, String.class);
    }

    public static ValueStack stack() {
        return ActionContext.getContext().getValueStack();
    }

    public static String escapedProperty(final String expression) {
        return Helper.escaped(stringProperty(expression));
    }

    public static String propertyEscapedOrNull(final String expression, final String ifNull) {
        final String result = stringProperty(expression);
        return result == null ? ifNull : Helper.escaped(result);
    }

    public static <T> T top(final Class<T> type) {
        return cast(stack().peek(), type);
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T top() {
        return (T) stack().peek();
    }

    public static void push(final Object object) {
        stack().push(object);
    }

    public static Object pop() {
        return stack().pop();
    }

    public static void call(final PageContext context,
                            final String template,
                            final Object stackTop,
                            final Object... arguments) throws Exception {
        push(new Object() {
            public final Map<String, Object> parameters = new HashMap<String, Object>(1) {
                public Object put(final String key, final Object value) {
                    return super.put(key, value);
                }
            };

            {
                for (int i = 0; i < arguments.length; i += 2) {
                    final Object arg = arguments[i + 1];
                    parameters.put((String) arguments[i], arg);
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
            Include.include("/WEB-INF/components/" + template, context.getOut(), context.getRequest(),
                    httpServletResponse);
        } finally {
            unplugOverridesIfNecessary();
        }
    }

    public static void allowOverrides() {
        final Map<?, ?> overrides = (Map<?, ?>) ActionContext.getContext().get(OVERRIDES_KEY);
        if (overrides != null)
            stack().setExprOverrides(overrides);
    }

    public static void disAllowOverrides() {
        unplugOverridesIfNecessary();
    }

    public static void unplugOverridesIfNecessary() {
        final Map<?, ?> exprOverrides = stack().getExprOverrides();
        if (exprOverrides != null)
            exprOverrides.clear();
    }

    @SuppressWarnings({"unchecked"})
    public static void saveAndUnplugOverrides() {
        final ActionContext context = ActionContext.getContext();
        final ValueStack stack = context.getValueStack();
        final Map<String, Object> overrides = stack.getExprOverrides();
        if (overrides != null) {
            final Map<String, ?> overridesCopy = new HashMap<String, Object>(overrides);
            if (context.get(OVERRIDES_KEY) == null)
                context.put(OVERRIDES_KEY, overridesCopy);
            unplugOverridesIfNecessary();
        }
    }

    public static <T, U> void paginate(final PageContext context,
                                       final String template,
                                       final PaginationView<T, U> stackTop,
                                       final Object... arguments) throws Exception {
        call(context, template, stackTop.transformer.transform(stackTop.collection), arguments);
        call(context, "paginationButtons.jsp", stackTop);
    }
}
