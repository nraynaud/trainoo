<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.DisciplineDistance" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:if test="%{distanceByDisciplines.size > 0}">
    |
    <%
        for (final DisciplineDistance dd : (List<DisciplineDistance>) property("distanceByDisciplines",
                List.class)) {
            final String distancePart = dd.distance == null ? "" : "&nbsp;(" + dd.distance + "km)";
            out.append(currenUrlWithParams(dd.discipline + distancePart, true, "disciplineFilter",
                    dd.discipline.nonEscaped()));
            out.append("&nbsp;|");
        }
        out.append(currenUrlWithParams(
                "toutes disciplines&nbsp;(" + (long) property("globalDistance", Double.class).doubleValue() + "km)",
                true, "disciplineFilter", ""));
        out.append("&nbsp;|");
    %>

</s:if>