package com.nraynaud.sport.web;

import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import org.apache.struts2.RequestUtils;
import org.apache.struts2.dispatcher.mapper.ActionMapper;
import org.apache.struts2.dispatcher.mapper.ActionMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Pattern;

public class SportActionMapper implements ActionMapper {
    private static final Comparator<NamespaceData> NAMESPACE_COMPARATOR = new Comparator<NamespaceData>() {
        public int compare(final NamespaceData o1, final NamespaceData o2) {
            return o2.getPrefix().length() - o1.getPrefix().length();
        }
    };
    private static final Pattern SLASH_PATTERN = Pattern.compile("/");

    public static final ConcurrentMap<Map<String, PackageConfig>, Collection<NamespaceData>> CONFIG_CACHE = new ConcurrentHashMap<Map<String, PackageConfig>, Collection<NamespaceData>>(
            1);

    @SuppressWarnings({"unchecked"})
    public ActionMapping getMapping(final HttpServletRequest request, final ConfigurationManager configManager) {
        final String uri = getUri(request);
        if (uri.startsWith("/struts"))
            return null;
        if (uri.startsWith("/static"))
            return null;
        final Map<String, PackageConfig> currentConfig = configManager.getConfiguration().getPackageConfigs();
        final ActionMapping mapping = new ActionMapping();
        extractNamespaceAndName(uri, currentConfig, mapping, request.getMethod());

        final String name = mapping.getName();
        if (name.endsWith("/"))
            mapping.setName(name.substring(0, name.length() - 1));
        return mapping;
    }

    static void extractNamespaceAndName(final String uri,
                                        final Map<String, PackageConfig> config,
                                        final ActionMapping mapping,
                                        final String httpMethod) {
        final NamespaceMatching matching = extractNamespace(uri, config);
        mapping.setNamespace(matching.getNamespace());
        final String name = uri.substring(matching.getIndex());
        final String[] segments = SLASH_PATTERN.split(name);
        mapping.setName(segments[0]);
        if (segments.length > 1) {
            mapping.setMethod(segments[1]);
            if (segments.length > 2)
                addParameter(mapping, segments);
        } else
            mapping.setMethod(defaultMethod(httpMethod));
    }

    @SuppressWarnings({"unchecked"})
    private static void addParameter(final ActionMapping mapping, final String[] segments) {
        final Map<String, String> params = mapping.getParams();
        final Map<String, String> copy;
        if (params == null)
            copy = new HashMap<String, String>();
        else
            copy = new HashMap<String, String>(params);
        copy.put("id", segments[2]);
        mapping.setParams(copy);
    }

    private static String defaultMethod(final String httpMethod) {
        if (httpMethod.equals("POST"))
            return "create";
        else
            return "index";
    }

    private static NamespaceMatching extractNamespace(final String uri, final Map<String, PackageConfig> config) {
        for (final NamespaceData namespace : createNamespaceTable(config)) {
            final String prefix = namespace.getPrefix();
            if (uri.startsWith(prefix)) {
                return new NamespaceMatching(namespace.getNamespace(), prefix.length());
            }
        }
        return new NamespaceMatching("", 0);
    }

    static Collection<NamespaceData> createNamespaceTable(final Map<String, PackageConfig> configs) {
        Collection<NamespaceData> namespaces = CONFIG_CACHE.get(configs);
        if (namespaces == null) {
            namespaces = new TreeSet<NamespaceData>(NAMESPACE_COMPARATOR);
            for (final PackageConfig config : configs.values()) {
                namespaces.add(new NamespaceData(config.getNamespace()));
            }
            if (CONFIG_CACHE.putIfAbsent(configs, namespaces) != namespaces)
                System.out.println("config cache update ! new size :" + CONFIG_CACHE.size());
        }
        return namespaces;
    }

    static class NamespaceMatching {
        private final String namespace;
        private final int index;

        NamespaceMatching(final String namespace, final int index) {
            this.namespace = namespace;
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public String getNamespace() {
            return namespace;
        }
    }

    static class NamespaceData {
        private final String prefix;
        private final String namespace;

        @SuppressWarnings({"StringConcatenation"})
        private NamespaceData(final String namespace) {
            this.namespace = namespace;
            if (namespace.length() > 0)
                if (namespace.equals("/"))
                    prefix = namespace;
                else
                    prefix = namespace + '/';
            else
                prefix = namespace;
        }

        public String getPrefix() {
            return prefix;
        }

        public String getNamespace() {
            return namespace;
        }

        public boolean equals(final Object o) {
            if (this == o) return true;
            if (!(o instanceof NamespaceData)) return false;
            final NamespaceData that = (NamespaceData) o;
            return namespace.equals(that.namespace);

        }

        public int hashCode() {
            return namespace.hashCode();
        }
    }

    public static String getUri(final HttpServletRequest request) {
        final String uri = (String) request.getAttribute("javax.servlet.include.servlet_path");
        if (uri != null) {
            return uri;
        }
        final String uri1 = RequestUtils.getServletPath(request);
        if (uri1 != null && !"".equals(uri1)) {
            return uri1;
        }
        return request.getRequestURI().substring(request.getContextPath().length());
    }


    public String getUriFromActionMapping(final ActionMapping mapping) {
        final StringBuilder uri = new StringBuilder(20);
        uri.append(mapping.getNamespace());
        if (!"/".equals(mapping.getNamespace())) {
            uri.append('/');
        }
        final String name = mapping.getName();
        uri.append(name);

        final String method = mapping.getMethod();
        if (!"index".equals(method) && !"create".equals(method))
            if (null != method && method.length() != 0) {
                uri.append('/').append(method);
            }
        return uri.toString();
    }
}
