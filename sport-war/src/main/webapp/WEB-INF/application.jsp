<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.nraynaud.sport.data.NewMessageData" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.DateHelper" %>
<%@ page import="com.nraynaud.sport.web.view.PageDetail" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="r" uri="/sport-tags" %>

<% final PageDetail pageDetail = PageDetail.detailFor(request); %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <!-- rev <%= PageDetail.class.getPackage().getImplementationVersion()%> -->
    <!-- date <%=DateHelper.today()%> -->
    <title><%= pageDetail.getTitle()%>
    </title>
    <meta name="verify-v1" content="yZTq8PJgPZNW+ohX4rJs4so6GlFfVS3hawur2jTQEIA=">
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

    <link href="<%=stat("/static/sport.css")%>" rel="stylesheet" type="text/css">
    <link href="<%=stat("/static/reset.css")%>" rel="stylesheet" type="text/css">
    <link href="<%=stat("/static/pimp/sport_pimp.css")%>" rel="stylesheet" type="text/css">
    <!--[if lt IE 7]>
        <link href="<%=stat("/static/sport_ie6.iecss")%>" rel="stylesheet" type="text/css">
    <![endif]-->
    <%
        for (final String header : pageDetail.getHeaders()) {
            out.append(header);
        }
    %>
</head>
<body class="<%= isLogged() ? "isLogged" : ""%>">
    <div id="body">
        <div id="center">
            <%if (pageDetail.isShowHeader()) {%>
            <%
                if (isLogged()) {
                    final int newMessagesCount = property("newMessagesTotal", Integer.class).intValue();
                    if (newMessagesCount > 0) {
            %>
            <div id="alertBox">
                <a href="<%=createUrl("/messages", "", "receiver", listProperty("newMessages", NewMessageData.class).get(0).sender.toString())%>"
                   title="Aller voir le 1er message">
                    Vous avez <%=newMessagesCount%> <%=pluralize(newMessagesCount, "nouveau message",
                        "nouveaux messages")%>
                </a>
            </div>
            <% }
            } %>
            <div id="header">
                <div id="logo"><a href="/" title="Trainoo.com - Tableau général">Trainoo.com</a></div>
                <div id="catchPhrase">Vous allez en suer&hellip;</div>
            </div>
            <%}%>
            <div id="contentWrapper">
                <%call(pageContext, "sidebar.jsp");%>
                <div id="content">
                    <div id="contentDecor"></div>
                    <%call(pageContext, "menu.jsp");%>
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
                <%if (pageDetail.isShowFooter()) {%>
                <div id="footer">
                    <hr>
                    <p><%=link("/", "about", "À propos de trainoo.com…", null)%> -
                        <a href="mailto:nicolas@trainoo.com">Une idée, une question&nbsp;?</a></p>
                </div>
                <%}%>
            </div>

            <script type="text/javascript" src="<%=stat("/static/prototype.js")%>"></script>
            <script type="text/javascript" src="<%=stat("/static/scriptaculous.js")%>"></script>
            <script type="text/javascript" src="<%=stat("/static/sport.js")%>"></script>
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
            <%if (pageDetail.isShowHeader()) {%>
            <div id="ad">
                <script type="text/javascript">
                    <!--
                    google_ad_client = "pub-1788371406648361";
                    /* 120x600, gratte-ciel */
                    google_ad_slot = "3759319574";
                    google_ad_width = 120;
                    google_ad_height = 600;
                    //-->
                </script>
                <script type="text/javascript"
                        src="http://pagead2.googlesyndication.com/pagead/show_ads.js"></script>
            </div>
            <script type="text/javascript">
                $('adPlaceHolder').update($('ad'));
            </script>
            <%}%>
        </div>
    </div>
</body>
</html>
