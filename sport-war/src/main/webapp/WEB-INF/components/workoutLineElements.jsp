<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.formatting.DateHelper" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.formatting.FormatHelper" %>
<%@ page import="java.util.Collection" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<% final Workout workout = top(Workout.class); %>
<span class="date"><%=DateHelper.printDate("dd/MM", workout.getDate())%></span>
<span class="discipline"><%=workout.getDiscipline()%></span>
<% if (boolParam("withUser")) {%>
        <span class="user">
        <%
            final Collection<User> participants = workout.getParticipants();
            if (participants.size() > 1) {
        %>
        <span title="<%=joinNames(participants)%>" style="cursor:help">collectif</span>
        <%
            } else
                out.append(shortSpan(workout.getUser().getName(), 9));
        %>
        </span>
<%}%>
<span class="duration"><%=FormatHelper.formatDuration(workout.getDuration(), "&nbsp;")%></span>
<span class="distance"><%=FormatHelper.formatDistance(workout.getDistance(), "&nbsp;")%></span>
<%final Long messageCount = workout.getMessageCount();%>
<span class="coms <%=messageCount > 0 ? "" : "comsNone"%>"><%=messageCount%></span>
