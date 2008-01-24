<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<% final String login = escaped("name"); %>
<p:layoutParams title="<%="Le dossard de " + login%>"/>
<% final String defaultValue = "<span class='unknown'>non précisé</span>"; %>

<p>Ville&nbsp;: <%=escapedOrNull("town", defaultValue)%>
</p>

<p>Moi&nbsp;: <%=escapedOrNull("description", defaultValue)%>
</p>

<p>Mon site&nbsp;: <%= formatUrl(property("webSite"), defaultValue)%>
</p>
