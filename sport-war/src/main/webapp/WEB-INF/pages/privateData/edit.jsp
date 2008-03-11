<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Modification des données personnelles"/>

<div id="tinyCenter">
    <fieldset>
        <legend>Changer votre e-mail de contact</legend>
        <s:form action="changeEmail" namespace="/privatedata">
            <p><label for="email">Votre nouvelle adresse <span class="labelComplement">(videz la case pour le
                supprimer du site)</span></label>
            </p>

            <p><s:textfield id="email" name="email" cssStyle="width:100%"/></p>

            <p><s:submit id="submit" value="Valider !"/></p>
        </s:form>
    </fieldset>
    <fieldset>
        <legend>Changer de mot de passe</legend>
        <s:form name="privatedata_form" id="privatedata_form" action="changePassword" namespace="/privatedata">
            <s:actionerror/>
            <s:fielderror/>
            <s:hidden id="fromAction" name="fromAction" value="%{actionDescription}"/>
            <p><label for="oldPassword">Votre mot de passe actuel</label></p>

            <p><s:password id="oldPassword" name="oldPassword" cssStyle="width:100%"/></p>
            <p:javascript>$('oldPassword').focus();</p:javascript>
            <% Helpers.call(pageContext, "passwordAndConfirm.jsp", null, "adjective", "'nouveau'"); %>
            <p><s:submit id="submit" value="Valider !"/></p>
        </s:form>
    </fieldset>

    <p style="text-align:right;"><a href="<s:url action="workouts" namespace="/"/>">Revenir à mon vestiaire</a></p>
</div>