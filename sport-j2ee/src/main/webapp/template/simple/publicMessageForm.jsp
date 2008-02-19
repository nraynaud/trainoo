<%@ page import="com.nraynaud.sport.Topic" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.ActionDetail" %>
<%@ page import="static com.nraynaud.sport.MessageKind.PUBLIC" %>
<%@ page import="com.nraynaud.sport.web.action.messages.WriteAction" %>
<%@ page import="com.opensymphony.xwork2.ActionContext" %>
<%@ page import="java.util.Arrays" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<% allowOverrides();%>
<s:form id="writeMessage" name="writeMessage" action="writePublic" namespace="/messages">
    <fieldset>
        <legend>Nouveau message</legend>
        <%
            if (Arrays.equals((Object[]) ActionContext.getContext().getParameters().get("error"),
                    new String[]{"createPublic"})) {
        %>
        <s:actionerror/>
        <s:fielderror>
            <s:param value="'content'"/>
        </s:fielderror>
        <a name="errorMessage"> </a>
        <%}%>
        <s:hidden name="fromAction" value="%{actionDescription}"/>
        <input type="hidden" id="pub_onErrorAction" name="onErrorAction"
               value="<%=((ActionDetail)property("actionDescription")).addParam("error", "createPublic")%>"/>
        <s:hidden name="publicMessage" value="true"/>
        <s:hidden name="aboutId" value="%{id}"/>
        <s:hidden name="topicKind" value="%{kind}"/>
        <input type="hidden" name="messageKind" value="<%=PUBLIC%>"/>
        <%if (property("kind") == Topic.Kind.WORKOUT) {%>
        <div id="aboutWorkoutDiv" class="workout">
            à propos de la sortie&nbsp;: <span class="tinyWorkout"><%
            call(pageContext, "workoutComponent.jsp", top());%></span>
        </div>
        <%}%>
        <s:textarea id="messageContent" cssClass="messageContentArea" name="content" rows="5"/>
        <p:javascript>makeItCount('messageContent', <%= WriteAction.CONTENT_MAX_LENGTH%>);</p:javascript>
        <s:submit value="Envoyer"/>
    </fieldset>
</s:form>
