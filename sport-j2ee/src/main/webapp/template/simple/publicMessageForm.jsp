<%@ page import="static com.nraynaud.sport.web.action.MessagesAction.CONTENT_MAX_LENGTH" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:actionerror/>
<s:actionmessage/>
<s:fielderror/>

<% final Workout workout = (Workout) top();%>
<s:form action="messages" namespace="/">
    <fieldset>
        <legend>Nouveau message</legend>
        <s:hidden name="publicMessage" value="true"/>
        <s:hidden name="aboutWorkoutId" value="%{id}"/>
        <div id="aboutWorkoutDiv" class="workout">
            à propos de la sortie&nbsp;: <span class="tinyWorkout"><%
            call(pageContext, "workoutComponent.jsp", workout);%></span>
        </div>
        <s:textarea id="messageContent" name="content" rows="5"/>
        <p:javascript>makeItCount('messageContent', <%= CONTENT_MAX_LENGTH%>);</p:javascript>
        <s:submit value="Envoyer"/>
    </fieldset>
</s:form>
