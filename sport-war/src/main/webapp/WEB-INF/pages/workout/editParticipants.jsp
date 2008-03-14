<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Mes partenaires lors de cette sortie"/>

<div class="content bigWorkout">
    <%call(pageContext, "workoutComponent.jsp", property("workout", Workout.class));%>
</div>
<div id="tinyCenter">

    <h2>Je n'étais pas tout seul !</h2>

    <div class="content">
        <s:form namespace="/workout" action="participants">
            <s:actionmessage/>
            <s:actionerror/>
            <s:fielderror/>
            <s:hidden name="id" value="%{id}"/>
            <label for="participants">Les autres participants étaient&nbsp;:</label><br/>
            <s:select id="participants" list="%{allUsers}" name="participants" multiple="true" value="%{participants}"
                      size="10"/>
            <s:submit value="Envoyer la liste !"/>
        </s:form>
    </div>
</div>
