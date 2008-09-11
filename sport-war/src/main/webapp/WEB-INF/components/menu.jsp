<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="menu">
    <ul class="primary"><%=
    tab("/", "", "Tableau général", "first", "current currentFirst")
    %><%= tab("/groups", "", "Groupes", "last", "current currentLast")
    %>
    </ul>
</div>
<%!
    public static String tab(final String namespace, final String action, final String content, final String classes,
                             final String selectedClasses, final String... params) {
        final boolean current = isCurrentAction(namespace, action);
        final String url = createUrl(namespace, action, params);
        final String selectedPart = current ? " " + selectedClasses : "";
        return "<li class='" + classes + selectedPart + "'><span>" + anchor(content, url) + "</span></li>";
    }
%>
