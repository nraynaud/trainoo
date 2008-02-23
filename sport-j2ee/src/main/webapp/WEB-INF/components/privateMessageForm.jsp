<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.ActionDetail" %>
<%@ page import="static com.nraynaud.sport.MessageKind.PRIVATE" %>
<%@ page import="com.nraynaud.sport.web.action.messages.WriteAction" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%!
    private static final String MY_ERROR_CODE = "createPrivate";
%><%
    final String errorParam = getFirstValue("error");
    if (errorParam == null || errorParam.equals(MY_ERROR_CODE)) {
        allowOverrides();%>
<% final Workout workout = property("aboutWorkout", Workout.class);
    final Workout answerWorkout = workout != null ? workout : parameter("aboutWorkout", Workout.class);%>

<form id="writeMessage" name="writeMessage"
      action="<s:url action="write" namespace="/messages" anchor="errorMessage" includeParams="none"/>" method="post">
    <fieldset>
        <legend>Nouveau message privé</legend>

        <%
            if (MY_ERROR_CODE.equals(errorParam)) {
        %>
        <s:actionerror/>
        <s:fielderror>
            <s:param value="'receiver'"/>
            <s:param value="'content'"/>
        </s:fielderror>
        <a name="errorMessage"> </a>
        <%}%>
        <% if (parameter("hideReceiverBox", String.class) == null || !boolParam("hideReceiverBox")) {%>
        <div id="answerReceiver">
            <div id="privateReceiver" style="display:inline;">
                <label for="receiver">Destinataire&nbsp;:</label><br>
                <s:textfield name="receiver" id="receiver" maxlength="20"
                             value="%{receiver != null ? receiver : parameters.receiver}"/>
                <div id="receiver_choices" class="autocomplete">&nbsp;</div>
                <p:javascript>
                    new Ajax.Autocompleter("receiver", "receiver_choices", "/feedback",
                    {paramName: "data", minChars:1, parameters:"type=logins"});
                </p:javascript>
            </div>
        </div>
        <% } else {%>
        <s:hidden name="receiver"
                  value="%{parameters.receiver != null ? parameters.receiver.nonEscaped() : receiver.nonEscaped()}"/>
        <%}%>
        <input type="hidden" name="messageKind" value="<%=PRIVATE%>"/>
        <s:hidden id="priv_fromAction" name="fromAction" value="%{actionDescription}"/>
        <input type="hidden" id="priv_onErrorAction" name="onErrorAction"
               value="<%=((ActionDetail)property("actionDescription",Object.class)).addParam("error", MY_ERROR_CODE)%>"/>
        <s:hidden name="publicMessage" value="false"/>
        <s:hidden name="aboutWorkoutId" value="%{aboutWorkout.id}"/>
        <div id="priv_aboutWorkoutDiv" class="workout">
            <% if (answerWorkout != null) {%>
            à propos de la sortie&nbsp;: <span class="tinyWorkout"><%
            call(pageContext, "workoutComponent.jsp", answerWorkout);%></span>
            <%}%>
        </div>
        <s:textarea id="priv_messageContent" cssClass="messageContentArea" name="content" cols="40" rows="5"/>
        <p:javascript>makeItCount('priv_messageContent', <%= WriteAction.CONTENT_MAX_LENGTH%>);</p:javascript>
        <s:submit id="priv_submit" value="Envoyer"/>
    </fieldset>
</form>
<%}%>