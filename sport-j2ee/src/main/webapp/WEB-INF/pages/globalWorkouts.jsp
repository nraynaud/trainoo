<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.GlobalWorkoutsPageData" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Les derniers entraînements"/>
<%final GlobalWorkoutsPageData data = (GlobalWorkoutsPageData) top();%>
<%final String discipline = stringProperty("discipline");%>
<h2>
    <%=data.statisticsData.globalDistance%>km <%if (discipline == null) {%>parcourus par les membres.<%} else {%>
    de <%=discipline%><%}%></h2>
<% call(pageContext, "distanceByDiscipline.jsp", data.statisticsData);%>
<div id="globalLeft">
    <h2>Les dernières sorties</h2>
    <% call(pageContext, "workoutTable.jsp", data.statisticsData.workouts, "displayEdit", "false", "displayName",
            "true");%>
</div>
<div id="globalRight">
    <%if (!isLogged()) {%>
    <h1>Bienvenue !</h1>

    <p>Vous pratiquez le sport en dehors de la Télé&nbsp;?<br>
        Vous suivrez ici votre vie active.</p>

    <p><%=signupUrl("Inscrivez-vous")%>, vous pourrez publier dans ce tableau vos entraînements,
        les commenter ainsi que ceux des autres. Vous pourrez dialoguer avec d'autres sportifs comme vous et organiser
        des sorties collectives.</p>
    <%}%>
    <h2>Les derniers messages</h2>
    <%call(pageContext, "messageList.jsp", data.recentMessages, "showTopicLink", "true");%>
</div>