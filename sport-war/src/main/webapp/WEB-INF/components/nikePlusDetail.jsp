<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.nikeplus.NikeCurveHelper" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final Workout workout = top();
    final String userId = workout.getUser().getNikePlusId();
    final String workoutId = workout.getNikePlusId();
%>

<p:javascript src="<%=Helpers.stat("/static/excanvas.js")%>"/>
<p:javascript src="<%=Helpers.stat("/static/flotr-0.2.0-alpha.js")%>"/>
<p:javascript>
    var curve = <%=NikeCurveHelper.getNikePlusData(userId, workoutId)%>;
    drawCurve(curve, $('container'));
</p:javascript>
<div id="container" style="width:360px;height:100px;"></div>


<a href="http://nikeplus.nike.com/nikeplus/?l=runners,runs,<%=userId%>,runID,<%=workoutId%>"
        >Voir la course sur nike.com</a>