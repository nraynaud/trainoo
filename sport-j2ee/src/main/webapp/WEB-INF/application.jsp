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
    //468x60, trainoo
    google_ad_slot = "7291082437";
    google_ad_width = 468;
    google_ad_height = 60;
    //--></script>
</head>
<body>
<div id="center">
    <div id="logo"><a href="/">Train<span style="color:#968148;">oo</span>.com</a></div>

    <% if (pageDetail.isLoginHeader()) {%>
    <s:component template="loginHeader.jsp"/>
    <% } %>

    <div id="ad">
        <script type='text/javascript' src='http://pagead2.googlesyndication.com/pagead/show_ads.js' defer="defer"></script>
    </div>
    <div id="content">
        <r:writeContent/>
    </div>
    <hr id="bottom">
    <div><p class="smaller" style="text-align:center;">Copyright Nicolas Raynaud 2008. Une idée, une question&nbsp;? <a
            href="mailto:nicolas@trainoo.com">Par ici&nbsp;!</a></p></div>
</div>
<script type="text/javascript" src="<s:url value="/static/prototype_packed.js"/>"></script>
<script type="text/javascript" src="<s:url value="/static/sport.js"/>"></script>
<r:writeJavascript/>
<script type="text/javascript">
    var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
    document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
    var pageTracker = _gat._getTracker("UA-3412937-1");
    pageTracker._initData();
    pageTracker._trackPageview();
</script>
</body>
</html>