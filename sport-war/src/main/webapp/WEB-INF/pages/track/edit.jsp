<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Édition d'un parcours" showHeader="false" showTitleInPage="false" showFooter="false"/>

<p:header>
    <link href="<%=stat("/static/track/trackstyle.css")%>" rel="stylesheet" type="text/css">
</p:header>

<div id="map"></div>
<div id="controlPanel">
    <div id="trackName"><span>Mon parcours</span>
        <button onclick="" style="font-size:10px">Renommer</button>
    </div>
    <span id="distance"></span>(<span id="pointsCount"></span> points)

    <s:form id="createForm" namespace="/track" action="create">
        <s:hidden id="trackVar" name="track" value="%{track.points}"/>
        <s:hidden id="lengthVar" name="length" value="%{track.length}"/>
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
<%call(pageContext, "trackLoader.jsp");%>
<p:javascript> onLoaded.push(function() {loadTrack($('trackVar').value);});</p:javascript>

