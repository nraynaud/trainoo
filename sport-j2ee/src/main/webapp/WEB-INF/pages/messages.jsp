<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p:layoutParams title="Messagerie"/>
<div id="tinyCenter">

    <%call(pageContext, "privateMessageForm.jsp", property("conversationData"));%>
    <%call(pageContext, "messageList.jsp", property("conversationData.messages"));%>
</div>