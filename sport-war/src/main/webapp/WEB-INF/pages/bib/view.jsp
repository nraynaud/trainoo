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

<div id="<%= lookingOwnBib || !isLogged() ? "tinyCenter" : "globalLeft"%>">
    <% final String defaultValue = "non précisé";
        final String townLabel = "Ma ville";%>
    <table class="displayFormLayoutTable">
        <tr>
            <td><span class="label"><%=townLabel%>&nbsp;:</span></td>
            <td><span class="<%=defaultOrUserClass(user.getTown())%>"><%=escapedOrNull(user.getTown(),
                    defaultValue)%></span>
            </td>
        </tr>
        <tr>
            <td><span class="label">Moi&nbsp;:</span></td>
            <td><span class="<%=defaultOrUserClass(user.getDescription())%>"><%=escapedOrNullmultilines(
                    user.getDescription(), defaultValue)%></span></td>
        </tr>
        <tr>
            <td><span class="label">Mon site&nbsp;: </span></td>
            <td><span class="<%=defaultOrUserClass(user.getWebSite())%>"><%=formatUrl(user.getWebSite(),
                    defaultValue)%></span></td>
        </tr>
    </table>
    <% if (lookingOwnBib) {%>
    <p align="right"><a href="<s:url action="edit" namespace="/bib"/>">Mettre à jour</a></p>
    <%} else {%>
    <h2>Ses dernières sorties</h2>
    <% paginate(pageContext, "workoutTable.jsp", view(data.workouts, "workoutPage"));%>
    <%}%>
</div>
<%if (!lookingOwnBib && isLogged()) {%>
<div id="globalRight">
    <h2>Envoyer un message à <%=user.getName()%>
    </h2>

    <div class="content">
        <%
            call(pageContext, "privateMessageForm.jsp", new PrivateMessageFormConfig(data.user.getName()),
                    "hideReceiverBox", true);
            paginate(pageContext, "messageList.jsp", view(data.privateMessages, "messagePageIndex"));
        %>
    </div>
</div>
<%}%>
<%!
    private static String escapedOrNull(final UserString town, final String defaultValue) {
        return town == null ? defaultValue : town.toString();
    }
%>