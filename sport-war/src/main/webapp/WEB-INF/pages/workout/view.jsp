<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.data.WorkoutPageData" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="static com.nraynaud.sport.web.converter.DistanceConverter.formatDistance" %>
<%@ page import="java.util.Locale" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<% final WorkoutPageData data = top(WorkoutPageData.class);
    final Workout workout = data.workout;
    final User runner = workout.getUser();
    final User currentUser = currentUser();
    final boolean isCurrentUser = currentUser != null && currentUser.equals(runner); %>
<p:layoutParams title="<%=isCurrentUser ? "Mon entraînement" : "Entraînement de " + runner.getName()%>"/>

<div class="content bigWorkout">
    <span class="workout">
        <span class="userName"><%=bibLink(workout.getUser())%></span>
        <span class="date"><%=new SimpleDateFormat("EEEE dd/M/yyyy", Locale.FRANCE).format(workout.getDate())%></span>
        <span class="discipline"><%=escaped(workout.getDiscipline())%></span>
        <span class="duration"><%=formatDuration(workout.getDuration())%></span>
        <span class="distance"><%= Helpers.formatDistance(workout.getDistance())%></span>
    </span>
    <%if (isCurrentUser) {%>
    <s:url id="editurl" namespace="/workout" action="edit" includeParams="none">
        <s:param name="id" value="id"/>
    </s:url>
    <s:a href="%{editurl}" title="Modifier ou effacer cet entraînement"><img class="pen"
                                                                             src="<%=stat("/static/pen.png")%>" alt=""></s:a>
    <%}%>
</div>
<div id="globalLeft">
    <div class="content">
        <%
            final boolean hasAverageSpeed = workout.getDistance() != null && workout.getDuration() != null;
            final boolean isNikePlus = workout.isNikePlus();
            final boolean hasPartners = workout.getParticipants().size() > 1;
        %>
        <div style="border:#824900 thick solid; padding:6px;">
            <%
                if (hasAverageSpeed) {
                    final double distance = workout.getDistance().doubleValue();
                    final long duration = workout.getDuration().longValue();
                    final String averageSpeed = formatDistance(distance / (duration / 60.0 / 60));
                    final double averageTimeByKM = duration / distance;
            %>

            <span class="label">Vitesse moyenne&nbsp;:</span>
            <span class="userInteresting"><%=averageSpeed%>km/h (<%=(int) (averageTimeByKM / 60)%>'<%=(int) (
                    averageTimeByKM % 60)%>''/km)</span>
            <br>
            <%}%>
            <%if (workout.getEnergy() != null) {%>
            <span class="label">Energie dépensée&nbsp;:</span>
            <span class="userInteresting"><%=workout.getEnergy()%>kcal</span>
            <br>
            <%
                if (workout.getDuration() != null) {
            %>
            <span class="label">Puissance moyenne&nbsp;:</span>
            <span class="userInteresting"><%=workout.getEnergy().longValue() * 4187 / workout.getDuration()
                    .longValue()%>W</span>
            <br>
            <%
                    }
                }
            %>
            <%
                if (isNikePlus) {
                    call(pageContext, "nikePlusDetail.jsp", workout);
                    out.append("<br>");
                } %>
            <%
                if (hasPartners) {%>
            Les équipiers étaient&nbsp;:
            <%
                for (final User participant : workout.getParticipants())
                    out.append(' ').append(bibLink(participant));
            %>
            <%}%>
            <%if (isCurrentUser) {%>
            <%=selectableLink("/workout", "participants", "Ajouter des participants", "Ajouter des participants", "id",
                    workout.getId().toString())%>
            <%}%>
            <div class="<%=defaultOrUserClass(workout.getComment())%>"><%=escapedOrNullmultilines(workout.getComment(),
                    "Pas de commentaire")%>
                <%if (isCurrentUser) {%>
                <s:a href="%{editurl}" title="Modifier"><img class="pen" src="<%=stat("/static/pen.png")%>"
                                                             alt=""></s:a>
                <%}%>
            </div>
        </div>
    </div>
    <h2><%=!data.messages.isEmpty() ? "Les réactions à cette sortie" : "Aucune réaction pour l'instant."%>
    </h2>

    <div class="content">
        <%if (!isLogged()) {%>
        <p>En vous <%=signupUrl("inscrivant")%> ou en vous <%=loginUrl("connectant")%> vous pourriez réagir à cette
            sortie.</p>
        <%
            } else
                call(pageContext, "publicMessageForm.jsp", workout);
            paginate(pageContext, "messageList.jsp", view(data.messages, "publicMessagesPageIndex"),
                    "hideWorkoutSubject", true);
        %>
    </div>
</div>

<div id="globalRight">
    <%if (!data.similarWorkouts.isEmpty()) {%>
    <h2><%=isCurrentUser ? "Mes" : "Ses"%> entraînements similaires</h2>

    <div class="content">
        <% paginate(pageContext, "workoutTable.jsp", view(data.similarWorkouts, "similarPage"), "highLight",
                workout.getId());%>
    </div>
    <%}%>
    <h2><%=isCurrentUser ? "Mes" : "Ses"%> derniers entraînements</h2>

    <div class="content">
        <% paginate(pageContext, "workoutTable.jsp", view(data.lastWorkouts, "workoutPage"), "highLight",
                workout.getId());%>
    </div>

    <% if (isLogged() && !isCurrentUser) {%>
    <h2>Envoyer un message à <%=runner.getName()%>
    </h2>

    <div class="content">
        <%
            call(pageContext, "privateMessageForm.jsp", privateFormConfig(workout, runner), "hideReceiverBox", true);
            paginate(pageContext, "messageList.jsp", view(data.privateMessages, "privateMessagesPageIndex"));
        %>
    </div>
    <%}%>
</div>