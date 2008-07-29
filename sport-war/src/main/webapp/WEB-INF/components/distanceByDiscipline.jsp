<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.DisciplineDistance" %>
<%@ page import="java.util.List" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final List<DisciplineDistance> disciplines = listProperty("distanceByDisciplines", DisciplineDistance.class);
    String currentFilter = pageContext.getRequest().getParameter("disciplineFilter");
    if (currentFilter == null) {
        currentFilter = "";
    }
%>
<% if (disciplines.size() == 0) { %>
<ul class="emptyDisciplineList">
    <% } else { %>
    <ul class="disciplineList">
        <li class="label">Voir :</li>
        <li class="<%=currentFilter.equals("")?"current":""%>"><%=linkCurrenUrlWithParams("Tous", "disciplineFilter",
                "")%>
        </li>
        <% for (final DisciplineDistance dd : disciplines) { %>
        <li class="<%=currentFilter.equals(dd.discipline.nonEscaped())?"current":""%>">
            <%=linkCurrenUrlWithParams(dd.discipline.toString(), "disciplineFilter",
                    dd.discipline.nonEscaped())%>
        </li>
        <% } %>
        <% } %>
    </ul>
