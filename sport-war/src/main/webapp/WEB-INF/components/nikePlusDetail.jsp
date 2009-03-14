<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page import="static com.nraynaud.sport.nikeplus.NikeCurveHelper.*" %>
<%@ page import="java.io.PrintWriter" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final Workout workout = top();
    final String userId = workout.getUser().getNikePlusId();
    final String workoutId = workout.getNikePlusId();
%>
<!--[if IE]>
<p:javascript src="<%=Helpers.stat("/static/excanvas.js")%>"/>
<![endif]-->

<p:javascript src="<%=Helpers.stat("/static/flotr-0.2.0-alpha.js")%>"/>
<%
    final String min = numberOrNull(pageContext.getRequest().getParameter("min"));
    final String max = numberOrNull(pageContext.getRequest().getParameter("max"));
    final String parameter = pageContext.getRequest().getParameter("radius");
    final int radius = parameter == null ? 60 : Integer.parseInt(parameter);
    try {
        final String lowPassCurve = getLowPassCurve(userId, workoutId, radius);
        final String nikePlusCurve = getNikePlusCurve(userId, workoutId);
%>
<p:javascript-raw>
    var lowPasscurve = <%=lowPassCurve%>;
    var nikeCurve = <%=nikePlusCurve%>;
    var min = <%=min%>;
    var max = <%=max%>;
</p:javascript-raw>
<p:javascript>
    f = drawCurve([
        {data: nikeCurve, points: {show: true}},
        {data: lowPasscurve, lines: {lineWidth: 2}, color:'rgba(0,0,0,0.2)',
            mouse: {lineColor: 'black', radius:1, sensibility: 2}}], $('container'), min, max);
</p:javascript>

<div id="container" style="max-width:400px;width:90%;height:100px;margin:auto;"></div>
<%
    } catch (Exception e) {
        out.append("<!-- ");
        e.printStackTrace(new PrintWriter(out));
        out.append(" -->");
    }
%>
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