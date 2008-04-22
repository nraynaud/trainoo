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
    <s:form id="createForm" namespace="/track" action="edit">
        <s:hidden name="id" value="%{track.id}"/>
        <s:hidden id="trackVar" name="points" value="%{track.points}"/>
        <s:hidden id="lengthVar" name="length" value="%{track.length}"/>
        <label for="title">Titre&nbsp;:</label>

        <p><s:textfield id="title" name="title" value="%{track.title}" cssStyle="width:99%"/></p>
        <p:javascript>makeItCount('title', 25);</p:javascript>
        <s:submit id="submit" value="Enregistrer" tabindex="1"/>

        <span id="distance"></span>(<span id="pointsCount"></span> points)
    </s:form>
    <a href="#" onclick="newTrack(); return false;">Nouveau parcours</a>

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
<p:javascript src="<%=stat("/static/track/track.js")%>"/>
<p:javascript>loadOnStartup($('trackVar').value);</p:javascript>

