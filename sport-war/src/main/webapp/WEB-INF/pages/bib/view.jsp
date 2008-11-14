<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.UserString" %>
<%@ page import="com.nraynaud.sport.data.BibPageData" %>
<%@ page import="com.nraynaud.sport.web.view.PrivateMessageFormConfig" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<% final BibPageData data = top(BibPageData.class);
    final User user = data.user;
    final boolean lookingOwnBib = user.equals(currentUser());%>
<p:layoutParams title="<%=lookingOwnBib ? "Mon dossard" : "Le dossard de " + user.getName()%>"/>

<div class="block bibBlock">
    <div class="content">
            <span class="buttonList">
                <% if (lookingOwnBib) {%>
                <a href="<%=createUrl("/bib", "edit")%>" title="Modifier mon dossard"
                   class="button editButton">Modifier</a>
                <%}%>
            </span>
        <% final String townLabel = "Ma ville";%>
        <dl>
            <% if (user.getTown() == null && user.getDescription() == null && user.getWebSite() == null) {%>
            <dt class="noDescription informative">
                <%if (lookingOwnBib) {%>
                Vous n'avez entré aucune information, cliquez sur le petit crayon pour remplir votre dossard.
                <%} else {%>
                <%=user.getName()%> n'a pas encore rempli son dossard.
                <%}%>
            </dt>
            <%}%>
            <% if (user.getTown() != null) { %>
            <dt><%=townLabel%>&nbsp;:</dt>
            <dd><%=escapedOrNull(user.getTown(),
                    "")%>
            </dd>
            <% } %>
            <% if (user.getDescription() != null) { %>
            <dt>Moi&nbsp;:</dt>
            <dd><%=escapedOrNullmultilines(
                    user.getDescription(), "")%>
            </dd>
            <% } %>
            <% if (user.getWebSite() != null) { %>
            <dt>Mon site&nbsp;:</dt>
            <dd><%=formatUrl(user.getWebSite(),
                    "")%>
            </dd>
            <% } %>
        </dl>
    </div>
    <span class="importantLink"><%=link("/statistics", "", "Voir ses statistiques", "", "id",
            String.valueOf(user.getId()))%></span>
</div>

<div <%=!lookingOwnBib && isLogged() ? "id='globalLeft'" : "" %> >

    <% if (!lookingOwnBib) {%>
    <div class="block sheetBlock userSheetBlock">
        <div class="header">
            <div class="deco"></div>
            <h3>Dernières sorties</h3>
        </div>
        <div class="content">
            <div class="deco"></div>
            <%
                paginate(pageContext, "workoutTable.jsp", view(data.workouts, "workoutPage"));%>
        </div>
        <div class="footer">
            <div class="deco"></div>
        </div>
    </div>
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
