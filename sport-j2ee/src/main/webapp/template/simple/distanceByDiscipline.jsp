<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:if test="%{distanceByDisciplines.size > 0}">
    <span class="smaller">|
    <s:iterator value="distanceByDisciplines">
        <a href="<%=currentUrlAndParams("discipline", stringProperty("discipline"))%>"><s:property value="discipline"/>&nbsp;(<s:property
                value="distance"/>km)</a>&nbsp;|
    </s:iterator></span>
</s:if>