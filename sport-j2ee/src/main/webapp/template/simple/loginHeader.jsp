<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="loginHeader">
    <% if (isLogged()) {%>
    <span id="loginName"><%= escaped(currentUser().getName())%><!--<%=currentUser().getId()%> --></span> |
    <a href="<s:url namespace="/" action='workouts' includeParams="none"/>">Mon vestiaire</a> |
    <a href="<s:url namespace="/bib" action='' includeParams="none"/>">Mon dossard</a> |
    <s:form id="logoutForm" namespace="/" action="logout" method="POST">
        <s:submit cssClass="logoutButton" value="Déconnexion"/>
    </s:form>
    <% } else { %>
    <a href="<s:url namespace="/" action='login'/>">Connexion</a> | <a href="<s:url namespace="/" action='signup'/>">Inscription</a>&nbsp;
    <% } %>
</div>