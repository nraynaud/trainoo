<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="J'ai oublié mon mot de passe."/>

<div id="tinyCenter" class="content">
    <s:form namespace="/" action="forgotPassword">
        <fieldset>
            <legend>Re-générer mon mot de passe</legend>
            <s:fielderror/>
            <s:actionerror/>
            <p><label for="email">Adresse e-mail&nbsp;:</label></p>

            <p><s:textfield id="email" name="email" cssStyle="width:100%"/></p>
            <p:javascript>$('email').focus();</p:javascript>

            <s:submit value="envoyer !"/>
        </fieldset>
    </s:form>
    Si vous n'aviez pas saisi votre e-mail dans votre compte, contactez le <a
        href="mailto:support@trainoo.com">support</a>.
</div>