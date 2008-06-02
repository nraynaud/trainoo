<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.DisciplineDistance" %>
<%@ page import="java.util.List" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final List<DisciplineDistance> disciplines = (List<DisciplineDistance>) property("distanceByDisciplines",
            List.class);
    if (disciplines.size() > 0) {
        for (final DisciplineDistance dd : disciplines) {
            final String distancePart = dd.distance == null ? "" : "&nbsp;(" + formatDistance(dd.distance) + ")";
            out.append(currenUrlWithParams(dd.discipline + distancePart, true, "disciplineFilter",
                    dd.discipline.nonEscaped()));
            out.append("&nbsp;| ");
        }
        out.append(currenUrlWithParams(
                "toutes disciplines&nbsp;(" + (long) property("globalDistance", Double.class).doubleValue() + "km)",
                true, "disciplineFilter", ""));
        out.append("&nbsp;| ");
    }
%>