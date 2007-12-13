<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.web.SportSession" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<% final SportSession session = SportSession.fromRequest(pageContext.getRequest());
    if (session != null) {
        final User user = session.getUser();%>
<span id="loginName"><%= user.getName()%> | <a href="<s:url action='workouts'/>">Mes entraînements</a></span> |
<s:form id="logoutForm" action="logout" method="POST">
    <s:submit cssClass="logoutButton" value="Déconnexion"/>
</s:form>
<% } else { %>
<a href="<s:url action='login'/>">S'identifier</a> | <a href="<s:url action='signup'/>">S'inscrire</a>
<% } %>