<%@ page import="com.nraynaud.sport.UserString" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.MessageKind.PRIVATE" %>
<%@ page import="com.nraynaud.sport.web.action.messages.WriteAction" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.view.StackUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%!
    private static final String MY_ERROR_CODE = "createPrivate";
%><%
    final String errorParam = getFirstValue("error");
    if (errorParam == null || errorParam.equals(MY_ERROR_CODE)) {
        allowOverrides();
        final Workout workout = property("aboutWorkout");
        final Workout answerWorkout = workout != null ? workout : StackUtil.<Workout>parameter("aboutWorkout");
%>

<div class="block addCommentBlock">
    <div class="content">
        <% if (answerWorkout != null) {%>
        <p>
            à propos de la sortie&nbsp;:
            <%call(pageContext, "workoutComponent.jsp", answerWorkout);%>
        </p>
        <%}%>
        <form method="POST" action="<%=createUrl("/messages", "write")%>#errorMessage">
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
            <% final UserString receiverProperty = property("receiver");
                if (!boolParam("hideReceiverBox")) {
                    final UserString receiverParameter = parameter("receiver");
                    final String receiver = receiverProperty != null ? receiverProperty.toString() : receiverParameter
                            != null ? receiverParameter.toString() : "";%>
            <span class="label">
                <label for="receiver">À :</label>
            </span>
            <span class="input">
                <input name="receiver" id="receiver" class="text"
                       value="<%=receiver %>">
                <div id="receiver_choices" class="autocomplete">&nbsp;</div>
                <p:javascript>
                    new Ajax.Autocompleter("receiver", "receiver_choices", "/feedback",
                    {paramName: "data", minChars: 1, parameters:"type=logins"});
                </p:javascript>
            </span>
            <% } else {%>
            <input type="hidden" name="receiver" value="<%=receiverProperty%>">
            <%}%>
            <input type="hidden" name="messageKind" value="<%=PRIVATE%>">
            <input type="hidden" name="fromAction" value="<%=currentAction()%>">
            <input type="hidden" name="onErrorAction" value="<%=currentAction().addParam("error", MY_ERROR_CODE)%>">
            <input type="hidden" name="publicMessage" value="false">
            <% final Workout aboutWorkout = property("aboutWorkout");
                if (aboutWorkout != null) { %>
            <input type="hidden" name="aboutWorkoutId" value="<%=aboutWorkout.getId()%>">
            <% } %>

            <span class="input"><%=textArea("privateMessageContent", "content", "")%></span>
            <p:javascript>makeItCount('privateMessageContent', <%= WriteAction.CONTENT_MAX_LENGTH%>);</p:javascript>

            <span class="actions">
                <input type="submit" class="submit" value="Envoyer" accesskey="s">
            </span>
        </form>
    </div>
</div>
<%}%>
