<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.web.SportSession" %>
<%@ page import="com.opensymphony.xwork2.util.TextUtils" %>
<%@ page import="static com.nraynaud.sport.web.SportRequest.getSportRequest" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="loginHeader">
    <% final SportSession session = getSportRequest().getSportSession();
        if (session != null) {
            final User user = session.getUser();%>
    <span id="loginName"><%= TextUtils.htmlEncode(user.getName()) + "<!-- " + user.getId() + " -->"%></span> |
    <a href="<s:url namespace="/" action='workouts'/>">Mon vestiaire</a> |
    <a href="<s:url namespace="/bib" action=''/>">Mon dossard</a> |
    <s:form id="logoutForm" namespace="/" action="logout" method="POST">
        <s:submit cssClass="logoutButton" value="Déconnexion"/>
    </s:form>
    <% } else { %>
    <a href="<s:url namespace="/" action='login'/>">Connexion</a> | <a href="<s:url namespace="/" action='signup'/>">Inscription</a>&nbsp;
    <% } %>
</div>