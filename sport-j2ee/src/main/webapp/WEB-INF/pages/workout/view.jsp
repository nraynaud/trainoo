<%@ page import="com.nraynaud.sport.data.WorkoutPageData" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>


<% final WorkoutPageData data = (WorkoutPageData) top();%>
<p:layoutParams title="<%="Entraînement de " + data.workout.getUser().getName()%>"/>

<div id="globalLeft">
    <div class="bigWorkout">
        <% call(pageContext, "workoutComponent.jsp", data.workout); %>
    </div>
    <h2>Les réactions à cette sortie</h2>
    <% call(pageContext, "messageList.jsp", data.messages);
        if (isLogged()) call(pageContext, "publicMessageForm.jsp", data.workout);%>
</div>

<div id="globalRight">
    <h2>Ses dernier entraînements</h2>
    <% call(pageContext, "workoutTable.jsp", data.lastWorkouts, "highLight", data.workout.getId());%>
</div>