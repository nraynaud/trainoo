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

<div id="<%= lookingOwnBib ? "tinyCenter" : "globalLeft"%>">
    <% if (lookingOwnBib) {%>
    <p><a href="<s:url action="edit" namespace="/bib"/>">Mettre à jour</a></p>
    <%} else {%>
    <p>&nbsp;</p>
    <%}%>

    <% final String defaultValue = "non précisé";
        final String town = user.getTown();
        final String townLabel = "Ma ville";%>
    <table class="displayFormLayoutTable">
        <tr>
            <td><span class="label"><%=townLabel%>&nbsp;:</span></td>
            <td><span class="<%=className(town)%>"><%=escapedOrNull(town, defaultValue)%></span></td>
        </tr>
        <tr>
            <td><span class="label">Moi&nbsp;:</span></td>
            <td><span class="<%=className(user.getDescription())%>"><%=escapedOrNullmultilines(user.getDescription(),
                    defaultValue)%></span></td>
        </tr>
        <tr>
            <td><span class="label">Mon site&nbsp;: </span></td>
            <td><span class="<%=className(user.getDescription())%>"><%=formatUrl(user.getWebSite(),
                    defaultValue)%></span></td>
        </tr>
    </table>
</div>
<%if (!lookingOwnBib) {%>
<div id="globalRight">
    <h2>Lui envoyer un message ?</h2>
    <s:component template="messages.jsp">
        <s:param name="receiver" value="%{user.name}"/>
    </s:component>
</div>
<%}%>
<%!
    private static String className(final String town) {
        return town == null ? "serverDefault" : "userSupplied";
    }
%>