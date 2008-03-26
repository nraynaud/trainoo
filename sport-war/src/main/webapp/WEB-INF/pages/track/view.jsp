<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page import="java.util.List" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Édition d'un parcours" showTitleInPage="false" showFooter="false"/>
<style type="text/css">
    html, body, #center {
        min-height: 99%;
        height: 99%;
    }
</style>
<div style="float:left; width:150px;">
    <%
        int i = 0;
        final List<String> strings = (List<String>) Helpers.property("tracks", List.class);
        for (final String track : strings) {
            i++;
    %>
    <a href="#" onclick="loadTrack('<%=track%>');return false;">circuit<%=i%>&nbsp;</a>
    <br>
    <%
        }
    %>
</div>
<div id="map" style="width: 50%; height: 400px; float:right;"></div>
<div id="distance"></div>
<s:form id="createForm" namespace="/track" action="create">
    <s:hidden id="trackVar" name="track"/>
    <s:hidden id="lengthVar" name="length"/>
    <s:submit id="submit" value="Enregistrer le circuit !" onclick="" tabindex="1"/>
</s:form>

<script type="text/javascript"
        src="http://www.google.com/maps?file=api&v=2&key=ABQIAAAAB_pY09DNgzbhAapu-Taq3BTtRcdkEu7NTWhsViqteywQ9rYraBTFFcawlKj7SSAwxJ8j_FZD8gkcEA&hl=fr"></script>
<script type="text/javascript" src="<%=Helpers.stat("/static/track/elabel.js")%>"></script>
<script type="text/javascript" src="<%=Helpers.stat("/static/track/track.js")%>"></script>
