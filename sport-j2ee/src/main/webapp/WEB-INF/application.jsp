<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.nraynaud.sport.web.view.PageDetail" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="r" uri="/sport-tags" %>

<% final PageDetail pageDetail = PageDetail.detailFor(request); %>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
    <title><%= pageDetail.getTitle()%>
    </title>
    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css">
</head>
<body>
<div id="center">
    <div id="logo"><a href="/"> BougeTonBoule.com</a></div>
    
    <% if (pageDetail.isLoginHeader()) {%>
    <s:component template="loginHeader.jsp"/>
    <% } %>
    <r:writeContent/>
</div>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/prototype_packed.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/sport.js"/>"></script>
<r:writeJavascript/>
</body>
</html>