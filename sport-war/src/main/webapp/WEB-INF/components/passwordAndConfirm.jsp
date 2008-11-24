<%@ page import="static com.nraynaud.sport.web.actionsupport.PasswordAction.*" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<span class="label">
    <label for="password">Votre <%=firstNonNull(stringParam("adjective"), "")%> mot de passe</label>
</span>
<span class="input fullWidth">
    <input name="password" id="password" type="password" class="text">
</span>
<p:javascript>makeItCount('password', <%= PASSWORD_MAX_LENGTH%>, <%=PASSWORD_MIN_LENGTH%>);</p:javascript>

<span class="label">
    <label for="passwordConfirmation">Confirmation de votre <%=firstNonNull(stringParam("adjective"), "")%> mot de
        passe</label>
    <span class="help fullWidth">pour éviter les erreurs de frappe</span>
</span>
<span class="input fullWidth">
    <input name="passwordConfirmation" id="passwordConfirmation" type="password" class="text">
</span>
