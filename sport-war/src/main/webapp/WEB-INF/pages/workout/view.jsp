<%@ page import="com.google.code.facebookapi.FacebookException" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.google.code.facebookapi.IFacebookRestClient" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page import="static com.nraynaud.sport.Helper.*" %>
<%@ page import="com.nraynaud.sport.data.WorkoutPageData" %>
<%@ page import="static com.nraynaud.sport.formatting.DateHelper.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.action.workout.CreateAction" %>
<%@ page import="com.nraynaud.sport.web.view.DataHelper" %>
<%@ page import="org.w3c.dom.Document" %>
<%@ page import="static com.nraynaud.sport.FacebookUtil.*" %>
<%@ page import="java.util.List" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<% final WorkoutPageData data = top();
    final Workout workout = data.workout;
    final User runner = workout.getUser();
    final User currentUser = currentUser();
    final boolean isCurrentUser = currentUser != null && currentUser.equals(runner);
    final boolean isNikePlus = workout.isNikePlus();
    final boolean hasPartners = workout.getParticipants().size() > 1;
%>
<p:layoutParams title='<%=isCurrentUser ? "Mon entraînement" : "Entraînement de " + runner.getName()%>'
                showTitleInPage="false"/>
<p:javascript-raw>
    Trainoo.isWorkout = true;
    Trainoo.workout = {
    id :<%=workout.getId()%>,
    runner : {
    id : <%=workout.getUser().getId()%>,
    name : '<%=escapedForJavascript(workout.getUser().getName().nonEscaped())%>'
    }
    };

    prepareParticipantList();
</p:javascript-raw>
<%
    final HttpSession session = request.getSession(false);
    if (session != null) {
        final Workout newWorkout = (Workout) session.getAttribute(CreateAction.NEW_WORKOUT);
        try {
            if (newWorkout != null && newWorkout.getUser().getFacebookId() != null) {
                session.removeAttribute(CreateAction.NEW_WORKOUT);
                final IFacebookRestClient<Document> client = getClient(request, response);
                final String formatted = formatWorkout(newWorkout);
                final String activity = formatActivity(newWorkout, getInfo(client, "sex").equals("female"));
%>
<p:javascript src="http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php"/>
<p:javascript-raw>
    FB.init("<%=client.getApiKey()%>", "/static/facebook/xd_receiver.htm");
    var data = {"detail": "<%=formatted%>", "activity": "<%=activity%>", "workoutid": "<%=newWorkout.getId()%>"};
</p:javascript-raw>
<p:javascript>
    FB.ensureInit(function() {
        FB.Connect.showFeedDialog(61529416197, data);
    });
</p:javascript>
<%
            }
        } catch (FacebookException e) {
            //swallow
        }
    }
%>
<h1><%=bibLink(runner, 15)%>&nbsp;: <%=workout.getDiscipline()%>
    <small><%=printDate("EEEE dd MMMM", workout.getDate())%>
    </small>
</h1>

<div class="block workoutBlock" id="workoutBlock">
    <div class="content">
        <form action="<%=createUrl("/workout", "delete")%>" method="POST" name="delWorkoutForm">
            <span class="buttonList">
                <% if (isCurrentUser) { %>
                <a href="<%=createUrl("/workout", "edit", "id", workout.getId().toString())%>"
                   title="Modifier ou effacer cet entrainement" class="button editButton">
                    Modifier</a>
                    <input id="delete" type="submit" class="submit" name="delete" value="Supprimer"/>
                    <a href="#" class="button deleteButton"
                       onclick="document.delWorkoutForm.submit(); return false;"
                            >
                        <label for="delete">Supprimer</label>
                    </a>
                    <input type="hidden" name="id" value="<%=workout.getId()%>"/>
                <% } %>
            </span>
        </form>
        <dl>
            <%
                final List<DataHelper.Data> dataList = DataHelper.compute(workout);
                if (dataList.size() == 0 && isCurrentUser) {
            %>
            <dt class="noDescription informative">Vous n'avez entré ni durée ni distance,
                cliquez sur le petit crayon pour ajouter les informations manquantes.
            </dt>
            <%} else {%>
            <%
                for (final DataHelper.Data row : dataList) {
            %>
            <dt <%=row.userProvided ? "class=\"editable\"" : ""%>><%=row.label%>
            </dt>
            <dd><span><%=row.value%></span></dd>
            <%}%>
            <%}%>

        </dl>
        <% if (isNikePlus && workout.getUser().getNikePlusId() != null) { %>
        <div>
            <%call(pageContext, "nikePlusDetail.jsp", workout);%>
        </div>
        <%}%>
    </div>
</div>

<div id="globalLeft">
    <h2><%=!data.messages.isEmpty() ? "Les réactions à cette sortie" : "Aucune réaction pour l'instant."%>
    </h2>

    <%if (!isLogged()) {%>
    <div class="block">
        <div class="content textContent">
            <p>En vous <%=signupUrl("inscrivant")%> ou en vous <%=loginUrl("connectant")%> vous pourriez réagir à cette
                sortie.</p>
        </div>
    </div>
    <%
        } else
            call(pageContext, "publicMessageForm.jsp", workout);
        paginate(pageContext, "messageList.jsp", view(data.messages, "publicMessagesPageIndex"), "hideWorkoutSubject",
                true);
    %>
</div>

<div id="globalRight">
    <%if (isCurrentUser || workout.getDebriefing() != null) {%>
    <h2>
        <span class="buttonList">
        <% if (isCurrentUser) { %>
            <a href="<%=createUrl("/workout", "edit", "id", workout.getId().toString())%>"
               title="Modifier le compte-rendu" class="button editButton">
                Modifier</a>
        <%}%>
        </span>

        Compte rendu
    </h2>
    <%}%>
    <div class="block">
        <div class="content textContent">
            <p><%=escapedOrNullmultilines(workout.getDebriefing(),
                    "<em>Pas de compte-rendu</em>")%>
            </p>
        </div>
    </div>


    <% if (hasPartners || isCurrentUser) { %>
    <h2 class="participantsEditingArea">
        <span class="buttonList">
            <%if (isCurrentUser) {%>
            <a href="<%=createUrl("/workout", "participants", "id", workout.getId().toString())%>"
               title="Ajouter des participants" class="button editButton"
               id="editParticipantsList">Ajouter des participants</a>
            <%}%>
        </span>
        Participants
    </h2>

    <div class="block userListBlock participantsEditingArea">
        <div class="content" id="participantsList">
            <%call(pageContext, "userListBlock.jsp", workout.getParticipants());%>
        </div>
    </div>

    <% if (!data.similarWorkouts.isEmpty()) { %>
    <div class="block sheetBlock">
        <div class="header">
            <div class="deco"></div>
            <h3>Entraînements similaires</h3>
        </div>
        <div class="content">
            <div class="deco"></div>
            <%
                call(pageContext, "workoutTable.jsp", data.similarWorkouts, "displayName",
                        true, "withUser", true);
            %>
        </div>
        <div class="secondaryHeader">
            <div class="deco"></div>
            <h3>Derniers entraînements</h3>
        </div>
        <div class="content">
            <div class="deco"></div>
            <%
                call(pageContext, "workoutTable.jsp", data.lastWorkouts, "displayName", true, "withUser", true);
            %>
        </div>
        <div class="footer">
            <div class="deco"></div>
        </div>
    </div>
    <% } %>

    <%
        }
        if (isLogged() && !isCurrentUser) {%>
    <%=link("/messages", "", "Envoyer un message privé à " + runner.getName(), "", "receiver",
            runner.getName().nonEscaped())%>
    <%}%>
</div>
