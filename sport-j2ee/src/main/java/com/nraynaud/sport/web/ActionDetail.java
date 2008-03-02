package com.nraynaud.sport.web;

import com.nraynaud.sport.Helper;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * immutable class
 */
public class ActionDetail {

    private static final Pattern FROM_ACTION_PATTERN = Pattern.compile("(.*?)\\|(.*?)\\?(.*?)");
    private static final Pattern AND_PATTERN = Pattern.compile("&");
    private static final Pattern EQUAL_PATTERN = Pattern.compile("=");
    public final String namespace;
    public final String name;
    public final Map<String, String[]> parameters;
    public final String encodedAction;

    private ActionDetail(final String encodedAction) {
        this.encodedAction = encodedAction;
        final Matcher matcher = FROM_ACTION_PATTERN.matcher(encodedAction);
        if (!matcher.matches())
            throw new RuntimeException("action invalide : '" + encodedAction + "'");
        namespace = matcher.group(1);
        name = matcher.group(2);
        final String[] params = AND_PATTERN.split(matcher.group(3));
        final Map<String, String[]> map = new HashMap<String, String[]>();
        for (final String param : params) {
            if (param.length() > 0) {
                final String[] pair = EQUAL_PATTERN.split(param);
                final String value = pair.length > 1 ? pair[1] : "";
                map.put(pair[0], new String[]{value});
            }
        }
        parameters = map;
    }

    public ActionDetail(final String namespace, final String name, final Map<String, String[]> parameters) {
        this.namespace = namespace;
        this.name = name;
        this.parameters = parameters;
        final StringBuilder encoded = new StringBuilder(40);
        encoded.append(namespace);
        encoded.append("|");
        encoded.append(name);
        encoded.append('?');
        for (final Object o : parameters.entrySet()) {
            final Map.Entry entry = (Map.Entry) o;
            encoded.append(entry.getKey());
            encoded.append('=');
            final Object value = entry.getValue();
            encoded.append(value instanceof String ? value : ((String[]) value)[0]);
            encoded.append('&');
        }
        encodedAction = encoded.toString();
    }

    public String toString() {
        return Helper.escaped(encodedAction);
    }

    /**
     * @param key
     * @return a new ActionDetail the param removed
     */
    public ActionDetail removeParam(final String key) {
        final Map<String, String[]> clone = new HashMap<String, String[]>(parameters);
        clone.remove(key);
        return new ActionDetail(namespace, name, clone);
    }

    /**
     * @return a new ActionDetail the param added
     */
    public ActionDetail addParam(final String key, final String value) {
        final Map<String, String[]> clone = new HashMap<String, String[]>(parameters);
        clone.put(key, new String[]{value});
        return new ActionDetail(namespace, name, clone);
    }

    public static ActionDetail decodeActionDetail(final String encodedAction) {
        return encodedAction == null ? null : new ActionDetail(encodedAction);
    }
}
