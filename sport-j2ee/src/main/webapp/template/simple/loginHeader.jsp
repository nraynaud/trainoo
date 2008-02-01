<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="loginHeader">
    <div class="left"><a <%=c2("/|?")%> href="<s:url action="" namespace="/" includeParams="none"/>">Tableau
        général</a></div>

    <div class="right">
        <% if (isLogged()) {%>
        <span id="loginName"><!--<%=currentUser().getId()%> --><%= escaped(currentUser().getName())%><a <%=c2(
                "/privatedata|?")%>
                href="<s:url action="" namespace="/privatedata" includeParams="none"/>" style="font-size:x-small;">(mot
            de passe)</a></span>
        <a <%=c2("/|workouts?")%> href="<s:url namespace="/" action='workouts' includeParams="none"/>">Mon vestiaire</a>
        <a <%=c2("/bib|?")%> href="<s:url namespace="/bib" action='' includeParams="none"/>">Mon dossard</a>
        <s:form id="logoutForm" namespace="/" action="logout" method="POST">
            <s:submit cssClass="logoutButton" value="Déconnexion"/>
        </s:form>
        <% } else { %>
        <a <%=c2("/|login?")%> href="<s:url namespace="/" action='login' includeParams="none"/>">Connexion</a> <a <%=c2(
            "/|signup?")%>
            href="<s:url namespace="/" action='signup' includeParams="none"/>">Inscription</a>&nbsp;
        <% } %>
    </div>
</div>
<%!
    private static String c2(final String prefix) {
        return stringProperty("actionDescription").startsWith(prefix) ? "class='selected'" : "";
    }
%>