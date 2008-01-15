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
    <script type="text/javascript"><!--
    google_ad_client = "pub-6101279689689980";
    //728x90, date de création 14/01/08
    google_ad_slot = "0714252817";
    google_ad_width = 728;
    google_ad_height = 90;
    //--></script>
</head>
<body>
<div id="center">
    <div id="logo"><a href="/">Train<span style="color:#968148;">oo</span>.fr</a></div>

    <% if (pageDetail.isLoginHeader()) {%>
    <s:component template="loginHeader.jsp"/>
    <% } %>
    <script type="text/javascript"
            src="http://pagead2.googlesyndication.com/pagead/show_ads.js">
    </script>

    <r:writeContent/>
    <hr>
    <div><p style="text-align:center;">Copyright Nicolas Raynaud 2008. Une idée, une question&nbsp;? <a
            href="mailto://trainoo@trainoo.com">Par ici&nbsp;!</a></p></div>
</div>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/prototype_packed.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/sport.js"/>"></script>
<r:writeJavascript/>
</body>
</html>