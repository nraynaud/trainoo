<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Modification des données personnelles"/>

<div id="tinyCenter">

    <s:actionerror/>

    <h2>Changer votre e-mail de contact</h2>
    <div class="block">
        <div class="content textContent">
            <form method="POST" action="<%=createUrl("/privatedata", "changeEmail")%>" />

                <s:fielderror>
                    <s:param value="'email'"/>
                </s:fielderror>

                <span class="label">
                    <label for="email">Votre e-mail</label>
                    <span class="help fullWidth">vider la case pour le supprimer du site</span>
                </span>
                <span class="input fullWidth">
                    <input name="email" id="email" class="text" value="<%=escapedOrNull(stringProperty("email"), "")%>" />
                </span>
                
                <p:javascript>$('email').focus();</p:javascript>
                <span class="actions">
                    <input type="submit" class="submit" value="Valider !" />
                </span>
            </form>
        </div>
    </div>

    <h2>Changer de mot de passe</h2>
    <div class="block">
        <div class="content textContent">
            <form method="POST" action="<%=createUrl("/privatedata", "changePassword")%>" >
                
                <s:fielderror>
                    <s:param value="'oldPassword'"/>
                    <s:param value="'password'"/>
                </s:fielderror>

                <input type="hidden" name="fromAction" value="<%=stringProperty("actionDescription")%>"/>
                <span class="label">
                    <label for="oldPassword">Votre mot de passe actuel</label>
                </span>
                <span class="input fullWidth">
                    <input id="oldPassword" name="oldPassword" type="password" class="text" />
                </span>

                <% call(pageContext, "passwordAndConfirm.jsp", null, "adjective", "'nouveau'"); %>
                <span class="actions">
                    <input type="submit" class="submit" value="Valider !"/>
                </span>
            </form>
        </div>
    </div>

    <h2>Mon compte Nike+</h2>
    <div class="block">
        <div class="content textContent">
            <form method="POST"action="<%=createUrl("/privatedata", "changeNikePlus")%>" >

                <s:fielderror>
                    <s:param value="'nikePlusEmail'"/>
                    <s:param value="'nikePlusPassword'"/>
                </s:fielderror>

                <input type="hidden" name="fromAction" value="<%=stringProperty("actionDescription")%>"/>
                <span class="label">
                    <label for="nikePlusEmail">Votre e-mail Nike+</label>
                    <span class="help fullWidth">vider la case pour supprimer votre compte Nike+ du site mais pas vos entraînements</span>
                </span>
                <span class="input fullWidth">
                    <input name="nikePlusEmail" id="nikePlusEmail" class="text" 
                        value="<%=escapedOrNull(stringProperty("nikePlusEmail"), "")%>"/>
                </span>

                <span class="label">
                    <label for="nikePlusPassword">Votre mot de passe Nike+</label>
                </span>
                <span class="input fullWidth">
                    <input id="nikePlusPassword" name="nikePlusPassword" type="password" class="text"/>
                </span>

                <span class="actions">
                    <input type="submit" class="submit" value="Valider !"/>
                </span>
            </form>
        </div>
    </div>

    <div class="block">
        <div class="content textContent">
            <p><a href="<%=createUrl("/", "workouts")%>" title="Revenir à mon vestiaire">Revenir à mon vestiaire</a></p>
        </div>
    </div>
</div>
