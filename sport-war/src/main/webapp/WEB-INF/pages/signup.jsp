<%@ page import="static com.nraynaud.sport.web.action.SignupAction.*" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Inscription"/>
<div id="tinyCenter">
    <div class="block">
        <div class="content textContent">
            <form method="POST" action="<%=createUrl("/", "signup")%>">
                <s:actionerror/>
                <s:fielderror/>
                <input type="hidden" name="fromAction" value="<%=fromActionOrCurrent()%>"/>
                <span class="label">
                    <label for="login">Votre surnom</label>
                    <span class="help fullWidth">c'est par ce nom que vous serez visible sur tout le site</span>
                </span>
                <span class="input fullWidth">
                    <input name="login" id="login" class="text"/>
                </span>

                <p:javascript>$('login').focus();</p:javascript>
                <p:javascript>makeItCount('login', <%= LOGIN_MAX_LENGTH%>, <%= LOGIN_MIN_LENGTH%>);</p:javascript>
                <% call(pageContext, "passwordAndConfirm.jsp", null); %>

                <span class="actions">
                    <input type="submit" value="S'inscrire !" class="submit"/>
                </span>

                <fieldset>
                    <legend>Champs optionnels</legend>
                    <span class="label">
                        <label for="email">Votre e-mail</label>
                        <span class="help fullWidth">en cas de perte de vos identifiants</span>
                    </span>
                    <span class="input fullWidth">
                        <input name="email" id="email" class="text"/>
                    </span>
                    <span class="input precedingInput">
                        <input type="checkbox" class="checkbox" name="rememberMe" id="rememberMe" checked="checked"/>
                    </span>
                    <span class="label">
                        <label for="rememberMe">Se souvenir de moi</label>
                        <span class="help fullWidth">décocher si ce n'est pas votre propre ordinateur</span>
                    </span>
                </fieldset>
            </form>
        </div>
    </div>
</div>
