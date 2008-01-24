<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<% final boolean isCurrentUser = currentUser().equals(top());
    final String title = isCurrentUser ? "Mon dossard" : "Le dossard de " + escaped("name"); %>
<p:layoutParams title="<%=title%>"/>

<% final String defaultValue = "<span class='unknown'>non précisé</span>"; %>

<% if (isCurrentUser) {%>
<p><a href="<s:url action="edit" namespace="/bib"/>">Mettre à jour</a></p>
<%} else {%>
<p>&nbsp;</p>
<%}%>

<p>Ma ville&nbsp;: <%=escapedOrNull("town", defaultValue)%>
</p>

<p>Moi&nbsp;: <%=escapedOrNull("description", defaultValue)%>
</p>

<p>Mon site&nbsp;: <%=formatUrl(property("webSite"), defaultValue)%>
</p>
