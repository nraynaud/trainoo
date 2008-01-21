<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Insrivez-vous !" loginHeader="false"/>
<h1>Inscrivez-vous&nbsp;!</h1>

<div class="aroundForm">
    <s:form action="signup">
        <s:actionerror/>
        <s:actionmessage/>
        <s:fielderror/>

        <p class="loginLabel"><label for="login">Votre surnom</label></p>

        <p><s:textfield id="login" name="login"/></p>
        <p:javascript>Field.activate('login');</p:javascript>
        <p class="loginLabel"><label for="password">Votre mot de passe</label></p>

        <p><s:password id="password" name="password"/></p>

        <p class="loginLabel"><label for="passwordConfirmation">Confirmation de votre mot de passe<br>
            (pour éviter les erreurs de frappe)</label></p>

        <p><s:password id="passwordConfirmation" name="passwordConfirmation"/></p>

        <p><s:submit value="Inscription !"/></p>

    </s:form>
</div>
Déjà un compte&nbsp;? <a href="<s:url action='login'/>">Identifiez-vous&nbsp;!</a>
