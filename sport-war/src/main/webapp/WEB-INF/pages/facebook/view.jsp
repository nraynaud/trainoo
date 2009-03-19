<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Trainoo.com arrive bientôt sur Facebook</title>
        <link rel="icon" href="<%=stat("/static/favicon.ico")%>" type="image/vnd.microsoft.icon">
        <link href="<%=stat("/static/sport.css")%>" rel="stylesheet" type="text/css">
        <link href="<%=stat("/static/reset.css")%>" rel="stylesheet" type="text/css">
        <link href="<%=stat("/static/pimp/sport_pimp.css")%>" rel="stylesheet" type="text/css">
        <!--[if lt IE 7]>
        <link href="<%=stat("/static/sport_ie6.iecss")%>" rel="stylesheet" type="text/css">
    <![endif]-->
        <style type="text/css">
            #contentWrapper {
                background: #F7F7E2;
                overflow: hidden;
                margin: 20px 20px 20px 20px;
            }
        </style>
    </head>
    <body>
        <div id="body">
            <div id="center" style="width:inherit;">
                <img src="<%=stat("/static/pimp/logo.png")%>" alt="logo">

                <div id="contentWrapper">
                    <div></div>
                    <div id="content">
                        <h1><a href="http://trainoo.com" target="_parent">Trainoo.com</a> arrive bientôt sur facebook
                        </h1>

                        <h2>Continuez à vous entraîner en attendant !</h2>
                    </div>
                </div>
            </div>
        </div>
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