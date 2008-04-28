<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Track" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Édition d'un parcours" showHeader="false" showTitleInPage="false" showFooter="false"/>

<p:header>
    <link href="<%=stat("/static/track/trackstyle.css")%>" rel="stylesheet" type="text/css">
</p:header>

<% final Track track = property("track", Track.class);%>
<div id="mapGlobalContainer">
    <div id="controlPanel">
        <s:form id="createForm" namespace="/track" action="edit">
            <input type="hidden" name="id" value="<%=track == null ? "" : track.getId()%>"/>
            <input type="hidden" id="trackVar" name="points" value="<%=track == null ? "" : track.getPoints()%>"/>
            <input type="hidden" id="lengthVar" name="length" value="<%=track == null ? "" : track.getLength()%>"/>
            <label for="title">Titre&nbsp;:</label>

            <div><input type="text" id="title" name="title"
                        value="<%=track == null ? "Nouveau parcours" : track.getTitle()%>" style="width:99%"/></div>
            <p:javascript>makeItCount('title', 25);</p:javascript>
            <label>Longueur&nbsp;:</label><span id="distance"></span>
            <s:submit id="submit" value="Enregistrer" tabindex="1"/>
        </s:form>
        <%if (track != null) {%>
        <hr>
        <s:form namespace="/track" action="delete">
            <input type="hidden" name="id" value="<%=track == null ? "" : track.getId()%>"/>
            <s:submit value="Supprimer le parcours"/>
        </s:form>
        <%}%>
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
    <div id="tip" style="display:none;">Appuyez sur la touche <img src="<%=stat("/static/track/shift_key.png")%>"
                                                                   alt="Majuscule"> pour modifier votre chemin.
    </div>
    <div id="map"></div>
</div>
<%call(pageContext, "trackLoader.jsp");%>
<p:javascript src="<%=stat("/static/track/updateEditor.js")%>"/>
<p:javascript src="<%=stat("/static/track/appendEditor.js")%>"/>
<p:javascript src="<%=stat("/static/track/trackEdit2.js")%>"/>
<%if (track != null) {%>
<p:javascript>loadOnStartup($('trackVar').value);</p:javascript>
<%}%>

