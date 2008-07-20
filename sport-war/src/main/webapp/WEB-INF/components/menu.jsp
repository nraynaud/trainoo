<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="menu">
    <ul class="secondary">
        <% if (isLogged()) {
            final User user = currentUser();
        %>
        <%= tab("/", "workouts", "Mon vestiaire", "first", "current currentFirst")%><%= tab("/bib", "", "Mon dossard", "last",
            "current currentLast",
            "id", String.valueOf(user.getId()))%>
        <% } else {
            final String from = findFromAction();
            out.append(tab("/", "forgotPassword", "Mot de passe oublié ?", "first", "current currentFirst"))
                    .append(tab("/", "login", "Connexion", "", "current", "fromAction", from))
                    .append(' ')
                    .append(tab("/", "signup", "Inscription", "last", "current currentLast", "fromAction", from));
        } %>
    </ul>
    <ul class="primary"><%= tab("/", "", "Tableau général", "first", "current currentFirst")%> <%=tab("/groups", "",
            "Groupes", "last", "current currentLast")%>
    </ul>
    <%
        if (isLogged()) {
            final User user = currentUser();
    %>
    <%}%>
</div>
<%!
    public static String tab(final String namespace, final String action, final String content, final String classes,
                             final String selectedClasses, final String... params) {
        final boolean current = isCurrentAction(namespace, action);
        final String url = createUrl(namespace, action, params);
        final String selectedPart = current ? " " + selectedClasses : "";
        final String decoratedContent = current ? content : anchor(content, url);
        return "<li class='" + classes + selectedPart + "'><span>" + decoratedContent + "</span></li>";
    }
%>
