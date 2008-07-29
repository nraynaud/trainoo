<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.DisciplineDistance" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.Comparator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.TreeSet" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%!
    private static final int MAX_DISCIPLINES_TABS = 4;
%>
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
        <li class="<%=currentFilter.equals("") ? "current" : ""%>"><%=linkCurrenUrlWithParams("Tous",
                "disciplineFilter",
                "")%>
        </li>
        <% final TreeSet<DisciplineDistance> ddSet = new TreeSet<DisciplineDistance>(
                new Comparator<DisciplineDistance>() {
                    public int compare(final DisciplineDistance o1, final DisciplineDistance o2) {
                        final long diff = o2.count.longValue() - o1.count.longValue();
                        if (diff != 0)
                            return (int) diff < 0 ? -1 : 1;
                        return o1.discipline.nonEscaped().compareTo(o2.discipline.nonEscaped());
                    }
                });
            ddSet.addAll(disciplines);
            final ArrayList<DisciplineDistance> orderedDD = new ArrayList<DisciplineDistance>(
                    ddSet);
            System.out.println(orderedDD);
            for (final DisciplineDistance dd : orderedDD.subList(0,
                    Math.min(orderedDD.size(), MAX_DISCIPLINES_TABS))) { %>
        <li class="<%=currentFilter.equals(dd.discipline.nonEscaped())?"current":""%>">
            <%=linkCurrenUrlWithParams(dd.discipline.toString(), "disciplineFilter",
                    dd.discipline.nonEscaped())%>
        </li>
        <% }
        } %>
    </ul>
