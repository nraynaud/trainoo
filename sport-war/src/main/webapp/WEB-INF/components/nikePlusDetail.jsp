<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page import="static com.nraynaud.sport.nikeplus.NikeCurveHelper.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final Workout workout = top();
    final String userId = workout.getUser().getNikePlusId();
    final String workoutId = workout.getNikePlusId();
%>

<p:javascript src="<%=Helpers.stat("/static/excanvas.js")%>"/>
<p:javascript src="<%=Helpers.stat("/static/flotr-0.2.0-alpha.js")%>"/>
<%
    final String lowPass = pageContext.getRequest().getParameter("lowPass");
    final String min = numberOrNull(pageContext.getRequest().getParameter("min"));
    final String max = numberOrNull(pageContext.getRequest().getParameter("max"));
    final String parameter = pageContext.getRequest().getParameter("radius");
    final int radius = parameter == null ? 60 : Integer.parseInt(parameter);
%>
<p:javascript>
    var lowPasscurve = <%=getLowPassCurve(userId, workoutId, radius)%>;
    var nikeCurve = <%=getNikePlusCurve(userId, workoutId)%>;
    f = drawCurve([
    nikeCurve,
    {data: lowPasscurve, lines: {lineWidth: 2}, color:'rgba(0,0,0,0.2)', mouse: {lineColor: 'black', radius:1}}], $('container'),<%=min%>, <%=max%>);

    var ymin = f.series[0].yaxis.min;
    var ymax = f.series[0].yaxis.max;
</p:javascript>

<div id="container" style="width:90%;height:100px;margin:auto;"></div>
<div style="text-align:center">
    <a href="http://nikeplus.nike.com/nikeplus/?l=runners,runs,<%=userId%>,runID,<%=workoutId%>">Voir la course sur
        nike.com</a>
</div>

<%!
    //only avoids injection in JS
    private static String numberOrNull(final String String) {
        return String == null ? "null" : Double.valueOf(String).toString();
    }
%>