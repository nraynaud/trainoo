<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.SportActionMapper" %>
<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%@ page import="org.apache.struts2.dispatcher.mapper.ActionMapping" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="loginHeader">
    <div class="left"><%= tabElement("/", "", "Tableau général")%>
    </div>
    <div class="right">
        <% if (isLogged()) {%>
        <span id="loginName"><!--<%=currentUser().getId()%> --><%= escaped(currentUser().getName())%>
            <span style="font-size:x-small;"><%= tabElement("/privatedata", "", "(mot de passe)")%></span>
        </span>
        <%= tabElement("/", "workouts", "Mon vestiaire")%><%= tabElement("/bib", "", "Mon dossard")%>
        <s:form id="logoutForm" namespace="/" action="logout" method="POST">
            <s:submit cssClass="logoutButton" value="Déconnexion"/>
        </s:form>
        <% } else { %>
        <%= tabElement("/", "login", "Connexion")%> <%= tabElement("/", "signup", "Inscription")%>
        <% } %>
    </div>
</div>
<%!
    private static final SportActionMapper MAPPER = new SportActionMapper();

    private static String tabElement(final String namespace, final String action, final String content) {
        final ActionMapping mapping = (ActionMapping) ActionContext.getContext().get("struts.actionMapping");
        final String cssClass = namespace.equals(mapping.getNamespace()) && action.equals(
                mapping.getName()) ? "class='selected'" : "";
        final String url = MAPPER.getUriFromActionMapping(new ActionMapping(action, namespace, null, null));
        return "<a " + cssClass + " href='" + url + "'>" + content + "</a>";
    }
%>