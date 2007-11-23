<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<head>
    <title>Insrivez-vous !</title>
    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="center">
    <h1>Votre compte :</h1>

    <div style="display:block;">
        <s:form action="signup">

            <s:actionerror/>
            <s:actionmessage/>
            <s:fielderror/>
            <s:label for="login">Votre surnom :</s:label>
            <s:textfield id="login" name="login"/><br/>

            <s:label for="password">Votre mot de passe :</s:label>
            <s:password id="password" name="password"/><br/>

            <s:label
                    for="passwordConfirmation">Confirmation de votre mot de passe (pour éviter les erreurs de frappe) :</s:label>
            <s:password id="passwordConfirmation" name="passwordConfirmation"/><br/>

            <s:submit value="Inscription !"/>
        </s:form>
    </div>
</div>


<script language="JavaScript" type="text/javascript"
        src="<s:url value="/static/prototype_packed.js"/>"></script>
<script language="JavaScript" type="text/javascript"
        src="<s:url value="/static/sport.js"/>"></script>
</body>
</html>