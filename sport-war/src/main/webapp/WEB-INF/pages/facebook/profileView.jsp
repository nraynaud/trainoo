<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.BibPageData" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final BibPageData data = property("model", BibPageData.class);
    if (data != null) {
        call(pageContext, "facebookTable.jsp", data);
    }
%>