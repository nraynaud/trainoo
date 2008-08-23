<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.data.WorkoutPageData" %>
<%@ page import="com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page import="static com.nraynaud.sport.web.DateHelper.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<% final WorkoutPageData data = top(WorkoutPageData.class);
    final Workout workout = data.workout;
    final User runner = workout.getUser();
    final User currentUser = currentUser();
    final boolean isCurrentUser = currentUser != null && currentUser.equals(runner);
    final boolean hasPartners = workout.getParticipants().size() > 1;
%>
<p:layoutParams title="<%=isCurrentUser ? "Mon entraînement" : "Entraînement de " + runner.getName()%>"
                showTitleInPage="false"/>
<% if (boolParam("createWorkout")) { %>
<form action="<%=createUrl("/workout", "create")%>" method="POST" name="workoutForm">
<h1>Création d'une sortie</h1>
<% } else { %>
<form action="<%=createUrl("/workout", "edit", "id", workout.getId().toString())%>" method="POST" name="workoutForm">
    <h1>Modification d'une sortie</h1>
    <% } %>

    <div class="block workoutBlock editingWorkoutBlock" id="workoutBlock">
        <div class="content">
            <span class="buttonList">
                <% if (boolParam("createWorkout")) { %>
                <a href="<%=createUrl("/workouts", "")%>"
                   title="Annuler les modifications" class="button cancelButton verboseButton">
                    Annuler</a>
                <% } else { %>
                <a href="<%=createUrl("/workout", "", "id", workout.getId().toString())%>"
                   title="Annuler les modifications" class="button cancelButton verboseButton">
                    Annuler</a>
                <% } %>
                <input id="submitWorkout" type="submit" class="submit" value="Valider">
                <a href="#" class="button applyButton verboseButton"
                   onclick="document.workoutForm.submit(); return false;"
                        ><label for="submitWorkout">Valider</label></a>
            </span>
            <dl>
                <dt>Discipline :</dt>
                <dd class="editable">
                    <select name="discipline" value="<%=escapedOrNull(stringProperty("discipline"), "")%>">
                        <option value="course">Course</option>
                        <option value="vélo">Vélo</option>
                        <option value="VTT">VTT</option>
                        <option value="marche">Marche</option>
                        <option value="natation">Natation</option>
                        <option value="roller">Roller</option>
                    </select>
                </dd>
                <dt>Date :</dt>
                <dd class="editable">
                    <input id="date" name="date" value="<%=escapedOrNull(stringProperty("date"), "")%>"
                           maxlength="15"
                           onfocus="showWorkoutToolTip(event,'ex&nbsp;: 03/10/2006 ou «hier»' , 'date', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('date', this.value)"
                           onmouseover=""
                           onmouseout="">
                </dd>
                <dt>Distance :</dt>
                <dd class="editable">
                    <input id="distance" name="distance" value="<%=escapedOrNull(stringProperty("distance"), "")%>"
                           maxlength="10"
                           onfocus="showWorkoutToolTip(event, 'En kilomètres.', 'distance', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('distance', this.value)">
                </dd>
                <dt>Durée :</dt>
                <dd class="editable">
                    <input id="duration" name="duration" value="<%=escapedOrNull(stringProperty("duration"), "")%>"
                           maxlength="10"
                           onfocus="showWorkoutToolTip(event,'ex&nbsp;: 03h41\'17 ou 40\'22' , 'duration', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('duration', this.value)">
                </dd>
                <dt>Énergie Dépensée :</dt>
                <dd class="editable">
                    <input id="energy" name="energy" value="<%=escapedOrNull(stringProperty("energy"), "")%>"
                           maxlength="10"
                           onfocus="showWorkoutToolTip(event, 'En kilocalories.', 'energy', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('energy', this.value)">
                </dd>
            </dl>
        </div>
    </div>
</form>

<% if (!boolParam("createWorkout")) { %>
<div id="globalLeft">
    <h2><%=!data.messages.isEmpty() ? "Les réactions à cette sortie" : "Aucune réaction pour l'instant."%>
    </h2>
    <%
        paginate(pageContext, "messageList.jsp", view(data.messages, "publicMessagesPageIndex"), "hideWorkoutSubject",
                true);
    %>
</div>
<% } %>

<div id="globalRight">

    <h2>
        Compte rendu
    </h2>

    <div class="block debriefBlock">
        <div class="content textContent">
            <p>
                <span class="input">
                    <textarea id="comment" name="comment"><%=
                    escapedOrNullmultilines(workout.getComment(), "")
                    %>
                    </textarea>
                </span>
            </p>
            <p:javascript>makeItCount('comment', <%=AbstractWorkoutAction.MAX_COMMENT_LENGTH%>);</p:javascript>
        </div>
    </div>
    <% if (!boolParam("createWorkout")) { %>
    <% if (hasPartners || isCurrentUser) { %>
    <h2>
        <span class="buttonList">
            <a href="<%=createUrl("/workout", "participants", "id", workout.getId().toString())%>"
               title="Ajouter des participants" class="button editButton">Ajouter des participants</a>
        </span>
        Participants
    </h2>
    <% } %>

    <div class="block userListBlock">
        <div class="content">
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
                call(pageContext, "workoutTable.jsp", data.similarWorkouts, "displayName", "true", "withUser", "true");
            %>
        </div>
        <div class="secondaryHeader">
            <div class="deco"></div>
            <h3>Derniers entraînements</h3>
        </div>
        <div class="content">
            <div class="deco"></div>
            <%
                call(pageContext, "workoutTable.jsp", data.lastWorkouts, "displayName", "true", "withUser", "true");
            %>
        </div>
        <div class="footer">
            <div class="deco"></div>
        </div>
    </div>
    <% } %>
    <% } %>

</div>
