<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="java.util.Collection" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<% final Workout workout = top(Workout.class); %>
<span class="discipline"><%=workout.getDiscipline()%></span>
<% if (boolParam("withUser")) {%>
        <span class="user">
        <%
            final Collection<User> participans = workout.getParticipants();
            if (participans.size() > 1) {
        %>
        <span title="<%=joinNames(participans)%>" style="cursor:help">collectif</span>
        <%
            } else
                out.append(shortSpan(workout.getUser().getName()));
        %>
        </span>
<%}%>
<span class="duration"><%=formatDuration(workout.getDuration(), "&nbsp;")%></span>
<span class="distance"><%=formatDistance(workout.getDistance(), "&nbsp;")%></span>
<%final Long messageCount = workout.getMessageCount();%>
<span class="coms <%=messageCount > 0 ? "" : "comsNone"%>"><%=messageCount%></span>