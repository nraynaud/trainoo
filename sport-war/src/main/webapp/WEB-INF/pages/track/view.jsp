<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Édition d'un parcours" showTitleInPage="false" showFooter="false"/>

<p:header>
    <link href="<%=stat("/static/track/trackstyle.css")%>" rel="stylesheet" type="text/css">
</p:header>

<div id="map"></div>
<div id="controlPanel">
    <img id='lolToken' src="http://www.geoportail.fr/imgs/visu/empty.gif" alt="">
    <%
        int i = 0;
        final List<String> strings = (List<String>) property("tracks", List.class);
        for (final String track : strings) {
            i++;
    %>
    <a href="#" onclick="loadTrack('<%=track%>');return false;">circuit<%=i%>&nbsp;</a>
    <br>
    <%
        }
    %>
    longueur&nbsp;:<span id="distance"></span>(<span id="pointsCount"></span>)

    <s:form id="createForm" namespace="/track" action="create">
        <s:hidden id="trackVar" name="track"/>
        <s:hidden id="lengthVar" name="length"/>
        <s:submit id="submit" value="Enregistrer le circuit !" onclick="" tabindex="1"/>
    </s:form>
    <a href="#" onclick="newTrack(); return false;">Nouveau parcours</a>
    <span id="position"></span>
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
