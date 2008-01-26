<%@ page import="static com.nraynaud.sport.web.action.MessagesAction.CONTENT_MAX_LENGTH" %>
<%@ page import="com.nraynaud.sport.Message" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:actionerror/>
<s:actionmessage/>
<s:fielderror/>

<s:form action="messages" namespace="/">
    <fieldset>
        <legend>Écrire</legend>
        <label for="receiver">Destinataire&nbsp;:</label><br>
        <s:textfield name="receiver" id="receiver" maxlength="20"
                     value="%{parameters.receiver != null ? parameters.receiver : receiver}"/>
        <s:hidden name="aboutWorkoutId"/>
        <div id="aboutWorkoutDiv" class="workout">
            <% final Workout workout = (Workout) property("aboutWorkout");
                if (workout != null) {
                    try {
                        push(workout);%>
            à propos de la sortie&nbsp;:<s:component template="tinyWorkout.jsp"/>
            <%
                    } finally {
                        pop();
                    }
                }%>
        </div>
        <div id="receiver_choices" class="autocomplete">&nbsp;</div>
        <p:javascript>
            new Ajax.Autocompleter("receiver", "receiver_choices", "/feedback",
            {paramName: "data", minChars:1, parameters:"type=logins"});
        </p:javascript>
        <s:textarea id="messageContent" name="content" rows="5"/><br>
        <p:javascript>makeItCount('messageContent', <%= CONTENT_MAX_LENGTH%>);</p:javascript>
        <s:submit value="Envoyer"/>
    </fieldset>
</s:form>
<s:iterator value="messages">
    <%
        final Message message = (Message) top();
        final String cssClass = currentUser().equals(message.getReceiver()) ? "received" : "sent";
    %>
    <div class="message <%=cssClass%>">
        <% final Workout workout = message.getWorkout();%>
                <span class="messageHeading">
                    <s:date name="date" format="E dd/M à HH:mm"/>
                    <% final String name = escaped(message.getSender().getName()); %>
                        <span class="message_from">
                            <s:url id="answerUrl" action="messages" namespace="/" includeParams="get">
                                <s:param name="receiver" value="sender.name"/>
                                <s:param name="aboutWorkoutId" value="%{workout != null ? workout.id : ''}"/>
                            </s:url>
                            <s:a href="%{answerUrl}" title="répondre"><%=name%>
                            </s:a>
                        </span> pour <s:property value="receiver.name" escape="true"/>&nbsp;:
                </span>
        <% if (workout != null) {
            try {
                push(workout);%>
        <div class="workout">à propos de la sortie&nbsp;:<s:component template="tinyWorkout.jsp"/></div>
        <%
                } finally {
                    pop();
                }
            }
        %>
        <p class="messageContent"><%= multilineText(message.getContent())%>
        </p>
    </div>
</s:iterator>