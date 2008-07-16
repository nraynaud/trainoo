package com.nraynaud.sport.web.view;

import com.nraynaud.sport.Helper;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserString;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.data.PaginatedCollection;
import com.nraynaud.sport.web.DateHelper;
import com.nraynaud.sport.web.SportActionMapper;
import com.nraynaud.sport.web.SportRequest;
import com.nraynaud.sport.web.URIValidator;
import com.nraynaud.sport.web.converter.DistanceConverter;
import com.nraynaud.sport.web.converter.DurationConverter;
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
import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.*;

public class Helpers {
    private static final String OVERRIDES_KEY = "overrides";
    public static final SportActionMapper MAPPER = new SportActionMapper();

    private static final String STATIC_CONTENT_PREFIX;
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
                    .append(com.nraynaud.sport.web.DateHelper.printDate("EE dd/MM", workout.getDate()))
                    .append("</span>");
            call(context, "workoutLineElements.jsp", workout);
        }
    };
    public static final PaginatedCollection.Transformer<Workout, TableContent> ONE_SHEET_CONTENT_TRANSFORMER = new PaginatedCollection.Transformer<Workout, TableContent>() {
        public TableContent transform(final PaginatedCollection<Workout> collection) {
            final TableContent.TableSheet sheet = new TableContent.TableSheet("", collection, SECONDARY_TABLE_RENDERER);
            return new TableContent(Collections.singletonList(sheet));
        }
    };

    static {
        final String envVar = System.getenv("SPORT_CONTENT_PREFIX");
        final String property = System.getProperty("com.nraynaud.sport.staticprefix");
        STATIC_CONTENT_PREFIX = envVar != null ? envVar : property != null ? property : "";
    }

    private Helpers() {
    }

    public static String formatUrl(final UserString url, final String ifNull) {
        if (url == null)
            return ifNull;
        else {
            final StringBuilder builder = new StringBuilder();
            final String stringUrl = url.toString();
            formatUrl(builder, stringUrl);
            return builder.toString();
        }
    }

    private static void formatUrl(final StringBuilder builder, final String stringUrl) {
        builder.append("<a href='");
        builder.append(stringUrl);
        builder.append("'>");
        builder.append(stringUrl);
        builder.append("</a>");
    }

    public static String toTitleCase(final String string) {
        final StringBuffer accum = new StringBuffer();
        final BreakIterator wordIterator = BreakIterator.getWordInstance();
        wordIterator.setText(string);
        int start = wordIterator.first();
        for (int end = wordIterator.next(); end != BreakIterator.DONE; start = end, end = wordIterator.next()) {
            accum.append(string.substring(start, start + 1).toUpperCase());
            accum.append(string.substring(start + 1, end));
        }
        return accum.toString();
    }

    public static String firstNonNull(final String... strings) {
        for (final String string : strings) {
            if (string != null) {
                return string;
            }
        }
        return null;
    }

    public static String stringProperty(final String expression) {
        return property(expression, String.class);
    }

    public static UserString userStringProperty(final String expression) {
        return property(expression, UserString.class);
    }

    @SuppressWarnings({"UnusedDeclaration", "unchecked"})
    public static <T> T cast(final Object value, final Class<T> type) {
        return (T) value;
    }

    public static <T> T property(final String expression, final Class<T> type) {
        return cast(stack().findValue(expression, type), type);
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

    public static <T> T top(final Class<T> type) {
        return cast(stack().peek(), type);
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
        final StringTokenizer lineTokenizer = new StringTokenizer(input.nonEscaped(), "\n");
        while (lineTokenizer.hasMoreTokens()) {
            final String line = lineTokenizer.nextToken();
            final StringTokenizer wordTokenizer = new StringTokenizer(line);
            while (wordTokenizer.hasMoreTokens()) {
                final String word = wordTokenizer.nextToken();
                if (URIValidator.verifyUrI(word))
                    formatUrl(builder, word);
                else
                    Helper.escape(word, builder);
                if (wordTokenizer.hasMoreTokens())
                    builder.append(' ');
            }
            if (lineTokenizer.hasMoreTokens())
                builder.append("<br>");
        }
        return builder.toString();
    }

    public static <T, U> void paginate(final PageContext context,
                                       final String template,
                                       final PaginationView<T, U> stackTop,
                                       final Object... arguments) throws Exception {
        context.getOut().append("<div class=\"pagination\">");
        try {
            call(context, template, stackTop.transformer.transform(stackTop.collection), arguments);
            call(context, "paginationButtons.jsp", stackTop);
        } finally {
            context.getOut().append("</div>");
        }
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
        final Map overrides = (Map) ActionContext.getContext().get(OVERRIDES_KEY);
        if (overrides != null)
            stack().setExprOverrides(overrides);
    }

    public static void disAllowOverrides() {
        unplugOverridesIfNecessary();
    }

    private static void unplugOverridesIfNecessary() {
        final Map exprOverrides = stack().getExprOverrides();
        if (exprOverrides != null)
            exprOverrides.clear();
    }

    @SuppressWarnings({"unchecked"})
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

    public static String literal(final UserString string) {
        return '\'' + string.toString() + '\'';
    }

    public static PrivateMessageFormConfig privateFormConfig(final Workout workout, final User user) {
        return new PrivateMessageFormConfig(user.getName(), workout);
    }

    public static String signupUrl(final String text) {
        final String from = findFromAction();
        return selectableLink("/", "signup", text, null, "fromAction", from);
    }

    public static String loginUrl(final String text) {
        final String from = findFromAction();
        return selectableLink("/", "login", text, null, "fromAction", from);
    }

    public static String findFromAction() {
        return stringProperty("fromAction") == null ? stringProperty("actionDescription") : stringProperty(
                "fromAction");
    }

    public static String getFirstValue(final String key) {
        final String[] val = (String[]) ActionContext.getContext().getParameters().get(key);
        if (val != null && val.length > 0)
            return val[0];
        return null;
    }

    public static String selectableLink(final String namespace, final String action, final String content,
                                        final String title, final String... params) {
        boolean selected = isCurrentAction(namespace, action);
        final String finalUrl = createUrl(namespace, action, params);
        for (int i = 0; i < params.length; i += 2) {
            selected &= params[i + 1].equals(getFirstValue(params[i]));
        }
        return selectableAnchorTag(content, finalUrl, selected, title);
    }

    public static String createUrl(final String namespace, final String action, final String... params) {
        final String urlPrefix = actionUrl(namespace, action);
        final String query;
        if (params.length > 0) {
            final StringBuilder getParams = new StringBuilder(20);
            getParams.append("?");
            for (int i = 0; i < params.length; i += 2) {
                pushParam(getParams.append("&amp;"), params[i], params[i + 1]);
            }
            query = getParams.toString();
        } else
            query = "";
        return urlPrefix + query;
    }

    public static boolean isCurrentAction(final String namespace, final String action) {
        final ActionMapping mapping = (ActionMapping) ActionContext.getContext().get("struts.actionMapping");
        return namespace.equals(mapping.getNamespace()) && action.equals(mapping.getName());
    }

    public static String actionUrl(final String namespace, final String action) {
        return MAPPER.getUriFromActionMapping(new ActionMapping(action, namespace, null, null));
    }

    public static String selectableAnchorTag(final String content, final String url, final boolean selected,
                                             final String title) {
        final String selectedPart = selected ? "class='selected'" : "";
        final String titlePart = title != null ? "title='" + title + '\'' : "";
        return "<a " + selectedPart + titlePart + " href='" + url + "'>" + content + "</a>";
    }

    public static String linkCurrenUrlWithoutParam(final String content, final String excludedKey) {
        return linkCurrenUrlWithAndWithoutParams(content, false, excludedKey);
    }

    public static String linkCurrenUrlWithParams(final String content, final boolean selectable,
                                                 final String... params) {
        return linkCurrenUrlWithAndWithoutParams(content, selectable, null, params);
    }

    @SuppressWarnings({"unchecked"})
    public static String linkCurrenUrlWithAndWithoutParams(final String content, final boolean selectable,
                                                           final String excludedKey,
                                                           final String... params) {
        final Map<String, String> newParams = new HashMap<String, String>();
        boolean selected = false;
        final Map<String, String[]> queryString = ServletActionContext.getRequest().getParameterMap();
        final String url = currentUrlLinkWithAndWithoutParams(excludedKey, newParams, params);
        for (final Map.Entry<String, String[]> queries : queryString.entrySet())
            if (newParams.containsKey(queries.getKey())) {
                selected |= newParams.get(queries.getKey()).equals(queries.getValue()[0]);
            }
        return selectableAnchorTag(content, url, selectable && selected, null);
    }

    @SuppressWarnings({"unchecked"})
    public static String currentUrlLinkWithAndWithoutParams(final String excludedKey,
                                                            final Map<String, String> newParams,
                                                            final String... params) {
        final ActionMapping mapping = (ActionMapping) ActionContext.getContext().get("struts.actionMapping");
        final Map<String, String[]> queryString = ServletActionContext.getRequest().getParameterMap();
        final String base = actionUrl(mapping.getNamespace(), mapping.getName());
        final StringBuilder url = new StringBuilder(20);
        url.append(base);
        url.append('?');
        for (int i = 0; i < params.length; i += 2) {
            if (i > 0)
                url.append("&amp;");
            newParams.put(params[i], params[i + 1]);
            pushParam(url, params[i], params[i + 1]);
        }
        if (excludedKey != null)
            newParams.put(excludedKey, "lol");
        for (final Map.Entry<String, String[]> queries : queryString.entrySet())
            if (!newParams.containsKey(queries.getKey()))
                pushParam(url.append("&amp;"), queries.getKey(), queries.getValue()[0]);
        return url.toString();
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
        return string == null ? "serverDefault" : "userInteresting";
    }

    public static String escaped(final UserString string) {
        return Helper.escaped(string);
    }

    public static String bibLink(final User user) {
        final UserString fullName = user.getName();
        final String shortName = shortString(user.getName());
        return selectableLink("/bib", "", shortName, "Voir le dossard de " + escaped(fullName), "id",
                user.getId().toString());
    }

    public static String shortString(final UserString userString) {
        final String nonEscaped = userString.nonEscaped();
        return nonEscaped.length() > 10 ? Helper.escaped(nonEscaped.substring(0, 7))
                + "…" : Helper.escaped(nonEscaped);
    }

    public static String shortSpan(final UserString userString) {
        return userString == null ? "" : "<span title='" + escaped(userString) + "'>" + shortString(userString)
                + "</span>";
    }

    public static String formatDistance(final Double distance, final String ifNull) {
        return distance != null ? DistanceConverter.formatDistance(distance) + "km" : ifNull;
    }

    public static String formatDuration(final Long duration, final String ifNull) {
        return duration != null ? DurationConverter.formatDuration(duration, "h", "\'", "''") : ifNull;
    }

    /**
     * rewriting for static resources
     */
    public static String stat(final String path) {
        return STATIC_CONTENT_PREFIX + path;
    }

    public static String formatWorkoutDate(final Date date) {
        final int thisYear = new GregorianCalendar().get(Calendar.YEAR);
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        if (thisYear == calendar.get(Calendar.YEAR))
            return new SimpleDateFormat("E dd/MM", Locale.FRANCE).format(date);
        else
            return new SimpleDateFormat("E dd/MM/y", Locale.FRANCE).format(date);
    }

    public static String anchor(final String content, final String url) {
        return selectableAnchorTag(content, url, false, null);
    }

    public static String joinNames(final Collection<User> participans) {
        final StringBuilder participantsCollector = new StringBuilder();
        for (final User user : participans)
            participantsCollector.append(", ").append(escaped(user.getName()));
        return participantsCollector.substring(2);
    }
}
