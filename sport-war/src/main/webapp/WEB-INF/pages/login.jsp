<%@ page import="static com.nraynaud.sport.web.view.Helpers.signupUrl" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Connexion"/>

<div id="tinyCenter">
    <div class="block">
        <div class="content textContent">
            <form method="POST" action="<%=createUrl("/", "login")%>" >
                <s:actionerror/>
                <s:fielderror/>
                <input type="hidden" name="fromAction" value="<%=stringProperty("fromAction")%>"/>
                <span class="label">
                    <label for="login">Votre surnom</label>
                </span>
                <span class="input fullWidth">
                    <input name="login" id="login" class="text" />
                </span>
                
                <p:javascript>$('login').focus();</p:javascript>

                <span class="label">
                    <label for="password">Votre mot de passe</label>
                </span>
                <span class="input fullWidth">
                    <input id="password" name="password" type="password" class="text"/>
                </span>

                <span class="actions">
                    <input type="submit" class="submit" value="Entrer !" class="submit"/>
                </span>

                <span class="input precedingInput">
                    <input type="checkbox" class="checkbox" name="rememberMe" id="rememberMe" checked="checked" />
                </span>
                <span class="label">
                    <label for="rememberMe">Se souvenir de moi</label>
                    <span class="help fullWidth">décocher si ce n'est pas votre propre ordinateur</span>
                </span>
            </form>
            <p>Pas encore de compte&nbsp;? <%=signupUrl("Inscrivez-vous&nbsp;!")%>
            </p>
        </div>
    </div>
</div>
