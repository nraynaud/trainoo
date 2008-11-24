<%@ page import="com.nraynaud.sport.data.BibPageData" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<a href="http://trainoo.com">Aller sur Trainoo.com</a>
<%
    final BibPageData data = property("model", BibPageData.class);
    if (data != null) {
        call(pageContext, "facebookTable.jsp", data);
    }
%>