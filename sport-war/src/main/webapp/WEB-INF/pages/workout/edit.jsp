<%@ page import="com.nraynaud.sport.Track" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page import="com.nraynaud.sport.web.view.WorkoutEditPageDetails" %>
<%@ page import="com.nraynaud.sport.web.view.WorkoutView" %>
<%@ page import="java.util.ArrayList" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<% final WorkoutEditPageDetails pageDetail = top();%>
<p:layoutParams title='<%=pageDetail.pageTile%>' showTitleInPage="false"/>


<p:javascript>
    /* ugly quick fix for the out-of-form debriefing problem */
    function retrieveComment() {
        document.workoutForm.debriefing.value = document.getElementById("externalComment").value;
    }
</p:javascript>
<%
    System.out.println("edit.jsp ");
    System.out.flush();
%>
<form name="workoutForm" action="<%=Helpers.createUrl(pageDetail.doAction)%>" method="POST"
      onsubmit="retrieveComment(); return true;">
    <input type="hidden" name="debriefing" id="hiddenComment" value="">

    <h1><%=pageDetail.pageTile%>
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
            <% final WorkoutView workoutView = pageDetail.workoutView; %>
            <%
                System.out.println("after fetch " + workoutView);
                System.out.flush();
            %>
            <span class="buttonList">
                <a href="<%=Helpers.createUrl(pageDetail.cancelAction)%>" title="Annuler les modifications"
                   class="button cancelButton verboseButton">
                    Annuler</a>
                <input id="submitWorkout" type="submit" class="submit" value="Valider">
                <a href="#" class="button applyButton verboseButton"
                   onclick="retrieveComment(); document.workoutForm.submit(); return false;"
                        ><label for="submitWorkout">Valider</label></a>
            </span>
            <%
                System.out.println("after buttons " + workoutView);
                System.out.flush();
            %>
            <dl>
                <dt><label for="discipline">Discipline :</label></dt>
                <dd class="editable"><%=Helpers.selectComponent("discipline", "discipline", Workout.DISCIPLINES,
                        Workout.DISCIPLINES, workoutView.discipline)%>
                </dd>
                <dt><label for="date">Date :</label></dt>
                <dd class="editable">
                    <input id="date" class="text" name="date"
                           value="<%=workoutView.date%>"
                           maxlength="15"
                           onfocus="showWorkoutToolTip(event,'ex&nbsp;: 03/10/2006 ou «hier»' , 'date', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('date', this.value);"
                           onmouseover=""
                           onmouseout="">
                </dd>
                <%
                    System.out.println("after date " + workoutView);
                    System.out.flush();
                %>
                <dt><label for="distance">Distance :</label></dt>
                <dd class="editable">
                    <input id="distance" class="text" name="distance"
                           value="<%=workoutView.distance%>"
                           maxlength="10"
                           onfocus="showWorkoutToolTip(event, 'En kilomètres.', 'distance', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('distance', this.value);">
                </dd>
                <%
                    System.out.println("after Distance " + workoutView);
                    System.out.flush();
                %>
                <dt><label for="duration">Durée :</label></dt>
                <dd class="editable">
                    <input id="duration" class="text" name="duration"
                           value="<%=workoutView.duration%>"
                           maxlength="10"
                           onfocus="showWorkoutToolTip(event,'ex&nbsp;: 03h41\'17 ou 40\'22' , 'duration', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('duration', this.value);">
                </dd>
                <%
                    System.out.println("after durée " + workoutView);
                    System.out.flush();
                %>
                <dt><label for="energy">Énergie Dépensée :</label></dt>
                <dd class="editable">
                    <input id="energy" class="text" name="energy"
                           value="<%=workoutView.energy%>"
                           maxlength="10"
                           onfocus="showWorkoutToolTip(event, 'En kilocalories.', 'energy', this.value);"
                           onblur="hideToolTip();"
                           onkeyup="feedback('energy', this.value);">
                </dd>
                <%
                    System.out.println("after energy " + workoutView);
                    System.out.flush();
                %>
                <dt><label for="energy">Parcours :</label></dt>
                <%
                    final ArrayList<String> idList = new ArrayList<String>();
                    final ArrayList<String> labels = new ArrayList<String>();
                    idList.add("");
                    labels.add("<b>Aucun</b>");
                    System.out.println("before tracks loop" + pageDetail.userTracks);
                    System.out.flush();
                    for (final Track track : pageDetail.userTracks) {
                        System.out.println("in tracks id: " + track.getId());
                        System.out.flush();
                        idList.add(track.getId().toString());
                        System.out.println("in tracks title: " + track.getTitle());
                        System.out.flush();
                        labels.add(String.valueOf(track.getTitle()));
                    }
                %>
                <%
                    System.out.println("after tracks " + workoutView);
                    System.out.flush();
                %>
                <dd class="editable"><%=Helpers.selectComponent("track", "track", idList,
                        labels, workoutView.trackId)%>
                </dd>
            </dl>
        </div>
    </div>
</form>

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
                    <%
                        System.out.println("lol " + workoutView);
                        System.out.flush();
                    %>
                    <%=textArea("externalComment", "externalComment", workoutView.debriefing)%>
                </span>
            </p>
            <p:javascript-raw>makeItCount('externalComment', <%=AbstractWorkoutAction.MAX_DEBRIEFING_LENGTH%>);</p:javascript-raw>
        </div>
    </div>
</div>
