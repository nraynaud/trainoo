<%@ page import="static com.nraynaud.sport.web.view.PageDetail.pageDetailFor" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="r" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<head>
    <title><%= pageDetailFor(request).getTitle()%>
    </title>
    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="center">
    <% if (pageDetailFor(request).isLoginHeader()) {%>
    <s:component template="header.jsp"/>
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