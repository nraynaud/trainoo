<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.data.WorkoutPageData" %>
<%@ page import="com.nraynaud.sport.web.view.DataHelper" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page import="com.nraynaud.sport.web.view.TableContent" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.nraynaud.sport.web.DateHelper.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<% final WorkoutPageData data = top(WorkoutPageData.class);
    final Workout workout = data.workout;
    final User runner = workout.getUser();
    final User currentUser = currentUser();
    final boolean isCurrentUser = currentUser != null && currentUser.equals(runner);
    final boolean isNikePlus = workout.isNikePlus();
    final boolean hasPartners = workout.getParticipants().size() > 1;
%>
<p:layoutParams title="<%=isCurrentUser ? "Mon entraînement" : "Entraînement de " + runner.getName()%>"
                showTitleInPage="false"/>
<h1><%=runner.getName()%>&nbsp;: Sortie <%=workout.getDiscipline()%> - <%=printDate("EEEE dd MMMM", workout.getDate())%>
</h1>

<div class="block workoutBlock" id="workoutBlock">
    <div class="content">
        <span class="buttonList">
            <a href="<%=createUrl("/workout", "edit", "id", workout.getId().toString())%>"
               title="Modifier ou effacer cet entrainement" class="button editButton">
                Modifier</a>
        </span>
        <dl>
            <%

                final List<DataHelper.Data> dataList = DataHelper.compute(workout);
                for (final DataHelper.Data row : dataList) {

            %>
            <dt <%=row.userProvided ? "class=\"editable\"" : ""%>><%=row.label%>
            </dt>
            <dd><span><%=row.value%></span></dd>
            <% }%>
            <% if (isNikePlus) { %>
            <% if (workout.getUser().getNikePlusId() != null) { %>
            <dt>Entrainement Nike+&nbsp;:</dt>
            <dd>
                <a href="http://nikeplus.nike.com/nikeplus/?l=runners,runs,<%=workout.getUser().getNikePlusId()%>,runID,<%=workout.getNikePlusId()%>">
                    Voir la course sur nike.com</a>
            </dd>
            <%} else {%>
            <dt class="noDescription">Entrainement Nike+</dt>
            <%}%>
            <%}%>
            <%if (isCurrentUser) {%>
            <%=selectableLink("/workout", "participants", "Ajouter des participants", "Ajouter des participants", "id",
                    workout.getId().toString())%>
            <%}%>
            <div class="<%=defaultOrUserClass(workout.getComment())%>"><%=escapedOrNullmultilines(workout.getComment(),
                    "Pas de compte rendu")%>
                <%if (isCurrentUser) {%>
                <s:a href="%{editurl}" title="Modifier"><img class="pen" src="<%=stat("/static/pen.png")%>"
                                                             alt=""></s:a>
                <%}%>
            </div>
    </div>
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

    <h2>
        <span class="buttonList">
            <%if (isCurrentUser) {%>
            <a href="<%=createUrl("/workout", "edit", "id", workout.getId().toString())%>" title="Modifier"
               class="button editButton">Modifier</a>
            <%}%>
        </span>
        Compte rendu
    </h2>

    <div class="block">
        <div class="content textContent">
            <p>
                <%=escapedOrNullmultilines(workout.getComment(),
                        "<em>Pas de commentaire</em>")%>
            </p>
        </div>
    </div>

    <% if (hasPartners || isCurrentUser) { %>
    <h2>
        <span class="buttonList">
            <%if (isCurrentUser) {%>
            <a href="<%=createUrl("/workout", "participants", "id", workout.getId().toString())%>"
               title="Ajouter des participants" class="button editButton">Ajouter des participants</a>
            <%}%>
        </span>
        Participants
    </h2>

    <div class="block userListBlock">
        <div class="content">
            <ul class="userList">
                <%
                    for (final User participant : workout.getParticipants()) {
                        out.append("<li>")
                                .append(bibLink(participant))
                                .append("</li>");
                    }
                %>
            </ul>
        </div>
    </div>
    <%
        }
        final ArrayList<TableContent.TableSheet> sheets = new ArrayList<TableContent.TableSheet>(2);
        if (!data.similarWorkouts.isEmpty()) {
            final TableContent.TableSheet similarSheet = new TableContent.TableSheet("Entraînements similaires",
                    data.similarWorkouts, SECONDARY_TABLE_RENDERER);
            sheets.add(similarSheet);
        }
        final TableContent.TableSheet lastSheet = new TableContent.TableSheet("Derniers entraînements",
                data.lastWorkouts, SECONDARY_TABLE_RENDERER);
        sheets.add(lastSheet);
        call(pageContext, "workoutTable.jsp", new TableContent(sheets), "idPrefix", "'lasts_'", "highLight",
                workout.getId());
        if (isLogged() && !isCurrentUser) {%>
    <h2>Envoyer un message à <%=runner.getName()%>
    </h2>

    <%
            call(pageContext, "privateMessageForm.jsp", privateFormConfig(workout, runner), "hideReceiverBox", true);
            paginate(pageContext, "messageList.jsp",
                    view(data.privateMessages, "privateMessagesPageIndex"));
        }
    %>
</div>
