<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page import="com.nraynaud.sport.web.view.NikePlusPoint" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final Workout workout = top();
    final String userId = workout.getUser().getNikePlusId();
    final String workoutId = workout.getNikePlusId();
%>

<p:javascript src="<%=Helpers.stat("/static/flotr-0.2.0-alpha.js")%>"/>
<p:javascript>
    var curve = <%=NikePlusPoint.getNikePlusData(userId, workoutId)%>;
    var f = Flotr.draw($('container'), [ curve ],
    {
    lines: {show: true},
    xaxis: {tickDecimals: 0, tickFormatter: function(num){ return num+ "<small>km</small>"}},
    yaxis: {autoscaleMargin: 0.1, ticks: []},
    mouse:{
    track: true, color: 'purple',
    sensibility: 6,
    trackDecimals: 2,
    trackFormatter: function(obj) {
    var min = -obj.y;
    var minDec = min - Math.floor(min);
    return obj.x +'km, ' + Math.floor(min) + "'" + Math.round((minDec) * 60) + "''/km";
    }
    }
    });
</p:javascript>
<div id="container" style="width:360px;height:100px;"></div>


<a href="http://nikeplus.nike.com/nikeplus/?l=runners,runs,<%=userId%>,runID,<%=workoutId%>"
        >Voir la course sur nike.com</a>