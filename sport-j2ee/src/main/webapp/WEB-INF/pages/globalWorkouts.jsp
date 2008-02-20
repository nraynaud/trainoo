<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.GlobalWorkoutsPageData" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Les derniers entraînements"/>
<div class="content">
    <%
        final GlobalWorkoutsPageData data = (GlobalWorkoutsPageData) top();
        call(pageContext, "distanceByDiscipline.jsp", data.statisticsData);
    %>
</div>
<div id="globalLeft">
    <h2>Les dernières sorties</h2>

    <div class="content">
        <% call(pageContext, "workoutTable.jsp", data.statisticsData.workouts, "displayEdit", "false", "displayName",
                "true");%>
    </div>
</div>
<div id="globalRight">
    <%if (!isLogged()) {%>
    <h1>Bienvenue !</h1>

    <div class="content">
        <p>Vous pratiquez le sport en dehors de la Télé&nbsp;?<br>
            Vous suivrez ici votre vie active.</p>

        <p><%=signupUrl("Inscrivez-vous")%>, vous pourrez publier dans ce tableau vos entraînements,
            les commenter ainsi que ceux des autres. Vous pourrez dialoguer avec d'autres sportifs comme vous et
            organiser
            des sorties collectives.</p>

    </div>
    <%}%>
    <h2>Les derniers messages</h2>

    <div class="content">
        <%call(pageContext, "messageList.jsp", data.recentMessages, "showTopicLink", "true");%>
    </div>
</div>