<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.ConversationData" %>
<%@ page import="com.nraynaud.sport.web.view.PrivateMessageFormConfig" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<%final ConversationData data = (ConversationData) property("conversationData");%>
<p:layoutParams title="<%="Messages privés avec " + data.receiver%>"/>

<div id="tinyCenter">
    <%
        call(pageContext, "privateMessageForm.jsp", new PrivateMessageFormConfig(data.receiver, null),
                "hideReceiverBox", true);
        call(pageContext, "messageList.jsp", data.privateMessages, "pageVariable", "'pageIndex'");
    %>
</div>