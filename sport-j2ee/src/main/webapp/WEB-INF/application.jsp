<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<head>
    <title>Liste des Entraînements</title>
    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="center">
    <span id="loginName"><s:property value="userName"/></span> |
    <s:form id="logoutForm" action="logout" method="POST">
        <s:submit cssClass="logoutButton" value="Déconnexion"/>
    </s:form>
    <hr>
    
    <s:component template="%{jspContent}" templateDir="WEB-INF"/>
</div>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/prototype_packed.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/sport.js"/>"></script>
</body>
</html>