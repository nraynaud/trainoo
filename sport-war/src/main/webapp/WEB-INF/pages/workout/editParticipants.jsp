<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Modification d'un entraînement"/>

<div id="tinyCenter">
    <%call(pageContext, "workoutComponent.jsp", property("workout", Workout.class));%>
    <h2>Je n'étais pas tout seul !</h2>

    <div class="content">
        <s:form namespace="/workout" action="participants">
            <s:hidden name="id" value="%{id}"/>
            <label for="participants">Les autres participants étaient&nbsp;:</label><br/>
            <s:select id="participants" list="%{allUsers}" name="participants" multiple="true" value="%{participants}"/>
            <s:submit value="Envoyer la liste !"/>
        </s:form>
    </div>
</div>
<p:javascript>$('date').focus();</p:javascript>
