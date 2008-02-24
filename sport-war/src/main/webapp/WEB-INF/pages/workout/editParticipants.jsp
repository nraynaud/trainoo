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

    <h2>Je n'étais pas tout seul !</h2>

    <div class="content">
        <s:form namespace="/workout" action="participants">
            <label for="participants">Les autres participants étaient&nbsp;:</label><br/>
            <s:select id="participants" list="%{allUsers}" name="participants" multiple="true"/>
            <s:submit value="Envoyer la liste !"/>
        </s:form>
    </div>
</div>
<p:javascript>$('date').focus();</p:javascript>
