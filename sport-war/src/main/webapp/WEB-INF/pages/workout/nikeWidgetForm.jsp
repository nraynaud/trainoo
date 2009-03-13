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
        <style type="text/css">
            ul {
                margin-left: 30px;
                list-style-position: outside;
                list-style-type: disc;
            }
        </style>
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
                        <h1>Affichez votre course Nike+ sous forme d'image !</h1>

                        <div class="block aboutBlock">
                            <div style="margin:40px;overflow:hidden; font-size:22px; line-height:130%">

                                <ul style="float:left;margin-top:40px;">
                                    <li>Pour votre blog</li>
                                    <li>Pour les forums</li>
                                    <li>Pas de couleur rouge !</li>
                                    <li>Pas de flash lent à charger !</li>
                                </ul>
                                <div style="display:block;float:right;">
                                    Exemple &nbsp;: <br>
                                    <img src="/static/nikeGraph/nike_example.png"
                                         alt="exemple de graphe">
                                </div>
                            </div>
                            <h2>1) Copiez l'adresse de partage de la course sur le site de Nike+</h2>

                            <div style="width:100%; text-align:center;"><img style="margin: auto auto;"
                                                                             src="/static/nikeGraph/copier_lien.gif"
                                                                             alt="anim lien copie">
                            </div>
                            <h2>2) Collez-la ci-dessous&nbsp;:</h2>

                            <div style="font-size:25px; margin-top:30px; margin-bottom:30px;">
                                <form action="" method="GET">
                                    <input type="text" id="urlInput" name="url" style="width:80%">
                                    <input type="submit" value="Envoyer !">
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
        <script type="text/javascript">
            document.getElementById("urlInput").focus();
        </script>
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
