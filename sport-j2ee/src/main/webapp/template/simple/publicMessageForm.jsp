<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.action.messages.WriteAction" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<% allowOverrides();%>
<% final Workout workout = (Workout) top();%>
<s:form action="write" namespace="/messages">
    <fieldset>
        <legend>Nouveau message</legend>

        <s:actionerror/>
        <s:fielderror>
            <s:param value="'content'"/>
        </s:fielderror>

        <s:hidden name="fromAction" value="%{actionDescription}"/>
        <s:hidden name="publicMessage" value="true"/>
        <s:hidden name="aboutWorkoutId" value="%{id}"/>
        <div id="aboutWorkoutDiv" class="workout">
            à propos de la sortie&nbsp;: <span class="tinyWorkout"><%
            call(pageContext, "workoutComponent.jsp", workout);%></span>
        </div>
        <s:textarea id="messageContent" cssClass="messageContentArea" name="content" rows="5"/>
        <p:javascript>makeItCount('messageContent', <%= WriteAction.CONTENT_MAX_LENGTH%>);</p:javascript>
        <s:submit value="Envoyer"/>
    </fieldset>
</s:form>