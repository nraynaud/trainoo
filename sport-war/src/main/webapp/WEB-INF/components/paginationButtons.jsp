<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.nraynaud.sport.web.view.PaginationView" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final PaginationView collection = top(PaginationView.class);
    if (collection.collection.hasPrevious()) {
%>
<s:url id="previousPageUrl" includeParams="get">
    <s:param name="%{pageVariable}" value="collection.previousIndex"/>
</s:url>
<div class="paginationPrevious"><s:a href="%{previousPageUrl}">&lt;&lt;-Précédents</s:a></div>
<%}%>
<%
    if (collection.collection.hasNext()) {
%>
<s:url id="nextPageUrl" includeParams="get">
    <s:param name="%{pageVariable}" value="collection.nextIndex"/>
</s:url>
<div class="paginationNext"><s:a href="%{nextPageUrl}">Suivants->></s:a></div>
<%}%>