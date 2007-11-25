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
    <h1>Enregistrement</h1>

    <div class="aroundForm">
        <s:form action="signup">
            <s:actionerror/>
            <s:actionmessage/>
            <s:fielderror/>

            <p><label for="login">Votre surnom&nbsp;:</label>
                <s:textfield id="login" name="login"/></p>

            <p><label for="password">Votre mot de passe&nbsp;:</label>
                <s:password id="password" name="password"/></p>

            <p><label for="passwordConfirmation">Confirmation de votre mot de passe&nbsp;:<br>
                (pour éviter les erreurs de frappe)</label>
                <s:password id="passwordConfirmation" name="passwordConfirmation"/></p>

            <p><s:submit value="Inscription !"/></p>

        </s:form>
    </div>
    Déjà un compte&nbsp;? <a href="<s:url action='login'/>">Identifiez-vous&nbsp;!</a>
</div>

<script language="JavaScript" type="text/javascript" src="<s:url value="/static/prototype_packed.js"/>"></script>
<script language="JavaScript" type="text/javascript" src="<s:url value="/static/sport.js"/>"></script>
<script language="JavaScript" type="text/javascript">Field.activate('login');</script>
</body>
</html>