<%@ page import="com.nraynaud.sport.Track" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Les parcours" showHeader="false" showTitleInPage="false" showFooter="false"/>


<p:header>
    <link href="<%=stat("/static/track/trackstyle.css")%>" rel="stylesheet" type="text/css">
</p:header>

<% final Track track = property("track", Track.class);%>
<div id="map"></div>
<div id="controlPanel">
    <table id="trackTable">
        <%
            boolean parity = false;
            for (final Track loopTrack : (List<Track>) property("tracks", List.class)) {
                parity = !parity;
        %>
        <tr class="<%=parity ? "odd":"even"%> <%=loopTrack.equals(track) ? "highLight" : ""%>">
            <td><%=selectableLink("/track", "", loopTrack.getId().toString(), "voir le parcours", "id",
                    loopTrack.getId().toString())%>
            </td>
            <td><%=shortSpan(loopTrack.getTitle())%>
            </td>
            <td><%=shortSpan(loopTrack.getUser().getName())%>
            </td>
            <%if (loopTrack.getUser().equals(currentUser())) { %>
            <td><%=selectableLink("/track", "edit",
                    "<img class='pen' src=\"" + stat("/static/pen.png") + "\" alt=\"\">",
                    "modifier le parcours", "id", loopTrack.getId().toString())%>
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
<%if (track != null) {%>
<p:javascript>loadOnStartup("<%=track.getPoints()%>");</p:javascript>
<%}%>