<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.data.WorkoutPageData" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<% final WorkoutPageData data = (WorkoutPageData) top();
    final Workout workout = data.workout;
    final User runner = workout.getUser();
    final User currentUser = currentUser();
    final boolean isCurrentUser = currentUser != null && currentUser.equals(runner); %>
<p:layoutParams title="<%=isCurrentUser ? "Mon entraînement" : "Entraînement de " + escaped(runner.getName())%>"/>

<div class="bigWorkout">
    <% call(pageContext, "workoutComponent.jsp", workout, "extended", Boolean.TRUE); %>
</div>
<div id="globalLeft">
    <h2><%=!data.messages.isEmpty() ? "Les réactions à cette sortie" : "Aucune réaction pour l'instant."%>
    </h2>
    <%if (!isLogged()) {%>
    <p>En vous <%=signupUrl("inscrivant")%> ou en vous <%=loginUrl("connectant")%> vous pourriez réagir à cette
        sortie.</p>
    <%
        } else
            call(pageContext, "publicMessageForm.jsp", workout);
        call(pageContext, "messageList.jsp", data.messages);
    %>
</div>

<div id="globalRight">
    <h2><%=isCurrentUser ? "Mes" : "Ses"%> derniers entraînements</h2>
    <% call(pageContext, "workoutTable.jsp", data.lastWorkouts, "highLight", workout.getId());
        if (isLogged() && !isCurrentUser) {%>
    <h2>Envoyer un message à <%=escaped(runner.getName())%>
    </h2>
    <% call(pageContext, "privateMessageForm.jsp", privateFormConfig(workout, runner), "hideReceiverBox", true);
        call(pageContext, "messageList.jsp", data.privateMessages);
    }%>
</div>