<%@ page import="com.nraynaud.sport.web.action.SignupAction" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p class="loginLabel"><label for="password">Votre <s:property value="%{parameters.adjective}"/> mot de passe</label></p>

<p><s:password id="password" name="password" cssStyle="width:100%"/></p>
<p:javascript>makeItCount('password', <%= SignupAction.PASSWORD_MAX_LENGTH%>);</p:javascript>

<p class="loginLabel"><label for="passwordConfirmation">Confirmation de votre <s:property
        value="%{parameters.adjective}"/>
    mot de passe<br>
    (pour éviter les erreurs de frappe)</label></p>

<p><s:password id="passwordConfirmation" name="passwordConfirmation" cssStyle="width:100%"/></p>