<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Mot de passe oublié ?"/>

<div id="tinyCenter">
    <div class="block">
        <div class="content textContent">
            <form method="POST" action="<%=createUrl("/", "forgotPassword")%>" >
            <s:fielderror/>
            <s:actionerror/>
            <span class="label">
                <label for="email">Votre e-mail</label>
                <span class="help fullWidth">un nouveau mot de passe sera envoyé à cette adresse</span>
            </span>
            <span class="input fullWidth">
                <input id="email" name="email" class="text" />
            </span>
            <p:javascript>
                $('email').focus();
            </p:javascript>
            <span class="actions">
                <input type="submit" value="Envoyer !" class="submit" />
            </span>
            </form>
            <p>
            Si vous n'aviez pas saisi votre e-mail dans votre compte, contactez le <a
                href="mailto:support@trainoo.com">support</a>.
            </p>
        </div>
    </div>
</div>
