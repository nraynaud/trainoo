<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.UserString" %>
<%@ page import="com.nraynaud.sport.data.BibPageData" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<% final BibPageData data = (BibPageData) top();
    final User user = data.user;
    final boolean lookingOwnBib = user.equals(currentUser());%>
<p:layoutParams title="<%=lookingOwnBib ? "Mon dossard" : "Le dossard de " + user.getName()%>"/>

<div id="<%= lookingOwnBib || !isLogged() ? "tinyCenter" : "globalLeft"%>">
    <% final String defaultValue = "non précisé";
        final String townLabel = "Ma ville";%>
    <table class="displayFormLayoutTable">
        <tr>
            <td><span class="label"><%=townLabel%>&nbsp;:</span></td>
            <td><span class="<%=className(user.getTown())%>"><%=escapedOrNull(user.getTown(), defaultValue)%></span>
            </td>
        </tr>
        <tr>
            <td><span class="label">Moi&nbsp;:</span></td>
            <td><span class="<%=className(user.getDescription())%>"><%=escapedOrNullmultilines(
                    user.getDescription(), defaultValue)%></span></td>
        </tr>
        <tr>
            <td><span class="label">Mon site&nbsp;: </span></td>
            <td><span class="<%=className(user.getWebSite())%>"><%=formatUrl(user.getWebSite(),
                    defaultValue)%></span></td>
        </tr>
    </table>
    <% if (lookingOwnBib) {%>
    <p align="right"><a href="<s:url action="edit" namespace="/bib"/>">Mettre à jour</a></p>
    <%} else {%>
    <h2>Ses dernières sorties</h2>
    <% call(pageContext, "workoutTable.jsp", data.workouts);%>
    <%}%>
</div>
<%if (!lookingOwnBib && isLogged()) {%>
<div id="globalRight">
    <h2>Envoyer un message à <%=user.getName()%>
    </h2>
    <%
        call(pageContext, "privateMessageForm.jsp", null, "receiver", literal(data.user.getName()), "hideReceiverBox",
                "true");
        call(pageContext, "messageList.jsp", data.privateMessages, "pageVariable", "'messagePageIndex'");
    %>
</div>
<%}%>
<%!
    private static String escapedOrNull(final UserString town, final String defaultValue) {
        return town == null ? defaultValue : town.toString();
    }

    private static String className(final UserString town) {
        return town == null ? "serverDefault" : "userSupplied";
    }
%>