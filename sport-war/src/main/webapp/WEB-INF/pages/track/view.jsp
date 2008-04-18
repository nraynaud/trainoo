<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="java.util.List" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Édition d'un parcours" showTitleInPage="false" showFooter="false"/>

<p:header>
    <link href="<%=stat("/static/track/trackstyle.css")%>" rel="stylesheet" type="text/css">
</p:header>

<div id="map"></div>
<div id="controlPanel">
    <form method="get" action="lol">
        <select name="trackSelection" style="width:100%" size="5">
            <%
                int i = 0;
                final List<String> strings = (List<String>) property("tracks", List.class);
                for (final String track : strings) {
                    i++;
            %>
            <option value="" onclick="loadTrack('<%=track%>');return false;">circuit<%=i%>
            </option>
            <%
                }
            %>
        </select>
    </form>
    <div id="trackName">Mon parcours</div>
    <span id="distance"></span>(<span id="pointsCount"></span> points)

    <s:form id="createForm" namespace="/track" action="create">
        <s:hidden id="trackVar" name="track"/>
        <s:hidden id="lengthVar" name="length"/>
        <s:submit id="submit" value="Enregistrer" onclick="" tabindex="1"/>
    </s:form>
    <a href="#" onclick="newTrack(); return false;">Nouveau parcours</a>

    <div style="visibility:hidden">
        <img id="map_handle" src="<%=stat("/static/track/map_handle.png")%>" alt="">
        <img id="map_handle_active" src="<%=stat("/static/track/map_handle_active.png")%>" alt="">
        <img id="map_marker" src="<%=stat("/static/track/map_marker.png")%>" alt="">
        <img id="map_marker_active" src="<%=stat("/static/track/map_marker_active.png")%>" alt="">
    </div>
    <div style="margin:0 auto; width:125px;">
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
<%
    final String localKey = "ABQIAAAAB_pY09DNgzbhAapu-Taq3BQ2JGlzsnS7jQe2K3BOHN-eIIg7qxTdWxIGQzbXObDxDX77K7MZDK5a1Q";
    final String serverKey = "ABQIAAAAB_pY09DNgzbhAapu-Taq3BTtRcdkEu7NTWhsViqteywQ9rYraBTFFcawlKj7SSAwxJ8j_FZD8gkcEA";
    final String key = pageContext.getRequest().getLocalName().equals("192.168.0.20") ? localKey : serverKey;
%>
<script type="text/javascript"
        src="http://www.google.com/maps?file=api&v=2&key=<%=key%>&hl=fr"></script>

<p:javascript src="<%=stat("/static/track/geo.js")%>"/>
<p:javascript src="<%=stat("/static/track/track.js")%>"/>
