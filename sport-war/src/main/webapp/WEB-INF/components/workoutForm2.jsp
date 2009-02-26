<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.web.ActionDetail" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%
    final String actionUrl = stringParam("actionUrl");
    final String title = stringParam("title");
    final ActionDetail fromAction = parameter("fromAction");
    final String discipline = stringParam("discipline");
    final String date = stringParam("date");
    final String duration = stringParam("duration");
    final String distance = stringParam("distance");
    final String energy = stringParam("energy");
%>

<p:javascript>
    /* ugly quick fix for the out-of-form comment problem */
    function retrieveComment() {
    document.workoutForm.comment.value = document.getElementById("externalComment").value;
    }
</p:javascript>

<form action="<%=actionUrl%>" method="POST" name="workoutForm"
      onsubmit="retrieveComment(); return true">
    <input type="hidden" name="comment" id="hiddenComment" value="">
    <input type="hidden" name="fromAction" value="<%=fromAction%>">

    <h1><%=title%>
    </h1>

    <div class="block workoutBlock editingWorkoutBlock" id="workoutBlock">
        <div class="content">
            <s:actionerror/>
            <s:fielderror>
                <s:param value="'date'"/>
                <s:param value="'distance'"/>
                <s:param value="'duration'"/>
                <s:param value="'energy'"/>
            </s:fielderror>
            <span class="buttonList">
                <a href="<%=Helpers.createUrl(fromAction)%>" title="Annuler les modifications"
                   class="button cancelButton verboseButton">
                    Annuler</a>
                <input id="submitWorkout" type="submit" class="submit" value="Valider">
                <a href="#" class="button applyButton verboseButton"
                   onclick="retrieveComment(); document.workoutForm.submit(); return false;"
                        ><label for="submitWorkout">Valider</label></a>
            </span>
            <dl>
                <dt><label for="discipline">Discipline :</label></dt>
                <dd class="editable"><%=Helpers.selectComponent("discipline", "discipline", Workout.DISCIPLINES,
                        Workout.DISCIPLINES, discipline)%>
                </dd>
                <dt><label for="date">Date :</label></dt>
                <dd class="editable">
                    <input id="date" class="text" name="date"
                           value="<%=date%>"
                           maxlength="15"
                           onfocus="showWorkoutToolTip(event,'ex&nbsp;: 03/10/2006 ou «hier»' , 'date', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('date', this.value)"
                           onmouseover=""
                           onmouseout="">
                </dd>
                <dt><label for="distance">Distance :</label></dt>
                <dd class="editable">
                    <input id="distance" class="text" name="distance"
                           value="<%=distance%>"
                           maxlength="10"
                           onfocus="showWorkoutToolTip(event, 'En kilomètres.', 'distance', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('distance', this.value)">
                </dd>
                <dt><label for="duration">Durée :</label></dt>
                <dd class="editable">
                    <input id="duration" class="text" name="duration"
                           value="<%=duration%>"
                           maxlength="10"
                           onfocus="showWorkoutToolTip(event,'ex&nbsp;: 03h41\'17 ou 40\'22' , 'duration', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('duration', this.value)">
                </dd>
                <dt><label for="energy">Énergie Dépensée :</label></dt>
                <dd class="editable">
                    <input id="energy" class="text" name="energy"
                           value="<%=energy%>"
                           maxlength="10"
                           onfocus="showWorkoutToolTip(event, 'En kilocalories.', 'energy', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('energy', this.value)">
                </dd>
            </dl>
        </div>
    </div>
</form>