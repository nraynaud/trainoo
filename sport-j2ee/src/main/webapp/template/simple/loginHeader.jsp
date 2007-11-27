<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.web.SessionUtil" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<% final HttpSession session = ((HttpServletRequest) pageContext.getRequest()).getSession(false);
    final User user = SessionUtil.getUser(session);
    if (user != null) {%>
<span id="loginName"><%= user.getName()%> | <a href="<s:url action='workouts'/>">Mes entraînements</a></span> |
<s:form id="logoutForm" action="logout" method="POST">
    <s:submit cssClass="logoutButton" value="Déconnexion"/>
</s:form>
<% } else { %>
<a href="<s:url action='login'/>">S'identifier</a> | <a href="<s:url action='signup'/>">S'inscrire</a>
<% } %>