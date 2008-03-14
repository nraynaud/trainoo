<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.nraynaud.sport.web.ActionDetail" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page import="com.nraynaud.sport.web.view.PageDetail" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>
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
    <!--[if IE ]>
    <style>
    #gradient {
      background: none;
      filter: progid:DXImageTransform.Microsoft.AlphaImageLoader(src='/static/gradient.png', sizingMethod='scale');
    }
    </style>
    <![endif]-->

    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css">
</head>
<body>
<div id="center">
    <div style="overflow:hidden;width: 100%">
        <h1 id="logo">
            <span style="position:relative;display:block;"><a id="logoHref" href="/">
                Train<span id="oo">oo</span>.com</a></span>
        </h1>

        <div id="catchPhrase" style="text-align:right;">
            <span style="font-size:10px"><%=new SimpleDateFormat().format(new Date())%></span><br/>
            Vous allez en suer&nbsp;!
        </div>
    </div>
    <%Helpers.call(pageContext, "loginHeader.jsp");%>
    <div style="position:relative;">
        <div id="adPlaceHolder">
        </div>
        <div id="content">
            <%if (pageDetail.isShowTitle()) {%>
            <h1><%=pageDetail.getTitle()%>
            </h1>
            <%}%>
            <s:actionmessage/>
            <% /* the page content*/
                final PageDetail detail = (PageDetail) pageContext.getRequest().getAttribute("detail");
                out.append(detail.getContent());
            %>
        </div>
        <hr id="bottom">
        <div><p class="smaller" style="text-align:center;">Copyright Nicolas Raynaud 2008.
            <a href="mailto:nicolas@trainoo.com">Une idée, une question&nbsp;?</a></p></div>

        <script type="text/javascript" src="<s:url value="/static/prototype.js"/>"></script>
        <script type="text/javascript" src="<s:url value="/static/scriptaculous.js"/>"></script>

        <!--[if !IE]>-->
        <script type="text/javascript">
            var canvas = new Element('canvas', {'id': 'gradient'});
            $('logoHref').insert(canvas);
            var height = $('logoHref').getHeight();
            var width = $('logoHref').getWidth();
            canvas.height = height
            canvas.width = width
            var ctx = canvas.getContext('2d');
            var lineargradient = ctx.createLinearGradient(0, 0, 0, height);
            lineargradient.addColorStop(0.5, 'rgba(255,255,255,0)');
            lineargradient.addColorStop(0, 'white');
            ctx.fillStyle = lineargradient;
            ctx.fillRect(0, 0, width, height);
        </script>
        <!--<![endif]-->
        <!--[if IE]>
        <script type="text/javascript">
              $('logoHref').insert("<div id='gradient'><"+"/div>");
        </script>
        <![endif]-->
        <script type="text/javascript" src="<s:url value="/static/sport.js"/>"></script>
        <r:writeJavascript/><% final ActionDetail actionDetail = Helpers.property("actionDescription",
            ActionDetail.class); %>

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
        <script type="text/javascript" src="http://nraynaud.fr/clickheat/js/clickheat.js"></script>
        <noscript><a href="http://www.labsmedia.com/index.html">Open source tools</a></noscript>
        <script type="text/javascript"><!--
        clickHeatSite = 'trainoo.com';
        clickHeatGroup = '<%=actionDetail.namespace +'/' + actionDetail.name%>';
        clickHeatServer = 'http://nraynaud.fr/clickheat/click.php';
        initClickHeat();
        //-->
        </script>

        <div id="ad">
            <script type="text/javascript"><!--
            google_ad_client = "pub-1788371406648361";
            /* trainoo */
            google_ad_slot = "2814569948";
            google_ad_width = 468;
            google_ad_height = 60;
            //-->
            </script>
            <script type="text/javascript" src="http://pagead2.googlesyndication.com/pagead/show_ads.js"></script>
        </div>
    </div>
</div>
</body>
</html>