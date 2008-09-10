<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.data.PaginatedCollection" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<ul class="sheet">
    <% boolean isOdd = true; %>
    <% for (final Object workoutObj : top(PaginatedCollection.class)) {
        final Workout workout = (Workout) workoutObj;
        final String urlPrefix = stringParam("urlPrefix");
    %>
    <li class="<%=isOdd ? "odd":"even"%>">
        <a href="<%=(urlPrefix == null ? "" : urlPrefix) + createUrl("/workout", "", "id", String.valueOf(workout.getId()))%>"
           title="Voir le détail de cet entrainement">
            <%call(pageContext, "workoutLineElements.jsp", workout, "withUser", boolParam("withUser"));%>
        </a>
    </li>
    <%
            isOdd = !isOdd;
        } %>
</ul>
