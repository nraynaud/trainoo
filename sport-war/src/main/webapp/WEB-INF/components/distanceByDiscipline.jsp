<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.DisciplineDistance" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:if test="%{distanceByDisciplines.size > 0}">
    |
    <%
        for (final DisciplineDistance dd : (List<DisciplineDistance>) property("distanceByDisciplines",
                List.class)) { %>
    <%=currenUrlWithParams(dd.discipline + "&nbsp;(" + (long) dd.distance.doubleValue() + "km)", true,
            "disciplineFilter", dd.discipline.nonEscaped())%>&nbsp;|
    <%}%>

    <%=currenUrlWithParams(
            "toutes disciplines&nbsp;(" + (long) property("globalDistance", Double.class).doubleValue() + "km)", true,
            "disciplineFilter", "")%>&nbsp;|

</s:if>