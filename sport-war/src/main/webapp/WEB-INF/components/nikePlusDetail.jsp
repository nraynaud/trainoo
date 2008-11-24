<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final Workout workout = top();
    if (workout.getUser().getNikePlusId() != null) {
%>
<a href="http://nikeplus.nike.com/nikeplus/?l=runners,runs,<%=workout.getUser().getNikePlusId()%>,runID,<%=workout.getNikePlusId()%>"
        >Voir la course sur nike.com</a> <%
} else {
%>
Entraînement Nike+
<%
    }
%>
