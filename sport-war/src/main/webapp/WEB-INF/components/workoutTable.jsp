<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.data.PaginatedCollection" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>


<div class="pagination">
    <table class="workouts" style="clear:both;">
        <% final PaginatedCollection<Workout> workouts = top(PaginatedCollection.class);%>
        <% if (!workouts.isEmpty()) {
            boolean parity = false;
            for (final Workout workout : workouts) {
                push(workout);
                try {
                    parity = !parity;
        %>
        <tr class="<%=workout.getId().equals(parameter("highLight", Long.class)) ? "highLight " : ""%><%=parity ? "odd":"even"%>">
            <s:url id="workoutUrl" action="" namespace="/workout" includeParams="none">
                <s:param name="id" value="id"/>
            </s:url>
            <td><s:date name="date" format="E dd/M"/></td>
            <s:if test="%{parameters.displayName}">
                <td><%
                    if (workout.getParticipants().size() > 1) {
                        final StringBuilder participants = new StringBuilder();
                        for (final User user : workout.getParticipants())
                            participants.append(", ").append(escaped(user.getName()));
                %>
                    <span title="<%=participants.substring(2)%>" style="cursor:help">collectif</span>
                    <% } else
                        out.append(bibLink(workout.getUser()));
                    %>
                </td>
            </s:if>
            <td><%=workout.getDiscipline()%>
            </td>
            <td><p:duration name="duration"/></td>
            <td><p:distance name="distance"/></td>
            <s:if test="%{parameters.displayEdit}">
                <td class="img">
                    <s:url id="editurl" namespace="/workout" action="edit" includeParams="none">
                        <s:param name="id" value="id"/>
                    </s:url>
                    <s:a href="%{editurl}" title="Modifier ou effacer cet entraînement"><img src="/static/pen.png"
                                                                                             alt=""></s:a>
                </td>
            </s:if>
            <td class="img">
                <div class="messageLink">
                    <s:a cssClass="messageLink" href="%{workoutUrl}"
                         title="Discussions à propos de cet entraînement"><img
                            src="/static/bulle.png" alt="commenter"><s:if test="%{messageNumber > 0}">
                        <span class="messageNumber"><s:property value="messageNumber"/></span>
                    </s:if></s:a>
                </div>
            </td>
        </tr>
        <%
                } finally {
                    pop();
                }
            }
        } else {%>
        <tr>
            <td>Rien, il va falloir bouger&nbsp;!</td>
        </tr>
        <% } %>
    </table>
    <%if (workouts.hasPrevious()) { %>
    <s:url id="previousPageUrl" includeParams="get">
        <s:param name="workoutPage" value="previousIndex"/>
    </s:url>
    <div class="paginationPrevious"><s:a href="%{previousPageUrl}">&lt;&lt;-Précédents</s:a></div>
    <%}%>
    <%if (workouts.hasNext()) { %>
    <s:url id="nextPageUrl" includeParams="get">
        <s:param name="workoutPage" value="nextIndex"/>
    </s:url>
    <div class="paginationNext"><s:a href="%{nextPageUrl}">Suivants->></s:a></div>
    <%}%>
</div>