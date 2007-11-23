<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<head>
    <title>Identification</title>
    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="center">
    <h1>Votre compte :</h1>

    <div style="display:block;">
        <s:form action="login">

            <s:actionerror/>
            <s:actionmessage/>
            <s:fielderror/>
            
            <s:label for="login">Votre surnom :</s:label>
            <s:textfield id="login" name="login"/><br/>

            <s:label for="password">Votre mot de passe :</s:label>
            <s:password id="password" name="password"/><br/>

            <s:submit value="entrez !"/>
        </s:form>
    </div>
    Pas encore de compte&nbsp;? <a href="<s:url action='signup'/>">Inscrivez-vous&nbsp;!</a>
</div>


<script language="JavaScript" type="text/javascript"
        src="<s:url value="/static/prototype_packed.js"/>"></script>
<script language="JavaScript" type="text/javascript"
        src="<s:url value="/static/sport.js"/>"></script>
</body>
</html>