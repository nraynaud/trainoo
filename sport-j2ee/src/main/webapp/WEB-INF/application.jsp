<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.nraynaud.sport.web.view.PageDetail" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="r" uri="/sport-tags" %>

<% final PageDetail pageDetail = PageDetail.detailFor(request); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- rev <%= PageDetail.class.getPackage().getImplementationVersion()%> -->
    <title><%= pageDetail.getTitle()%>
    </title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css">
</head>
<body>
<div id="center">
    <div style="overflow:hidden;">
        <h1 id="logo">
            <a id="logoHref" href="/">
                <canvas id="gradient" width="1" height="50">
                </canvas>
                Train<span style="color:#968148;">oo</span>.com</a>
        </h1>
    </div>

    <% if (pageDetail.isLoginHeader()) {%>
    <s:component template="loginHeader.jsp"/>
    <% } %>
    <div style="position:relative;">
        <div id="adPlaceHolder">
        </div>
        <div id="content">
            <h1><%=pageDetail.getTitle()%>
            </h1>
            <r:writeContent/>
        </div>
        <hr id="bottom">
        <div><p class="smaller" style="text-align:center;">Copyright Nicolas Raynaud 2008.
            <a href="mailto:nicolas@trainoo.com">Une idée, une question&nbsp;?</a></p></div>


        <script type="text/javascript" src="<s:url value="/static/prototype_packed.js"/>"></script>
        <script type="text/javascript" src="<s:url value="/static/scriptaculous.js"/>"></script>
        <script type="text/javascript" src="<s:url value="/static/sport.js"/>"></script>

        <r:writeJavascript/>
        <!--[if lt IE 7]>
        <script type="text/javascript">
            fixPNGIE();
        </script>
        <![endif]-->
        <script type="text/javascript">
            var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
            document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
        </script>
        <script type="text/javascript">
            var pageTracker = _gat._getTracker("UA-3412937-1");
            pageTracker._initData();
            pageTracker._trackPageview();
        </script>
        <script type="text/javascript">
            var canvas = document.getElementById('gradient');
            var height = $('logoHref').getHeight();
            canvas.height = height
            var ctx = canvas.getContext('2d');
            var lineargradient = ctx.createLinearGradient(0, 0, 0, height);
            lineargradient.addColorStop(0.8, 'rgba(255,255,255,0)');
            lineargradient.addColorStop(0, '#F2F2E8');
            //document.getElementById('center').backgroundColor);
            ctx.clearRect(0, 0, 1, height);
            ctx.fillStyle = lineargradient;
            ctx.fillRect(0, 0, 1, height);
        </script>
        <div id="ad">
            <script type="text/javascript"><!--
            google_ad_client = "pub-6101279689689980";
            google_ad_slot = "7291082437";
            google_ad_width = 468;
            google_ad_height = 60;
            //--></script>
            <script type='text/javascript' src='http://pagead2.googlesyndication.com/pagead/show_ads.js'
                    defer="defer"></script>
        </div>
    </div>
</div>
</body>
</html>