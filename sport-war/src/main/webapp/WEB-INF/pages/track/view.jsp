<%@ page import="com.nraynaud.sport.Track" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<% final Track track = property("track", Track.class);%>
<p:layoutParams title="<%=track == null ? "Mes parcours" : track.getTitle() == null ? "Mon parcours" : track.getTitle()
        .toString()%>" showHeader="false" showTitleInPage="false" showFooter="false"/>


<p:header>
    <link href="<%=stat("/static/track/trackstyle.css")%>" rel="stylesheet" type="text/css">
</p:header>

<div id="map"></div>
<div id="controlPanel">
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
        }%>
    <hr>
    <%
        }
    %>
    <%=selectableLink("/track", "edit", "Nouveau Parcours", "")%>
    <hr>
    <h3>Mes parcours&nbsp;:</h3>
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
            <td style="text-align:right;"><%=(int) loopTrack.getLength()%>km
            </td>
        </tr>
        <% }%>
    </table>
    <hr>
    <div style="margin:10px auto; width:125px;">
        <script type="text/javascript"><!--
        google_ad_client = "pub-1788371406648361";
        /* pour les tracks 125x125, date de création 17/04/08 */
        google_ad_slot = "5961618039";
        google_ad_width = 125;
        google_ad_height = 125;
        //-->
        </script>
        <script type="text/javascript"
                src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
        </script>
    </div>

</div>

<%call(pageContext, "trackLoader.jsp");%>
<p:javascript src="<%=stat("/static/track/trackView.js")%>"/>
<%if (track != null) {%>
<p:javascript>loadOnStartup("<%=track.getPoints()%>");</p:javascript>
<%}%>