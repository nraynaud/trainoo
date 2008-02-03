<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.NewMessageData" %>
<%@ page import="com.nraynaud.sport.web.SportActionMapper" %>
<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%@ page import="org.apache.struts2.dispatcher.mapper.ActionMapping" %>
<%@ page import="java.util.List" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="loginHeader">
    <div class="left"><%= tabElement("/", "", "Tableau général")%>
    </div>
    <div class="right">
        <% if (isLogged()) {
            final List<NewMessageData> newMessages = (List<NewMessageData>) property("newMessages");
            if (newMessages.size() > 0) {
                int count = 0;
                for (final NewMessageData message : newMessages) {
                    count += message.messageCount;
                }%>
        <s:url id="messagesURL" action="" namespace="/messages">
            <s:param name="receiver" value="newMessages.get(0).sender"/>
        </s:url>
        <a href="<s:property value="%{messagesURL}"/>" style="color:red"><%=count%> nouveau(x) message(s)</a>
        <%}%>
        <span id="loginName"><!--<%=currentUser().getId()%> --><%= escaped(currentUser().getName())%>
            <span style="font-size:x-small;"><%= tabElement("/privatedata", "", "(mot de passe)")%></span>
        </span>
        <%= tabElement("/", "workouts", "Mon vestiaire")%><%= tabElement("/bib", "", "Mon dossard", "id",
            String.valueOf(currentUser().getId()))%>
        <s:form id="logoutForm" namespace="/" action="logout" method="POST">
            <s:submit cssClass="logoutButton" value="Déconnexion"/>
        </s:form>
        <% } else { %>
        <%= tabElement("/", "login", "Connexion")%> <%= tabElement("/", "signup", "Inscription")%>
        <% } %>
    </div>
</div>
<%!
    private static String getFirstValue(final String key) {
        final Object val = ActionContext.getContext().getParameters().get(key);
        if (val != null)
            return ((String[]) val)[0];
        return null;
    }

    private static final SportActionMapper MAPPER = new SportActionMapper();

    private static String tabElement(final String namespace, final String action, final String content,
                                     final String... params) {
        final ActionMapping mapping = (ActionMapping) ActionContext.getContext().get("struts.actionMapping");
        boolean selected = namespace.equals(mapping.getNamespace()) && action.equals(
                mapping.getName());
        final String url = MAPPER.getUriFromActionMapping(new ActionMapping(action, namespace, null, null));
        final String query;
        if (params.length > 0) {
            final StringBuilder getParams = new StringBuilder(20);
            getParams.append("?");
            for (int i = 0; i < params.length; i += 2) {
                getParams.append(params[i]).append('=').append(params[i + 1]);
                selected &= params[i + 1].equals(getFirstValue(params[i]));
            }
            query = getParams.toString();
        } else
            query = "";
        return "<a " + (selected ? "class='selected'" : "") + " href='" + url + query + "'>" + content + "</a>";
    }
%>