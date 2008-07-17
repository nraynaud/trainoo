<%@ page import="com.nraynaud.sport.UserString" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.MessageKind.PRIVATE" %>
<%@ page import="com.nraynaud.sport.web.ActionDetail" %>
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

<div class="block addCommentBlock">
    <div class="content">
        <% if (answerWorkout != null) {%>
        <p>
        à propos de la sortie&nbsp;:
        <%call(pageContext, "workoutComponent.jsp", answerWorkout);%>
        </p>
        <%}%>
        <form method="POST" action="<%=createUrl("/messages", "write")%>#errorMessage" >
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
            <% if (!boolParam("hideReceiverBox")) {%>
            <span class="label">
                <label for="receiver">À :</label>
            </span>
            <span class="input">
                <input name="receiver" id="receiver" class="text"
                    value="<%=property("receiver", UserString.class) != null
                        ? property("receiver", UserString.class)
                        : ( parameter("receiver", UserString.class) != null
                            ? parameter("receiver", UserString.class)
                            : "" ) %>"/>
                <div id="receiver_choices" class="autocomplete">&nbsp;</div>
                <p:javascript>
                    new Ajax.Autocompleter("receiver", "receiver_choices", "/feedback",
                    {paramName: "data", minChars: 1, parameters:"type=logins"});
                </p:javascript>
            </span>
            <% } else {%>
            <input type="hidden" name="receiver" value="<%=property("receiver", UserString.class)%>"/>
            <%}%>
            <input type="hidden" name="messageKind" value="<%=PRIVATE%>"/>
            <input type="hidden" name="fromAction" value="<%=stringProperty("actionDescription")%>"/>
            <input type="hidden" name="onErrorAction"
               value="<%=((ActionDetail)property("actionDescription",Object.class)).addParam("error", MY_ERROR_CODE)%>"/>
            <input type="hidden" name="publicMessage" value="false"/>
            <% if (property("aboutWorkout", Workout.class) != null) { %>
            <input type="hidden" name="aboutWorkoutId" value="<%=property("aboutWorkout", Workout.class).getId()%>"/>
            <% } %>
            
            <span class="input">
                <textarea rows="3" name="content" id="messageContent"></textarea>
            </span>
            <p:javascript>makeItCount('messageContent', <%= WriteAction.CONTENT_MAX_LENGTH%>);</p:javascript>

            <span class="actions">
                <input type="submit" class="submit" value="Envoyer"/>
            </span>
        </form>
    </div>
</div>
<%}%>
