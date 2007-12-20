<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.nraynaud.sport.web.view.PageDetail" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="r" uri="/sport-tags" %>

<% final PageDetail pageDetail = PageDetail.detailFor(request); %>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<head>
    <title><%= pageDetail.getTitle()%>
    </title>
    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="center">
    <% if (pageDetail.isLoginHeader()) {%>
    <s:component template="loginHeader.jsp"/>
    <% } %>
    <hr>
    <r:writeContent/>
    <hr>
    <a href="<s:url action='globalWorkouts'/>">Les entraînements de tout le monde</a>
</div>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/prototype_packed.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/sport.js"/>"></script>
<r:writeJavascript/>
</body>
</html>