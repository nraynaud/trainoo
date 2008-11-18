<%@ page import="com.nraynaud.sport.web.ActionDetail" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.action.messages.WriteAction" %>
<%@ page import="static com.nraynaud.sport.MessageKind.PUBLIC" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%!
    private static final String MY_ERROR_CODE = "createPublic";
%>
<%
    final String errorParam = getFirstValue("error");
    if (errorParam == null || errorParam.equals(MY_ERROR_CODE)) {
        allowOverrides();%>

<div class="block addCommentBlock">
    <div class="content">
        <form id="writeMessage" name="writeMessage" action="<%=createUrl("/messages", "writePublic")%>" method="POST">
            <%
                if (MY_ERROR_CODE.equals(errorParam)) {
            %>
            <s:actionerror/>
            <s:fielderror>
                <s:param value="'content'"/>
            </s:fielderror>
            <a name="errorMessage"> </a>
            <%}%>
            <input type="hidden" name="fromAction" value="<%=stringProperty("actionDescription")%>"/>
            <input type="hidden" name="onErrorAction"
                   value="<%=property("actionDescription",ActionDetail.class).addParam("error", MY_ERROR_CODE)%>"/>
            <input type="hidden" name="publicMessage" value="true"/>
            <input type="hidden" name="aboutId" value="<%=stringProperty("id")%>"/>
            <input type="hidden" name="topicKind" value="<%=stringProperty("kind")%>"/>
            <input type="hidden" name="messageKind" value="<%=PUBLIC%>"/>

            <span class="input"><%=textArea("messageContent", "content", "")%></span>
            <p:javascript>makeItCount('messageContent', <%= WriteAction.CONTENT_MAX_LENGTH%>);</p:javascript>

            <span class="actions">
                <input type="submit" class="submit" value="Ajouter un message" accesskey="s"/>
            </span>
        </form>
    </div>
</div>
<%}%>
