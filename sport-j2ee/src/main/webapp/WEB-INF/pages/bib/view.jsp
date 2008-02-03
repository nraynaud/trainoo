<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.data.BibPageData" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<% final BibPageData data = (BibPageData) top();
    final User user = data.user;
    final boolean lookingOwnBib = currentUser().equals(user);%>
<p:layoutParams title="<%=lookingOwnBib ? "Mon dossard" : "Le dossard de " + escaped(user.getName())%>"/>

<div id="<%= lookingOwnBib ? "tinyCenter" : "globalLeft"%>">
    <% final String defaultValue = "non précisé";
        final String town = user.getTown();
        final String townLabel = "Ma ville";%>
    <table class="displayFormLayoutTable">
        <tr>
            <td><span class="label"><%=townLabel%>&nbsp;:</span></td>
            <td><span class="<%=className(town)%>"><%=escapedOrNull(town, defaultValue)%></span></td>
        </tr>
        <tr>
            <td><span class="label">Moi&nbsp;:</span></td>
            <td><span class="<%=className(user.getDescription())%>"><%=escapedOrNullmultilines(user.getDescription(),
                    defaultValue)%></span></td>
        </tr>
        <tr>
            <td><span class="label">Mon site&nbsp;: </span></td>
            <td><span class="<%=className(user.getDescription())%>"><%=formatUrl(user.getWebSite(),
                    defaultValue)%></span></td>
        </tr>
    </table>
    <% if (lookingOwnBib) {%>
    <p align="right"><a href="<s:url action="edit" namespace="/bib"/>">Mettre à jour</a></p>
    <%} else {%>
    <p>&nbsp;</p>
    <%}%>
    <h2>Ses derniers entraînements</h2>
    <% call(pageContext, "workoutTable.jsp", data.workouts);%>
</div>
<%if (!lookingOwnBib) {%>
<div id="globalRight">
    <h2>Envoyer un message à <%=escaped(user.getName())%>
    </h2>
    <%
        call(pageContext, "privateMessageForm.jsp", null, "receiver", literal(data.user.getName()), "hideReceiverBox",
                "true");
        call(pageContext, "messageList.jsp", data.messages);
    %>
</div>
<%}%>
<%!
    private static String className(final String town) {
        return town == null ? "serverDefault" : "userSupplied";
    }
%>