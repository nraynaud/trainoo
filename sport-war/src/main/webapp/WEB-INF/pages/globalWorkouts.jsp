<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.GlobalWorkoutsPageData" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Trainoo.com, pour les sportifs du dimanche." showTitleInPage="false"/>
<h1>Tableau général</h1>

<div id="globalLeft">
    <h2>Dernières sorties sportives</h2>
    <% final GlobalWorkoutsPageData data = top(GlobalWorkoutsPageData.class);
        paginate(pageContext, "workoutTable.jsp",
                view(data.statisticsData.workouts, "workoutPage", DEFAULT_WORKOUT_TRANSFORMER),
                "displayEdit", "false", "displayName", "true");%>
</div>
<div id="globalRight">
    <%if (!isLogged()) {%>
    <h2>Bienvenue&nbsp;!</h2>

    <div class="block importantBlock">
        <div class="content textContent">
            <p>Vous pratiquez le sport en dehors de la télé&nbsp;?<br/>
                Vous voulez suivre votre vie active&nbsp;?<br/>
                <%=signupUrl("Inscrivez vous")%> et vous pourrez&nbsp;:</p>
            <ul>
                <li>Publier vos entrainements</li>
                <li>Commenter vos entrainements et ceux des autres</li>
                <li>Dialoguer avec d'autres sportifs</li>
                <li>Organiser des sorties collectives</li>
            </ul>
            <form method="GET" id="subscribeForm" action="<%=createUrl("/", "signup")%>">
                <span class="actions">
                    <input type="submit" value="S'inscrire" class="submit"/>
                </span>
            </form>
        </div>
    </div>
    <%}%>
    <h2>Les derniers messages</h2>
    <%call(pageContext, "messageList.jsp", data.recentMessages, "showTopicLink", true, "hideToolbar", true);%>
</div>