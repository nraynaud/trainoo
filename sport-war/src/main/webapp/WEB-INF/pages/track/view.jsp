<%@ page import="com.nraynaud.sport.Track" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page import="com.nraynaud.sport.web.view.StackUtil" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<% final Track track = property("track");%>
<p:layoutParams title='<%=track == null ? "Mes parcours" : track.getTitle() == null ? "Mon parcours" : track.getTitle()
        .toString()%>' showHeader="false" showTitleInPage="false" showFooter="false"/>


<p:header>
    <link href='<%=stat("/static/track/trackstyle.css")%>' rel="stylesheet" type="text/css">
</p:header>
<div id="mapGlobalContainer">
    <div id="controlPanel">
        <a href="/" title="Trainoo.com - Tableau général">
            <img src="<%=stat("/static/external/logo_widget.png")%>" alt="logo">
        </a>
        (parcours beta)
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
        <%=link("/track", "edit",
                "<img class='pen' src=\"" + stat("/static/pen.png") + "\" alt=\"\">",
                "modifier le parcours", "id", track.getId().toString())%>
        <hr>
        <%
                }
            }
        %>
        <% if (Helpers.isLogged()) {%>
        <%=link("/track", "edit", "Nouveau Parcours", "")%>
        <%
            final List<Track> trackList = StackUtil.<List<Track>>property("tracks");
            if (!trackList.isEmpty()) {%>
        <hr>
        <h3>Mes parcours&nbsp;:</h3>
        <table id="trackTable">
            <%
                boolean parity = false;
                for (final Track loopTrack : trackList) {
                    parity = !parity;
            %>
            <tr class="<%=parity ? "odd":"even"%> <%=loopTrack.equals(track) ? "highLight" : ""%>">
                <td><%=link("/track", "", loopTrack.getId().toString(), "voir le parcours", "id",
                        loopTrack.getId().toString())%>
                </td>
                <td><%=shortSpan(loopTrack.getTitle(), 10)%>
                </td>
                <td><%=shortSpan(loopTrack.getUser().getName(), 10)%>
                </td>
                <td style="text-align:right;"><%=(int) loopTrack.getLength()%>km
                </td>
            </tr>
            <% }%>
        </table>

        <%
                }
            }
        %>
        <hr>
        <div style="margin:10px auto; width:125px;">
            <script type="text/javascript">
                <!--
                google_ad_client = "pub-1788371406648361";
                /* 120x600, gratte-ciel */
                google_ad_slot = "3759319574";
                google_ad_width = 120;
                google_ad_height = 600;
                //-->
            </script>
            <script type="text/javascript"
                    src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
            </script>
        </div>
    </div>
    <div id="map"></div>
</div>
<%call(pageContext, "trackLoader.jsp");%>
<p:javascript src='<%=stat("/static/track/trackView.js")%>'/>
<%if (track != null) {%>
<p:javascript>loadOnStartup("<%=track.getPoints()%>");</p:javascript>
<%}%>