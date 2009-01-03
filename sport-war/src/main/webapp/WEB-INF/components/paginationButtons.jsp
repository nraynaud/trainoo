<%@ page import="com.nraynaud.sport.data.PaginatedCollection" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.view.PaginationView" %>
<%@ page import="com.nraynaud.sport.web.view.StackUtil" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div class="pagination">
    <%
        final PaginationView<?, ?> view = StackUtil.<PaginationView<?, ?>>top();
        final PaginatedCollection<?> collection = view.collection;
        if (collection.hasPrevious()) {
    %>
    <div class="paginationPrevious"><%=linkCurrenUrlWithParams("« Précédents", view.pageVariable,
            String.valueOf(collection.getPreviousIndex()))%>
    </div>
    <%
        }
        if (collection.hasNext()) {
    %>
    <div class="paginationNext"><%=linkCurrenUrlWithParams("Suivants »", view.pageVariable,
            String.valueOf(collection.getNextIndex()))%>
    </div>
    <%}%>
</div>
