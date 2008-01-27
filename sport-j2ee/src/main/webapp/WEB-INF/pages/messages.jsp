<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p:layoutParams title="Messagerie"/>
<div id="tinyCenter">
    <% push(property("conversationData"));
        try {%>
    <s:component template="messagesComponent.jsp"/>
    <%
        } finally {
            pop();
        }
    %>
</div>