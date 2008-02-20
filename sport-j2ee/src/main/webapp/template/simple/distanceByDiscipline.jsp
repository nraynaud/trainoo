<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:if test="%{distanceByDisciplines.size > 0}">
    |
    <s:iterator value="distanceByDisciplines">
        <% final String discipline = stringProperty("discipline"); %>
        <%=currenUrlWithParams(discipline + "&nbsp;(" + stringProperty("distance") + "km)", true, "discipline",
                discipline)%>&nbsp;|
    </s:iterator>
    <%=currenUrlWithParams("toutes disciplines&nbsp;(" + stringProperty("globalDistance") + "km)", true,
            "discipline",
            "")%>&nbsp;|

</s:if>