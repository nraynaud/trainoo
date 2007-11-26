package com.nraynaud.sport.web;

import com.opensymphony.xwork2.config.ConfigurationManager;
import com.opensymphony.xwork2.config.entities.PackageConfig;
import org.apache.struts2.RequestUtils;
import org.apache.struts2.dispatcher.mapper.ActionMapping;
import org.apache.struts2.dispatcher.mapper.Restful2ActionMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class SportActionMapper extends Restful2ActionMapper {
    private static final Comparator<NamespaceData> NAMESPACE_COMPARATOR = new Comparator<NamespaceData>() {
        public int compare(final NamespaceData o1, final NamespaceData o2) {
            return o2.getPrefix().length() - o1.getPrefix().length();
        }
    };
    private static final Pattern SLASH_PATTERN = Pattern.compile("/");

    @SuppressWarnings({"unchecked"})
    public ActionMapping getMapping(final HttpServletRequest request, final ConfigurationManager configManager) {
        final String uri = getUri(request);
        if (uri.startsWith("/struts"))
            return null;
        if (uri.startsWith("/static"))
            return null;
        final Map<String, PackageConfig> currentConfig = configManager.getConfiguration().getPackageConfigs();
        final ActionMapping mapping = super.getMapping(request, configManager);
        extractNamespaceAndName(uri, currentConfig, mapping, request.getMethod());

        final String name = mapping.getName();
        if (name.endsWith("/"))
            mapping.setName(name.substring(0, name.length() - 1));
        /*
        System.out.println("action name: " + mapping.getName());
        System.out.println("namespace    " + mapping.getNamespace());
        System.out.println("method       " + mapping.getMethod());
        System.out.println("params       " + mapping.getParams());
        */
        return mapping;
    }

    static void extractNamespaceAndName(final String uri,
                                        final Map<String, PackageConfig> config,
                                        final ActionMapping mapping,
                                        final String httpMethod) {
        final NamespaceData namespace = extractNamespace(uri, config);
        mapping.setNamespace(namespace.getNamespace());
        final String name = uri.substring(namespace.getPrefix().length());
        final String[] segments = SLASH_PATTERN.split(name);
        mapping.setName(segments[0]);
        if (segments.length > 1)
            mapping.setMethod(segments[1]);
        else
            mapping.setMethod(defaultMethod(httpMethod));
    }

    private static String defaultMethod(final String httpMethod) {
        if (httpMethod.equals("POST"))
            return "create";
        else
            return "index";
    }

    private static NamespaceData extractNamespace(final String uri, final Map<String, PackageConfig> config) {
        for (final NamespaceData namespace : createNamespaceTable(config)) {
            if (uri.startsWith(namespace.getPrefix())) {
                return namespace;
            }
        }
        return new NamespaceData("");
    }

    static Collection<NamespaceData> createNamespaceTable(final Map<String, PackageConfig> configs) {
        final TreeSet<NamespaceData> namespaces = new TreeSet<NamespaceData>(NAMESPACE_COMPARATOR);
        for (final PackageConfig config : configs.values()) {
            namespaces.add(new NamespaceData(config.getNamespace()));
        }
        return namespaces;
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

}
