<%@ page import="static com.nraynaud.sport.web.action.SignupAction.*" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Inscrivez-vous !"/>
<div class="content">
    <p>Vous pourrez suivre votre vie sportive et rencontrer d'autres amateurs comme vous&nbsp;!</p>

    <div id="tinyCenter">
        <s:form action="signup">
            <s:actionerror/>
            <s:fielderror/>
            <s:hidden name="fromAction" value="%{fromAction}"/>
            <p class="loginLabel"><label for="login">Votre surnom <span
                    class="labelComplement">(C'est par ce nom que vous serez visible sur tout le site)</span></label>
            </p>

            <s:textfield id="login" name="login" cssStyle="width:100%"/>
            <p:javascript>$('login').focus();</p:javascript>
            <p:javascript>makeItCount('login', <%= LOGIN_MAX_LENGTH%>, <%= LOGIN_MIN_LENGTH%>);</p:javascript>
            <% Helpers.call(pageContext, "passwordAndConfirm.jsp", null); %>

            <p style="text-align:right;"><s:submit value="Inscription !"/></p>
            <fieldset style="width:80%; font-size:11px; margin:auto;">
                <legend>Optionnel</legend>
                <p class="loginLabel  tinyLabel" style="padding-top:0"><label for="email">Votre e-mail <span
                        class="labelComplement">(si vous désirez recevoir un rappel de vos identifants)</span></label>
                </p>
                <s:textfield id="email" name="email" cssStyle="width:100%"/>
                <p class="loginLabel tinyLabel"><s:checkbox id="rememberMe" name="rememberMe"/><label for="rememberMe">Se
                    souvenir de moi <span
                        class="labelComplement">(décocher si ce n'est pas votre propre ordinateur)</span></label></p>
            </fieldset>
        </s:form>
    </div>
    Déjà un compte&nbsp;? <%=Helpers.loginUrl("Connectez-vous&nbsp;!")%>
</div>