<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Insrivez-vous !" loginHeader="false"/>
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

<p:javascript>Field.activate('login');</p:javascript>