<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.UserString" %>
<%@ page import="com.nraynaud.sport.data.BibPageData" %>
<%@ page import="com.nraynaud.sport.web.view.PrivateMessageFormConfig" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<% final BibPageData data = top(BibPageData.class);
    final User user = data.user;
    final boolean lookingOwnBib = user.equals(currentUser());%>
<p:layoutParams title="<%=lookingOwnBib ? "Mon dossard" : "Le dossard de " + user.getName()%>"/>

<div <%=!lookingOwnBib && isLogged() ? "id='globalLeft'" : "" %> >
    <div class="block bibBlock">
        <div class="content">
            <span class="buttonList">
                <% if (lookingOwnBib) {%>
                <a href="<%=createUrl("/bib", "edit")%>" title="Modifier mon dossard"
                   class="button editButton">Modifier</a>
                <%}%>
            </span>
            <% final String defaultValue = "non précisé";
                final String townLabel = "Ma ville";%>
            <dl>
                <dt><%=townLabel%>&nbsp;:</dt>
                <dd><%=escapedOrNull(user.getTown(),
                        defaultValue)%>
                </dd>
                <dt>Moi&nbsp;:</dt>
                <dd><%=escapedOrNullmultilines(
                        user.getDescription(), defaultValue)%>
                </dd>
                <dt>Mon site&nbsp;:</dt>
                <dd><%=formatUrl(user.getWebSite(),
                        defaultValue)%>
                </dd>
            </dl>
        </div>
    </div>
    <% if (!lookingOwnBib) {%>
    <h2>Ses dernières sorties</h2>
    <% paginate(pageContext, "workoutTable.jsp", view(data.workouts, "workoutPage", DEFAULT_WORKOUT_TRANSFORMER));%>
    <%}%>
</div>
<%if (!lookingOwnBib && isLogged()) {%>
<div id="globalRight">
    <h2>Envoyer un message à <%=user.getName()%>
    </h2>
    <%
        call(pageContext, "privateMessageForm.jsp", new PrivateMessageFormConfig(data.user.getName()),
                "hideReceiverBox", true);
        paginate(pageContext, "messageList.jsp", view(data.privateMessages, "messagePageIndex"));
    %>
</div>
<%}%>
<%!
    private static String escapedOrNull(final UserString town, final String defaultValue) {
        return town == null ? defaultValue : town.toString();
    }
%>
