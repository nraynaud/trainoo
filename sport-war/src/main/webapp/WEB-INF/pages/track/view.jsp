<%@ page import="com.nraynaud.sport.Track" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Les parcours" showHeader="false" showTitleInPage="false" showFooter="false"/>


<p:header>
    <link href="<%=stat("/static/track/trackstyle.css")%>" rel="stylesheet" type="text/css">
</p:header>

<div id="map"></div>
<div id="controlPanel">
    <table>
        <%
            for (final Track track : (List<Track>) property("tracks", List.class)) {
        %>
        <tr>
            <td><%=selectableLink("/track", "", track.getId().toString(), "voir le parcours", "id",
                    track.getId().toString())%>
            </td>
            <td><%=track.getUser().getName()%>
            </td>
            <%if (track.getUser().equals(currentUser())) { %>
            <td><%=selectableLink("/track", "edit", "modifier", "modifier le parcours", "id",
                    track.getId().toString())%>
            </td>
            <%}%>
        </tr>
        <% }%>
    </table>
    <hr>
    <span id="distance"></span>
</div>

<%call(pageContext, "trackLoader.jsp");%>
<p:javascript src="<%=stat("/static/track/trackView.js")%>"/>
<p:javascript>loadOnStartup("<%=property("track", Track.class).getPoints()%>");</p:javascript>