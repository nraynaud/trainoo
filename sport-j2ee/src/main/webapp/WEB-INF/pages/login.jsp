<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:defineTitle value="Identification"/>
<h1>Identification</h1>

<div class="aroundForm">
    <s:form id="login_form" action="login">

        <s:actionerror/>
        <s:actionmessage/>
        <s:fielderror/>

        <p><label for="login">Votre surnom&nbsp;:</label>
            <s:textfield id="login" name="login"/><br/></p>

        <p><label for="password">Votre mot de passe&nbsp;:</label>
            <s:password id="password" name="password"/><br/></p>

        <p><s:submit value="Entrez !"/></p>
    </s:form>
</div>
Pas encore de compte&nbsp;? <a href="<s:url action='signup'/>">Inscrivez-vous&nbsp;!</a>
<p:javascript>Field.activate('login');</p:javascript>