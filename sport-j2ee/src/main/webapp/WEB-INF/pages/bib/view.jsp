<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.BibPageData" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<% final BibPageData data = (BibPageData) top();
    final User user = data.getUser();
    final boolean lookingOwnBib = currentUser().equals(user);%>
<p:layoutParams title="<%=lookingOwnBib ? "Mon dossard" : "Le dossard de " + escaped(user.getName())%>"/>

<div id="globalLeft">
    <% if (lookingOwnBib) {%>
    <p><a href="<s:url action="edit" namespace="/bib"/>">Mettre à jour</a></p>
    <%} else {%>
    <p>&nbsp;</p>
    <%}%>

    <% final String defaultValue = "<span class='unknown'>non précisé</span>"; %>
    <p>Ma ville&nbsp;: <%=escapedOrNull(user.getTown(), defaultValue)%>
    </p>

    <p>Moi&nbsp;: <%=escapedOrNull(user.getDescription(), defaultValue)%>
    </p>

    <p>Mon site&nbsp;: <%=formatUrl(user.getWebSite(), defaultValue)%>
    </p>
</div>
<%if (!lookingOwnBib) {%>
<div id="globalRight">
    <h2>Lui envoyer un message ?</h2>
    <s:component template="messages.jsp">
        <s:param name="receiver" value="%{user.name}"/>
    </s:component>
</div>
<%}%>