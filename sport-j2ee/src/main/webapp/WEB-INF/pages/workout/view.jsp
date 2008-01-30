<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.data.WorkoutPageData" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>


<% final WorkoutPageData data = (WorkoutPageData) top();%>
<% final Workout workout = data.workout;
    final User runner = workout.getUser(); %>
<p:layoutParams
        title="<%=currentUser() != null && currentUser().equals(runner) ? "Mon entraînement" : "Entraînement de " + escaped(runner.getName())%>"/>

<div class="bigWorkout">
    <% call(pageContext, "workoutComponent.jsp", workout); %>
</div>
<div id="globalLeft">

    <h2><%if (!data.messages.isEmpty()) {%>Les réactions à cette sortie<%} else {%>Aucune réaction pour
        l'instant.<%}%></h2>
    <%if (!isLogged()) {%>
    <p>En vous <a href="<s:url action="signup" namespace="/" includeParams="none"/>">inscrivant</a> ou en vous <a
            href="<s:url action="login" namespace="/" includeParams="none"/>">connectant</a> vous pourriez
        régir à cette sortie.</p>
    <%}%>
    <% call(pageContext, "messageList.jsp", data.messages);
        if (isLogged()) call(pageContext, "publicMessageForm.jsp", workout);%>
</div>

<div id="globalRight">
    <h2><%=currentUser() != null && currentUser().equals(runner) ? "Mes" : "Ses"%> dernier entraînements</h2>
    <% call(pageContext, "workoutTable.jsp", data.lastWorkouts, "highLight", workout.getId());%>
    <% if (isLogged() && !runner.equals(currentUser())) {%>
    <h2>Envoyer un message à <%=escaped(runner.getName())%>
    </h2>
    <%
            call(pageContext, "privateMessageForm.jsp", privateFormConfig(workout, runner),
                    "hideReceiverBox", true);
            call(pageContext, "messageList.jsp", data.privateMessages);
        }
    %>
</div>