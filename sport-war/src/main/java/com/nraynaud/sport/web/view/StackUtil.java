package com.nraynaud.sport.web.view;

import com.nraynaud.sport.Helper;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.data.PaginatedCollection;
import com.nraynaud.sport.formatting.DateHelper;
import com.nraynaud.sport.web.ActionDetail;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.util.CreateIfNull;
import com.opensymphony.xwork2.util.ValueStack;
import org.apache.struts2.components.Include;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import java.util.*;

public class StackUtil {
    public static final String OVERRIDES_KEY = "overrides";
    public static final Comparator<Date> REVERSE_DATE_COMPARATOR = new Comparator<Date>() {
        public int compare(final Date o1, final Date o2) {
            return o2.compareTo(o1);
        }
    };
    public static final PaginatedCollection.Transformer<Workout, TableContent> DEFAULT_WORKOUT_TRANSFORMER = new PaginatedCollection.Transformer<Workout, TableContent>() {
        public TableContent transform(final PaginatedCollection<Workout> paginatedCollection) {
            final Map<Date, Collection<Workout>> theMap = new TreeMap<Date, Collection<Workout>>(
                    REVERSE_DATE_COMPARATOR);
            for (final Workout workout : paginatedCollection) {
                final Date date = workout.getDate();
                final Collection<Workout> workoutCollection = theMap.get(date);
                if (workoutCollection == null) {
                    final Collection<Workout> theCollection = new ArrayList<Workout>();
                    theCollection.add(workout);
                    theMap.put(date, theCollection);
                } else
                    workoutCollection.add(workout);
            }
            final List<TableContent.TableSheet> sheets = new ArrayList<TableContent.TableSheet>(
                    theMap.size());
            for (final Map.Entry<Date, Collection<Workout>> sheetData : theMap.entrySet()) {
                final String formated = DateHelper.humanizePastDate(sheetData.getKey(),
                        "'Aujourd''hui'", "'Hier'",
                        "'Avant-hier'", "EEEE dd/MM");
                sheets.add(new TableContent.TableSheet(formated, sheetData.getValue(), new TableContent.RowRenderer() {
                    public void render(final Workout workout, final PageContext context) throws Exception {
                        call(context, "workoutLineElements.jsp", workout, "withUser", true);
                    }
                }));
            }
            return new TableContent(sheets);
        }
    };
    public static final TableContent.RowRenderer SECONDARY_TABLE_RENDERER = new TableContent.RowRenderer() {
        public void render(final Workout workout, final PageContext context) throws Exception {
            context.getOut()
                    .append("<span class='date'>")
                    .append(DateHelper.printDate("EE dd/MM", workout.getDate()))
                    .append("</span>");
            call(context, "workoutLineElements.jsp", workout);
        }
    };

    private StackUtil() {
    }

    public static String stringProperty(final String expression) {
        return cast(stack().findValue(expression, String.class));
    }

    @SuppressWarnings({"unchecked"})
    public static <T> T cast(final Object value) {
        return (T) value;
    }

    public static <T> T property(final String expression) {
        return StackUtil.<T>cast(stack().findValue(expression));
    }

    public static <T> T parameter(final String expression) {
        return StackUtil.<T>property("parameters." + expression);
    }

    /**
     * null is false
     */
    public static boolean boolParam(final String expression) {
        final Object aBoolean = parameter(expression);
        final Boolean param = (Boolean) aBoolean;
        return param != null && param.booleanValue();
    }

    public static String stringParam(final String expression) {
        return parameter(expression);
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

    public static <T> T top() {
        return StackUtil.<T>cast(stack().peek());
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

    /**
     * used for login as an example, it forwards you to its previous action if the login was successful.
     */
    public static String fromActionOrCurrent() {
        final String fromAction = stringProperty("fromAction");
        return fromAction == null ? currentAction().toString() : fromAction;
    }

    public static ActionDetail currentAction() {
        return property("actionDescription");
    }

    public static PaginatedCollection.Transformer<Workout, TableContent> oneSheetContentTransformer(
            final String sheetLabel) {
        return new PaginatedCollection.Transformer<Workout, TableContent>() {
            public TableContent transform(final PaginatedCollection<Workout> collection) {
                final TableContent.TableSheet sheet = new TableContent.TableSheet(sheetLabel, collection,
                        SECONDARY_TABLE_RENDERER);
                return new TableContent(Collections.singletonList(sheet));
            }
        };
    }
}
