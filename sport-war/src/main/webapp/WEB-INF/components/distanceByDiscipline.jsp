<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.DisciplineDistance" %>
<%@ page import="java.util.List" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div class="block">
    <div class="content textContent">
        <p>
            <%
                final List<DisciplineDistance> disciplines = (List<DisciplineDistance>) property(
                        "distanceByDisciplines",
                        List.class);
                if (disciplines.size() > 0) {
                    for (final DisciplineDistance dd : disciplines) {
                        final String distancePart = dd.distance == null ? "" : "&nbsp;("
                                + formatDistance(dd.distance, "")
                                + ")";
                        out.append(linkCurrenUrlWithParams(dd.discipline + distancePart, "disciplineFilter",
                                dd.discipline.nonEscaped()));
                        out.append("&nbsp;| ");
                    }
                    out.append(linkCurrenUrlWithParams(
                            "toutes disciplines&nbsp;("
                                    + (long) property("globalDistance", Double.class).doubleValue()
                                    + "km)",
                            "disciplineFilter", ""));
                }
            %>
        </p>
    </div>
</div>
