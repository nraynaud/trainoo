<%@ page session="false" contentType="text/html; charset=UTF-8" %>
<%@ page import="com.nraynaud.sport.data.NewMessageData" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.DateHelper" %>
<%@ page import="com.nraynaud.sport.web.view.PageDetail" %>
<%@ page import="java.util.List" %>
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
<div id="header">
    <div id="logo"><a href="/" title="Trainoo.com - Tableau général">Trainoo.com</a></div>
    <div id="catchPhrase">Vous allez en suer&hellip;</div>
    <div id="adPlaceHolder">publicité google</div>
    <div id="topBar">
        <div id="secondaryTop">
            <form action="http://trainoo.com/search/" id="cse-search-box">
                <div>
                    <input type="hidden" name="cx" value="partner-pub-1788371406648361:36ti6n6fck5"/>
                    <input type="hidden" name="cof" value="FORID:10"/>
                    <input type="hidden" name="ie" value="UTF-8"/>
                    <input type="text" class="text" name="q" size="31"/>
                    <input type="submit" class="submit" name="sa" value="Rechercher"/>
                </div>
            </form>
            <script type="text/javascript"
                    src="http://www.google.com/coop/cse/brand?form=cse-search-box&amp;lang=fr"></script>
        </div>
        <div id="primaryTop">
            <% if (isLogged()) { %>
            <ul id="accountLinks">
                <li class="userName"><%=currentUser().getName()%>&nbsp;:</li>
                <li><a href="<%=createUrl("/privatedata", "")%>" title="Mon compte">Mon compte</a></li>
                <li><a href="<%=createUrl("/", "logout")%>" title="Déconnexion">Déconnexion</a></li>
            </ul>
            <% } else { %>
            <form action="<%=createUrl("/", "login", "fromAction", findFromAction())%>" method="POST">
                <ul id="accountLinks">
                    <li><label for="loginUpper">Surnom&nbsp;:</label><input name="login" id="loginUpper" class="text"/></li>
                    <li><label for="passwordUpper">Mot de passe&nbsp;:</label><input name="password" id="passwordUpper" type="password" class="text"/></li>
                    <li><input type="submit" class="submit" name="submit" value="Connexion"/></li>
                </ul>
                <% } %>
            </form>
        </div>

    </div>
</div>
<%}%>
<%call(pageContext, "menu.jsp");%>
<div id="content">
    <% if (isLogged()) {
        final List<NewMessageData> newMessages = property("newMessages", List.class);
        if (newMessages.size() > 0) {
            int count = 0;
            for (final NewMessageData privateMessage : newMessages) {
                count += privateMessage.messageCount;
            }%>
    <s:url id="messagesURL" action="" namespace="/messages">
        <s:param name="receiver" value="newMessages.get(0).sender"/>
    </s:url>
    <div id="infoPop">
        <a class="newMessages" href="<s:property value="%{messagesURL}"/>"><%=count + " " + pluralize(
                count,
                "nouveau message", "nouveaux messages")%>
        </a>
    </div>
    <% }
    } %>
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
        <a href="mailto:nicolas@trainoo.com">Une idée, une question&nbsp;?</a></p></div>
<%}%>

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
    <script type="text/javascript"><!--
    google_ad_client = "pub-1788371406648361";
    /* 468x60, trainoo_pimp */
    google_ad_slot = "8185304312";
    google_ad_width = 468;
    google_ad_height = 60;
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
