<%@ page import="com.nraynaud.sport.web.ActionDetail" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction" %>
<%@ page import="com.nraynaud.sport.web.view.WorkoutView" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<%
    final WorkoutView workout = top();
%>
<p:layoutParams title='<%="Modification de mon entraînement"%>'
                showTitleInPage="false"/>

<%
    call(pageContext, "workoutForm2.jsp", null,
            "actionUrl", createUrl("/workout", "reallyEdit", "id", workout.id),
            "title", "Modification d'un entraînement",
            "fromAction", new ActionDetail("/workout", "", "id", workout.id),
            "workoutView", workout);
%>

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
                    <%=textArea("externalComment", "externalComment", workout.comment != null ? workout.comment : "")%>
                </span>
            </p>
            <p:javascript>makeItCount('comment', <%=AbstractWorkoutAction.MAX_COMMENT_LENGTH%>);</p:javascript>
        </div>
    </div>
</div>
