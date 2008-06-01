<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.nraynaud.sport.data.PaginatedCollection" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.view.PaginationView" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final PaginatedCollection collection = top(PaginationView.class).collection;
    final String pageVariable = property("pageVariable", String.class);
    if (collection.hasPrevious()) {
%>
<div class="paginationPrevious"><%=currenUrlWithParams("&lt;&lt;-Précédents", false, pageVariable,
        String.valueOf(collection.getPreviousIndex()))%>
</div>
<%
    }
    if (collection.hasNext()) {
%>
<div class="paginationNext"><%=currenUrlWithParams("Suivants->>", false, pageVariable,
        String.valueOf(collection.getNextIndex()))%>
</div>
<%}%>