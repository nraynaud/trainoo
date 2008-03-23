<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final Workout workout = Helpers.top(Workout.class);
    if (workout.getUser().getNikePlusId() != null) {
%> <a style="font-size:18px"
      href="http://nikeplus.nike.com/nikeplus/?l=runners,runs,<%=workout.getUser().getNikePlusId()%>,runID,<%=workout.getNikePlusId()%>">Voir
    la course sur nike.com</a> <%
} else {
%>
Entraînement Nike+
<%
    }
%>