<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.Helper.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.formatting.DateIO" %>
<%@ page import="com.nraynaud.sport.formatting.DistanceIO" %>
<%@ page import="com.nraynaud.sport.formatting.EnergyIO" %>
<%@ page import="com.nraynaud.sport.formatting.FormatHelper" %>
<%@ page import="com.nraynaud.sport.web.ActionDetail" %>
<%@ page import="com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<%
    final Workout workout = top();
%>
<p:layoutParams title='<%="Modification de mon entraînement"%>'
                showTitleInPage="false"/>
<p:javascript>
    Trainoo.isWorkout = true;
    Trainoo.workout = {
    id :<%=workout.getId()%>,
    runner : {
    id : <%=workout.getUser().getId()%>,
    name : '<%=escapedForJavascript(workout.getUser().getName().nonEscaped())%>'
    }
    };
</p:javascript>

<% call(pageContext, "workoutForm2.jsp", null,
        "actionUrl", createUrl("/workout", "edit", "id", workout.getId().toString()),
        "title", "Modification d'un entraînement",
        "fromAction", new ActionDetail("/workout", "", "id", workout.getId().toString()),
        "discipline", workout.getDiscipline().nonEscaped(),
        "date", DateIO.DATE_FORMATTER.print(workout.getDate().getTime()),
        "duration", FormatHelper.formatDuration(workout.getDuration(), ""),
        "distance", DistanceIO.formatDistance(workout.getDistance()),
        "energy", EnergyIO.formatEnergy(workout.getEnergy()));%>

<div id="globalLeft">
    &nbsp;
</div>

<div id="globalRight">

    <h2>
        Compte rendu
    </h2>

    <div class="block debriefBlock">
        <div class="content textContent">
            <p>
                <span class="input">
                    <%=textArea("externalComment", "externalComment", workout.getComment().toString())%>
                </span>
            </p>
            <p:javascript>makeItCount('comment', <%=AbstractWorkoutAction.MAX_COMMENT_LENGTH%>);</p:javascript>
        </div>
    </div>
</div>
