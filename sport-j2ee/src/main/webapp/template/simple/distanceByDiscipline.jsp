<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:if test="%{distanceByDisciplines.size > 0}">
    <span class="smaller">|
    <s:iterator value="distanceByDisciplines">
        <% final String discipline = stringProperty("discipline"); %>
        <%=currentUrlAndParams(discipline + "&nbsp;(" + stringProperty("distance") + "km)", "discipline",
                discipline)%>&nbsp;|
    </s:iterator>
        <%=currentUrlAndParams("toutes disciplines&nbsp;(" + stringProperty("globalDistance") + "km)", "discipline",
                "")%>&nbsp;|
    </span>
</s:if>