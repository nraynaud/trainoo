<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p:layoutParams title="Mon Dossard"/>


<p>Ville&nbsp;: <s:property value="town" default="<i>non précisé</i>"/></p>

<p>Moi&nbsp;: <s:property value="description" default="<i>non précisé</i>"/></p>

<p>Mon site&nbsp;: <%= formatUrl(property("webSite"), "<i>non précisé</i>")%>
</p>