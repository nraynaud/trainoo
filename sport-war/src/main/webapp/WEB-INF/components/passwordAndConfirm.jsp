<%@ page import="static com.nraynaud.sport.web.actionsupport.PasswordAction.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p class="loginLabel"><label for="password">Votre <s:property value="%{parameters.adjective}"/> mot de passe</label></p>

<div><s:password id="password" name="password" cssStyle="width:100%"/></div>
<p:javascript>makeItCount('password', <%= PASSWORD_MAX_LENGTH%>, <%=PASSWORD_MIN_LENGTH%>);</p:javascript>

<p class="loginLabel" style="padding-top:0; margin-top:0"><label for="passwordConfirmation">Confirmation de votre
    <s:property
            value="%{parameters.adjective}"/>
    mot de passe <span class="labelComplement">(pour éviter les erreurs de frappe)</span></label></p>

<div><s:password id="passwordConfirmation" name="passwordConfirmation" cssStyle="width:100%"/></div>