<%@ page import="static com.nraynaud.sport.web.action.SignupAction.*" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Inscrivez-vous !"/>
<p>Vous pourrez suivre votre vie sportive et rencontrer d'autres amateurs comme vous&nbsp;!</p>

<div id="tinyCenter">
    <s:form action="signup">
        <s:actionerror/>
        <s:fielderror/>
        <s:hidden name="fromAction" value="%{fromAction}"/>
        <p class="loginLabel"><label for="login">Votre surnom <span
                class="labelComplement">(C'est par ce nom que vous serez visible sur tout le site)</span></label></p>

        <s:textfield id="login" name="login" cssStyle="width:100%"/>
        <p:javascript>Field.activate('login');</p:javascript>
        <p:javascript>makeItCount('login', <%= LOGIN_MAX_LENGTH%>);</p:javascript>
        <% Helpers.call(pageContext, "passwordAndConfirm.jsp", null); %>
        <s:checkbox id="rememberMe" name="rememberMe"/><label for="rememberMe">Se souvenir de moi <span
            class="labelComplement">(décocher si ce n'est pas votre propre ordinateur)</span></label>

        <p><s:submit value="Inscription !"/></p>
    </s:form>
</div>
Déjà un compte&nbsp;? <a href="<s:url action='login'/>">Connectez-vous&nbsp;!</a>
