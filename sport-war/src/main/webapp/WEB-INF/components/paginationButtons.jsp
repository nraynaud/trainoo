<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.nraynaud.sport.data.PaginatedCollection" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.view.PaginationView" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.view.StackUtil" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div class="pagination">
    <%
        final PaginatedCollection<?> collection = StackUtil.<PaginationView<?, ?>>top().collection;
        final String pageVariable = stringProperty("pageVariable");
        if (collection.hasPrevious()) {
    %>
    <div class="paginationPrevious"><%=linkCurrenUrlWithParams("« Précédents", pageVariable,
            String.valueOf(collection.getPreviousIndex()))%>
    </div>
    <%
        }
        if (collection.hasNext()) {
    %>
    <div class="paginationNext"><%=linkCurrenUrlWithParams("Suivants »", pageVariable,
            String.valueOf(collection.getNextIndex()))%>
    </div>
    <%}%>
</div>
