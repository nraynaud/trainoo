<%@ page import="static com.nraynaud.sport.web.view.Helpers.signupUrl" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Connectez-vous !"/>

<div id="tinyCenter">
    <s:form id="login_form" action="login">

        <s:actionerror/>
        <s:fielderror/>
        <s:hidden name="fromAction" value="%{fromAction}"/>
        <p class="loginLabel"><label for="login">Votre surnom</label></p>

        <s:textfield id="login" name="login" cssStyle="width:100%;"/>

        <p class="loginLabel"><label for="password">Votre mot de passe</label></p>

        <p><s:password id="password" name="password" cssStyle="width:100%;"/></p>

        <p class="loginLabel"><s:checkbox id="rememberMe" name="rememberMe"/><label for="rememberMe">Se souvenir de moi <span
                class="labelComplement">(décocher si ce n'est pas votre propre ordinateur)</span></label></p>

        <p><s:submit value="Entrez !"/></p>
    </s:form>
</div>
Pas encore de compte&nbsp;? <%=signupUrl("Inscrivez-vous&nbsp;!")%>
<p:javascript>Field.activate('login');</p:javascript>