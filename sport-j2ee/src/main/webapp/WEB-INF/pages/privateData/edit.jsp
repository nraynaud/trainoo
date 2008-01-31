<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Modification des données personnelles"/>

<div id="tinyCenter">
    <s:form>
        <s:actionerror/>
        <s:actionmessage/>
        <s:fielderror/>
        <s:hidden name="fromAction" value="%{actionDescription}"/>
        <p class="loginLabel"><label for="oldPassword">Votre mot de passe actuel</label></p>

        <p><s:password id="oldPassword" name="oldPassword" cssStyle="width:100%"/></p>
        <% Helpers.call(pageContext, "passwordAndConfirm.jsp", null, "adjective", "'nouveau'"); %>
        <s:submit value="Valider !"/>
    </s:form>

    <p style="text-align:right;"><a href="<s:url action="workouts" namespace="/"/>">Revenir à mon vestiaire</a></p>
</div>