<%@ page import="com.nraynaud.sport.web.ActionDetail" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title='Nouvel Entraîment' showTitleInPage="false"/>


<% call(pageContext, "workoutForm2.jsp", null,
        "actionUrl", Helpers.createUrl("/workout", "create"),
        "title", "Création d'un Entraînement",
        "fromAction", new ActionDetail("/workouts", ""),
        "discipline", null,
        "date", "Aujourd'hui",
        "duration", "",
        "distance", "",
        "energy", "");%>

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
                    <%=Helpers.textArea("externalComment", "externalComment", propertyEscapedOrNull("comment", ""))%>
                </span>
            </p>
            <p:javascript>makeItCount('comment', <%=AbstractWorkoutAction.MAX_COMMENT_LENGTH%>);</p:javascript>
        </div>
    </div>
</div>
