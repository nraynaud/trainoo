<%@ page import="static com.nraynaud.sport.web.action.SignupAction.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Insrivez-vous !" loginHeader="false"/>
<p>Vous pourrez suivre votre vie sportive et rencontrer d'autres amateurs comme vous&nbsp;!</p>

<div id="tinyCenter">
    <s:form action="signup">
        <s:actionerror/>
        <s:actionmessage/>
        <s:fielderror/>

        <p class="loginLabel"><label for="login">Votre surnom</label></p>

        <p><s:textfield id="login" name="login" cssStyle="width:100%"/></p>
        <p:javascript>Field.activate('login');</p:javascript>
        <p:javascript>makeItCount('login', <%= LOGIN_MAX_LENGTH%>);</p:javascript>
        <p class="loginLabel"><label for="password">Votre mot de passe</label></p>

        <p><s:password id="password" name="password" cssStyle="width:100%"/></p>
        <p:javascript>makeItCount('password', <%= PASSWORD_MAX_LENGTH%>);</p:javascript>

        <p class="loginLabel"><label for="passwordConfirmation">Confirmation de votre mot de passe<br>
            (pour éviter les erreurs de frappe)</label></p>

        <p><s:password id="passwordConfirmation" name="passwordConfirmation" cssStyle="width:100%"/></p>

        <p><s:submit value="Inscription !"/></p>
    </s:form>
</div>
Déjà un compte&nbsp;? <a href="<s:url action='login'/>">Connectez-vous&nbsp;!</a>
