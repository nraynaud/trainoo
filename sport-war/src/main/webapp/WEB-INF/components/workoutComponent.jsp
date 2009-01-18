<%@ page import="static com.nraynaud.sport.formatting.FormatHelper.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.web.view.StackUtil" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%final Workout workout = StackUtil.top();%>
<span class="userName"><%=workout.getUser().getName()%></span>
<span class="date"><%=new SimpleDateFormat("dd/M").format(workout.getDate())%></span>
<span class="discipline"><%=workout.getDiscipline()%></span>
<span class="duration"><%=formatDuration(workout.getDuration(), "")%></span>
<span class="distance"><%=formatDistanceHtml(workout.getDistance(), "")%></span>