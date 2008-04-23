<%@ page import="com.nraynaud.sport.Track" %>
<%@ page import="com.nraynaud.sport.web.converter.DistanceConverter" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Les parcours" showHeader="false" showTitleInPage="false" showFooter="false"/>


<p:header>
    <link href="<%=stat("/static/track/trackstyle.css")%>" rel="stylesheet" type="text/css">
</p:header>

<% final Track track = property("track", Track.class);%>
<div id="map"></div>
<div id="controlPanel">
    <%=selectableLink("/track", "edit", "Nouveau Parcours", "")%>
    <hr>
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
            <td><%=DistanceConverter.formatDistance(loopTrack.getLength())%>km
            </td>
        </tr>
        <% }%>
    </table>
    <hr>
    <%
        if (track != null) {
            if (track.getTitle() != null) {
    %>
    <h3><%=track.getTitle()%>
    </h3>
    <%}%>
    <span id="distance"></span>
    <%if (track.getUser().equals(currentUser())) { %>
    <%=selectableLink("/track", "edit",
            "<img class='pen' src=\"" + stat("/static/pen.png") + "\" alt=\"\">",
            "modifier le parcours", "id", track.getId().toString())%>
    <%
            }
        }
    %>


</div>

<%call(pageContext, "trackLoader.jsp");%>
<p:javascript src="<%=stat("/static/track/trackView.js")%>"/>
<%if (track != null) {%>
<p:javascript>loadOnStartup("<%=track.getPoints()%>");</p:javascript>
<%}%>