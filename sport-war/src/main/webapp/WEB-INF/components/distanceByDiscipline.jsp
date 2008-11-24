<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.DisciplineData" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="static java.lang.Math.min" %>
<%@ page import="java.util.TreeSet" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%!
    private static final int MAX_DISCIPLINES_TABS = 4;
%>
<%
    final List<DisciplineData<DisciplineData.Count>> disciplines = property("distanceByDisciplines");
    String currentFilter = pageContext.getRequest().getParameter("disciplineFilter");
    if (currentFilter == null)
        currentFilter = "";
%>
<% if (disciplines.size() == 0) { %>
<ul class="emptyDisciplineList"></ul>
<% } else { %>
<ul class="disciplineList">
    <li class="label">Voir :</li>
    <li class="<%=currentFilter.equals("") ? "current" : ""%>"><%=linkCurrenUrlWithoutParam("Tous",
            "disciplineFilter")%>
    </li>
    <% final TreeSet<DisciplineData<DisciplineData.Count>> ddSet = new TreeSet<DisciplineData<DisciplineData.Count>>(
            DISCIPLNE_DISTANCE_COMPARATOR);
        ddSet.addAll(disciplines);
        final ArrayList<DisciplineData<DisciplineData.Count>> orderedDD = new ArrayList<DisciplineData<DisciplineData.Count>>(
                ddSet);
        for (final DisciplineData<DisciplineData.Count> dd : orderedDD.subList(0,
                min(orderedDD.size(), MAX_DISCIPLINES_TABS))) { %>
    <li class="<%=currentFilter.equals(dd.discipline.nonEscaped())?"current":""%>">
        <%=linkCurrenUrlWithParams(dd.discipline.toString(), "disciplineFilter", dd.discipline.nonEscaped())%>
    </li>
    <%
        }
        if (orderedDD.size() > MAX_DISCIPLINES_TABS) {
            final String otherFilter = StringUtils.join(orderedDD.subList(MAX_DISCIPLINES_TABS, orderedDD.size()),
                    ',');%>
    <li class="<%=currentFilter.equals(otherFilter)?"current":""%>">
        <%=linkCurrenUrlWithParams("autres", "disciplineFilter", otherFilter)%>
    </li>
    <%
        }
    %>
</ul>
<% } %>

