<%@ page import="static com.nraynaud.sport.web.view.Helpers.isLogged" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="java.util.List" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<table class="workouts">
    <% final List<Workout> workouts = (List<Workout>) top();%>
    <% if (workouts.size() != 0) {%>
    <s:iterator value="top" status="rowstatus">
        <%final Workout workout = (Workout) top(); %>
        <tr class="<%=workout.getId().equals(parameter("highLight")) ? "highLight " : ""%><s:if test="#rowstatus.odd == true">odd</s:if><s:else>even</s:else>">
            <s:url id="workoutUrl" action="" namespace="/workout" includeParams="get">
                <s:param name="id" value="id"/>
            </s:url>
            <td><s:a href="%{workoutUrl}" title="détails"><s:date name="date" format="E dd/M"/></s:a></td>
            <s:if test="%{parameters.displayName}">
                <td>
                    <% if (isLogged()) {%>
                    <s:url id="bibUrl" namespace="bib" action="" includeParams="get">
                        <s:param name="id" value="user.id"/>
                    </s:url>
                    <s:a href="%{bibUrl}" title="Voir son dossard"><s:property value="user.name"/></s:a>
                    <% } else {%>
                    <s:property value="user.name"/>
                    <%}%>
                </td>
            </s:if>
            <td><s:a href="%{workoutUrl}" title="détails"><s:property value="discipline"/></s:a></td>
            <td><s:a href="%{workoutUrl}" title="détails"><p:duration name="duration"/></s:a></td>
            <td><s:a href="%{workoutUrl}" title="détails"><p:distance name="distance"/></s:a></td>
            <s:if test="%{parameters.displayEdit}">
                <td class="img">
                    <s:url id="editurl" namespace="workout" action="edit" includeParams="get">
                        <s:param name="id" value="id"/>
                    </s:url>
                    <s:a href="%{editurl}"><img src="/static/pen.png" alt="modifier ou effacer"></s:a>
                </td>
            </s:if>
            <td class="img">
                <s:url id="replyUrl" action="" namespace="/workout" includeParams="get">
                    <s:param name="id" value="id"/>
                </s:url>
                <s:a href="%{replyUrl}" title="Commenter"><img src="/static/bulle.png" alt="commenter"></s:a>
            </td>
        </tr>
    </s:iterator>
    <%} else {%>
    <tr>
        <td>Rien, il va falloir bouger&nbsp;!</td>
    </tr>
    <% } %>
</table>