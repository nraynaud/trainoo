<%@ page import="com.nraynaud.sport.data.ConversationData" %>
<%@ page import="com.nraynaud.sport.web.view.PaginationView" %>
<%@ page import="com.nraynaud.sport.web.view.PrivateMessageFormConfig" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<%final ConversationData data = property("conversationData");%>
<p:layoutParams title='<%="Messages privés avec " + data.receiver%>'/>

<div id="tinyCenter">
    <h2>Nouveau message privé</h2>
    <%
        call(pageContext, "privateMessageForm.jsp", new PrivateMessageFormConfig(data.receiver),
                "hideReceiverBox", true);
        paginate(pageContext, "messageList.jsp", PaginationView.view(data.privateMessages, "pageIndex"));
    %>
</div>
