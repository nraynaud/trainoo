<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Modification d'un entraînement"/>

<div id="tinyCenter">
    <s:url id="updateurl" namespace="/workout" action="edit" includeParams="none">
        <s:param name="id" value="id"/>
    </s:url>
    <%
        call(pageContext, "workoutForm.jsp", null, "action", "updateurl", "showDelete", "true", "submit",
                literal("Modifier"));
    %>
</div>

<p:javascript>$('date').focus();</p:javascript>