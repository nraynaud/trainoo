<%@ page import="static com.nraynaud.sport.web.action.MessagesAction.CONTENT_MAX_LENGTH" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:actionerror/>
<s:actionmessage/>
<s:fielderror/>

<% final Workout answerWorkout = (Workout) property("aboutWorkout");%>
<s:form action="messages" namespace="/">
    <fieldset>
        <legend>Nouveau message</legend>
        <div id="answerReceiver">
            <div id="privateReceiver" style="display:inline;">
                <label for="receiver">Destinataire&nbsp;:</label><br>
                <s:textfield name="receiver" id="receiver" maxlength="20"
                             value="%{parameters.receiver != null ? parameters.receiver : receiver}"/>
                <div id="receiver_choices" class="autocomplete">&nbsp;</div>
                <p:javascript>
                    new Ajax.Autocompleter("receiver", "receiver_choices", "/feedback",
                    {paramName: "data", minChars:1, parameters:"type=logins"});
                </p:javascript>
            </div>
        </div>

        <s:hidden name="publicMessage" value="false"/>
        <s:hidden name="aboutWorkoutId"/>
        <div id="aboutWorkoutDiv" class="workout">
            <% if (answerWorkout != null) {%>
            à propos de la sortie&nbsp;: <span class="tinyWorkout"><%
            call(pageContext, "workoutComponent.jsp", answerWorkout);%></span>
            <%}%>
        </div>
        <s:textarea id="messageContent" name="content" rows="5"/>
        <p:javascript>makeItCount('messageContent', <%= CONTENT_MAX_LENGTH%>);</p:javascript>
        <s:submit value="Envoyer"/>
    </fieldset>
</s:form>
