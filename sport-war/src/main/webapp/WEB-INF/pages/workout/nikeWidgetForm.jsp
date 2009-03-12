<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <title>Votre widget Nike+</title>
        <meta name="verify-v1" content="yZTq8PJgPZNW+ohX4rJs4so6GlFfVS3hawur2jTQEIA=">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="icon" href="/static/favicon.ico" type="image/vnd.microsoft.icon">
        <link href="/static/sport.css" rel="stylesheet" type="text/css">
        <link href="/static/reset.css" rel="stylesheet" type="text/css">
        <link href="/static/pimp/sport_pimp.css" rel="stylesheet" type="text/css">
        <!--[if lt IE 7]>
            <link href="<%=Helpers.stat("/static/sport_ie6.iecss")%>" rel="stylesheet" type="text/css">
        <![endif]-->
    </head>
    <body>
        <div id="body">
            <div id="center">
                <div id="header">
                    <div id="logo"><a href="/" title="Trainoo.com - Tableau général">Trainoo.com</a></div>
                    <div id="catchPhrase">Vous allez en suer&hellip;</div>
                </div>
                <div id="contentWrapper">
                    <div id="content">
                        <div id="contentDecor"></div>
                        <h1>Récupérez votre widget Nike+ ici</h1>

                        <div class="block aboutBlock">
                            <h2>Collez ici l'adresse de partage de votre entraînement.</h2>
                            <img src="/static/nikeGraph/copier_lien.png" alt="">

                            <div style="font-size:25px">
                                <form action="" method="GET">
                                    <input type="text" name="url" style="width:80%">
                                    <input type="submit" value="envoyer !">
                                </form>
                            </div>
                        </div>
                    </div>
                    <div id="footer">
                        <hr>
                        <p><a href="/about">À propos de trainoo.com…</a> -
                            <a href="mailto:nicolas@trainoo.com">Une idée, une question&nbsp;?</a></p>
                    </div>
                </div>
            </div>
        </div>

        <script type="text/javascript" src="/static/prototype.js"></script>
        <script type="text/javascript" src="/static/sport.js"></script>

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
