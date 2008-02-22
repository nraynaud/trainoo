<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.data.WorkoutPageData" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<% final WorkoutPageData data = top(WorkoutPageData.class);
    final Workout workout = data.workout;
    final User runner = workout.getUser();
    final User currentUser = currentUser();
    final boolean isCurrentUser = currentUser != null && currentUser.equals(runner); %>
<p:layoutParams title="<%=isCurrentUser ? "Mon entraînement" : "Entraînement de " + runner.getName()%>"/>

<div class="content bigWorkout">
    <% call(pageContext, "workoutComponent.jsp", workout, "extended", Boolean.TRUE); %>
    <%if (isCurrentUser) {%>
    <s:url id="editurl" namespace="/workout" action="edit" includeParams="none">
        <s:param name="id" value="id"/>
    </s:url>
    <s:a href="%{editurl}" title="Modifier ou effacer cet entraînement"><img src="/static/pen.png" alt=""></s:a>
    <%}%>
</div>
<div id="globalLeft">
    <h2><%=!data.messages.isEmpty() ? "Les réactions à cette sortie" : "Aucune réaction pour l'instant."%>
    </h2>

    <div class="content">
        <%if (!isLogged()) {%>
        <p>En vous <%=signupUrl("inscrivant")%> ou en vous <%=loginUrl("connectant")%> vous pourriez réagir à cette
            sortie.</p>
        <%
            } else
                call(pageContext, "publicMessageForm.jsp", workout);
            call(pageContext, "messageList.jsp", data.messages, "pageVariable", "'publicMessagesPageIndex'");
        %>
    </div>
</div>

<div id="globalRight">
    <h2><%=isCurrentUser ? "Mes" : "Ses"%> derniers entraînements</h2>

    <div class="content">
        <% call(pageContext, "workoutTable.jsp", data.lastWorkouts, "highLight", workout.getId());%>
    </div>

    <% if (isLogged() && !isCurrentUser) {%>
    <h2>Envoyer un message à <%=runner.getName()%>
    </h2>

    <div class="content">
        <%
            call(pageContext, "privateMessageForm.jsp", privateFormConfig(workout, runner), "hideReceiverBox", true);
            call(pageContext, "messageList.jsp", data.privateMessages, "pageVariable", "'privateMessagesPageIndex'");
        %>
    </div>
    <%}%>
</div>