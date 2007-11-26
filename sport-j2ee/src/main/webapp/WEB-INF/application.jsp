<%@ page import="static com.nraynaud.sport.web.view.PageDetail.pageDetailFor" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.web.SessionUtil" %>
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
    <% final HttpSession session = ((HttpServletRequest) pageContext.getRequest()).getSession(false);
        final User user = SessionUtil.getUser(session);
        if (user != null) {%>
    <span id="loginName"><%= user.getName()%></span> |
    <s:form id="logoutForm" action="logout" method="POST">
        <s:submit cssClass="logoutButton" value="Déconnexion"/>
    </s:form>
    <% } else { %>
    <a href="<s:url action='login'/>">S'identifier</a> | <a href="<s:url action='signup'/>">S'inscrire</a>
    <%}%>
    <hr>

    <r:writeContent/>
</div>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/prototype_packed.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/sport.js"/>"></script>
<r:writeJavascript/>
</body>
</html>