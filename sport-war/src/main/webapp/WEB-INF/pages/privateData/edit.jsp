<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Modification des données personnelles"/>

<div id="tinyCenter">
    <s:form name="privatedata_form" id="privatedata_form" action="" namespace="/privatedata">
        <s:actionerror/>
        <s:fielderror/>
        <s:hidden id="fromAction" name="fromAction" value="%{actionDescription}"/>
        <p class="loginLabel"><label for="oldPassword">Votre mot de passe actuel</label></p>

        <p><s:password id="oldPassword" name="oldPassword" cssStyle="width:100%"/></p>
        <p:javascript>$('oldPassword').focus();</p:javascript>
        <% Helpers.call(pageContext, "passwordAndConfirm.jsp", null, "adjective", "'nouveau'"); %>
        <p><s:submit id="submit" value="Valider !"/></p>
    </s:form>

    <p style="text-align:right;"><a href="<s:url action="workouts" namespace="/"/>">Revenir à mon vestiaire</a></p>
</div>