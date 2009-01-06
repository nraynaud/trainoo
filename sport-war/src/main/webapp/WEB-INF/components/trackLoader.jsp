<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<div style="visibility:hidden">
    <img id="map_handle" src="<%=stat("/static/track/map_handle.png")%>" alt="">
    <img id="map_handle_active" src="<%=stat("/static/track/map_handle_active.png")%>" alt="">
    <img id="map_marker" src="<%=stat("/static/track/map_marker.png")%>" alt="">
    <img id="map_marker_active" src="<%=stat("/static/track/map_marker_active.png")%>" alt="">
</div>

<%
    final String localKey = "ABQIAAAAB_pY09DNgzbhAapu-Taq3BQ2JGlzsnS7jQe2K3BOHN-eIIg7qxTdWxIGQzbXObDxDX77K7MZDK5a1Q";
    final String serverKey = "ABQIAAAAB_pY09DNgzbhAapu-Taq3BTtRcdkEu7NTWhsViqteywQ9rYraBTFFcawlKj7SSAwxJ8j_FZD8gkcEA";
    final String key = pageContext.getRequest().getLocalName().equals("192.168.0.20") ? localKey : serverKey;
%>
<script type="text/javascript"
        src="http://www.google.com/maps?file=api&v=2&key=<%=key%>&hl=fr"></script>

<p:javascript src='<%=stat("/static/track/geo.js")%>'/>
<p:javascript src='<%=stat("/static/track/trackCommon.js")%>'/>