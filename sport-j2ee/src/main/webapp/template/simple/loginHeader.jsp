<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.web.SportRequest" %>
<%@ page import="com.nraynaud.sport.web.SportSession" %>
<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%@ page import="com.opensymphony.xwork2.util.TextUtils" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="loginHeader">
    <% final SportRequest sportRequest = (SportRequest) ActionContext.getContext().get("sportRequest");
        final SportSession session = sportRequest.getSportSession();
        if (session != null) {
            final User user = session.getUser();%>
    <span id="loginName"><%= TextUtils.htmlEncode(user.getName())%> | <a href="<s:url action='workouts'/>">Mes
        entraînements</a></span> |
    <s:form id="logoutForm" action="logout" method="POST">
        <s:submit cssClass="logoutButton" value="Déconnexion"/>
    </s:form>
    <% } else { %>
    <a href="<s:url action='login'/>">S'identifier</a> | <a href="<s:url action='signup'/>">S'inscrire</a>
    <% } %>
</div>