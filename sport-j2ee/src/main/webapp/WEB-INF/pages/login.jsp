<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Identification" loginHeader="false"/>
<h1>Identification</h1>

<div class="aroundForm">
    <s:form id="login_form" action="login">

        <s:actionerror/>
        <s:actionmessage/>
        <s:fielderror/>

        <p class="loginLabel"><label for="login">Votre surnom</label></p>

        <p><s:textfield id="login" name="login"/></p>

        <p class="loginLabel"><label for="password">Votre mot de passe</label></p>

        <p><s:password id="password" name="password"/></p>

        <p><s:submit value="Entrez !"/></p>
    </s:form>
</div>
Pas encore de compte&nbsp;? <a href="<s:url action='signup'/>">Inscrivez-vous&nbsp;!</a>
<p:javascript>Field.activate('login');</p:javascript>