<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.ConversationData" %>
<%@ page import="com.nraynaud.sport.web.view.PrivateMessageFormConfig" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<p:layoutParams title="<%="Messages privés avec " + escaped(stringProperty("receiver"))%>"/>

<div id="tinyCenter">
    <% final ConversationData data = (ConversationData) property("conversationData");
        call(pageContext, "privateMessageForm.jsp", new PrivateMessageFormConfig(stringProperty("receiver"), null),
                "hideReceiverBox", true);
        call(pageContext, "messageList.jsp", data.privateMessages);
    %>
</div>