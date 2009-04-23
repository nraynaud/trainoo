package com.nraynaud.sport.web.view;

import com.nraynaud.sport.Helper;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserString;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.data.DisciplineData;
import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.SportActionMapper;
import com.nraynaud.sport.web.SportRequest;
import com.nraynaud.sport.web.URIValidator;
import com.opensymphony.xwork2.ActionContext;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.*;

public class Helpers {
    private static final Random RNG = new Random();
    public static final SportActionMapper MAPPER = new SportActionMapper();

    private static final String STATIC_CONTENT_PREFIX;
    public static final Comparator<DisciplineData<DisciplineData.Count>> DISCIPLNE_DISTANCE_COMPARATOR = new Comparator<DisciplineData<DisciplineData.Count>>() {
        public int compare(final DisciplineData<DisciplineData.Count> o1,
                           final DisciplineData<DisciplineData.Count> o2) {
            final long diff = o2.data.count.longValue() - o1.data.count.longValue();
            if (diff != 0)
                return (int) diff < 0 ? -1 : 1;
            return o1.discipline.nonEscaped().compareTo(o2.discipline.nonEscaped());
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

    public static String firstNonNull(final String... strings) {
        for (final String string : strings)
            if (string != null)
                return string;
        return null;
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

    public static boolean isLogged() {
        return SportRequest.getSportRequest().isLogged();
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

    public static PrivateMessageFormConfig privateFormConfig(final Workout workout, final User user) {
        return new PrivateMessageFormConfig(user.getName(), workout);
    }

    public static String signupUrl(final String text) {
        final String from = StackUtil.fromActionOrCurrent();
        return link("/", "signup", text, null, "fromAction", from);
    }

    public static String loginUrl(final String text) {
        final String from = StackUtil.fromActionOrCurrent();
        return link("/", "login", text, null, "fromAction", from);
    }

    public static String getFirstValue(final String key) {
        final String[] val = (String[]) ActionContext.getContext().getParameters().get(key);
        if (val != null && val.length > 0)
            return val[0];
        return null;
    }

    public static String link(final String namespace, final String action, final String content,
                              final String title, final String... params) {
        final String finalUrl = createUrl(namespace, action, params);
        return anchorTag(content, finalUrl, title);
    }

    public static String createUrl(final String namespace, final String action, final String... params) {
        final String urlPrefix = actionUrl(namespace, action);
        final String query;
        if (params.length > 0) {
            final StringBuilder getParams = new StringBuilder(20);
            getParams.append("?");
            for (int i = 0; i < params.length; i += 2) {
                if (i != 0)
                    getParams.append("&amp;");
                pushParam(getParams, params[i], params[i + 1]);
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

    public static String anchorTag(final String content, final String url, final String title) {
        final String titlePart = title != null ? "title='" + title + '\'' : "";
        return "<a " + titlePart + " href='" + url + "'>" + content + "</a>";
    }

    public static String linkCurrenUrlWithoutParam(final String content, final String excludedKey) {
        return linkCurrenUrlWithAndWithoutParams(content, excludedKey);
    }

    public static String linkCurrenUrlWithParams(final String content, final String... params) {
        return linkCurrenUrlWithAndWithoutParams(content, null, params);
    }

    @SuppressWarnings({"unchecked"})
    public static String linkCurrenUrlWithAndWithoutParams(final String content, final String excludedKey,
                                                           final String... params) {
        final Map<String, String> newParams = new HashMap<String, String>();
        final String url = currentUrlLinkWithAndWithoutParams(excludedKey, newParams, params);
        return anchorTag(content, url, null);
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
        for (int i = 0; i < params.length; i += 2) {
            if (i == 0)
                url.append('?');
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
            url.append(URLEncoder.encode(key, "UTF-8")).append('=').append(URLEncoder.encode(value, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String pluralize(final long count, final String one, final String various) {
        return pluralize(count, "", one, various);
    }

    public static String pluralize(final long count, final String none, final String one, final String various) {
        return count <= 0 ? none : count == 1 ? one : various;
    }

    public static String escaped(final UserString string) {
        return Helper.escaped(string);
    }

    public static String bibLink(final User user, final int maxLength) {
        final UserString fullName = user.getName();
        final String shortName = shortString(user.getName(), maxLength);
        return link("/bib", "", shortName, "Voir le dossard de " + escaped(fullName), "id",
                user.getId().toString());
    }

    public static String shortString(final UserString userString, final int maxLength) {
        final String nonEscaped = userString.nonEscaped();
        return nonEscaped.length() > maxLength ? Helper.escaped(nonEscaped.substring(0, maxLength - 2))
                + "…" : Helper.escaped(nonEscaped);
    }

    public static String shortSpan(final UserString userString, final int maxLength) {
        return userString == null ? "" : "<span title='" + escaped(userString) + "'>" + shortString(userString,
                maxLength)
                + "</span>";
    }

    /**
     * rewriting for static resources
     *
     * @param path the path, with the leading /
     */
    public static String stat(final String path) {
        try {
            final URL resource = ServletActionContext.getServletContext().getResource(path);
            final URLConnection connection = resource.openConnection();
            final String suffix = String.valueOf(connection.getLastModified());
            return STATIC_CONTENT_PREFIX + path + "?" + suffix;
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String anchor(final String content, final String url) {
        return anchorTag(content, url, null);
    }

    public static String joinNames(final Collection<User> participans) {
        final StringBuilder participantsCollector = new StringBuilder();
        for (final User user : participans)
            participantsCollector.append(", ").append(escaped(user.getName()));
        return participantsCollector.substring(2);
    }

    public static String selectComponent(final String name, final String id, final Iterable<String> values,
                                         final Iterable<String> labels, final String selectedValue) {
        final String prefix = "<select name=\"" + name + "\" id=\"" + id + "\">\n";
        final String suffix = "</select>\n";
        final StringBuilder builder = new StringBuilder(50);
        final Iterator<String> labelIterator = labels.iterator();
        for (final String value : values) {
            final String label = labelIterator.next();
            final boolean selected = value.equals(selectedValue);
            builder.append("<option value=\"").append(value).append("\"")
                    .append(selected ? " selected=\"selected\"" : "").append(">")
                    .append(label).append("</option>\n");
        }
        return prefix + builder + suffix;
    }

    public static String textArea(final String id, final String name, final String content) {
        return "<textarea id='" + id + "' name='" + name + "' cols='10' rows='10'>" + content + "</textarea>";
    }

    public static String uniqueId(final String prefix) {
        return prefix + RNG.nextInt();
    }

    public static String createUrl(final ActionDetail action) {
        final List<String> accumulator = new ArrayList<String>(action.parameters.size() * 2);
        for (final Map.Entry<String, String[]> parameter : action.parameters.entrySet()) {
            accumulator.add(parameter.getKey());
            accumulator.add(parameter.getValue()[0]);
        }
        return createUrl(action.namespace, action.name, accumulator.toArray(new String[accumulator.size()]));
    }
}
