<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<p:layoutParams title="Mon Vestiaire"/>

<s:if test="%{update}">
    <h2>Vous avez parcouru <s:property value="globalDistance"/>km</h2>
    <%call(pageContext, "distanceByDiscipline.jsp");%>
</s:if>
<div id="globalLeft">
    <h2>Mes dernières sorties</h2>
    <% call(pageContext, "workoutTable.jsp", property("workouts"), "displayEdit", "true");%>

    <h2>Nouvel entraînement</h2>

    <div>
        <s:url id="createteurl" namespace="/workout" action="create" includeParams="none">
            <s:param name="id" value="id"/>
        </s:url>
        <%call(pageContext, "workoutForm.jsp", null, "action", "createteurl", "submit", literal("Ajouter"));%>
    </div>
</div>

<div id="globalRight" class="messages">
    <h2>Messagerie</h2>
    <%
        call(pageContext, "privateMessageForm.jsp");
    %>
    <fieldset>
        <legend>Mes correspondances</legend>
        <ul>
            <s:iterator value="%{privateMessageReceivers}">
                <s:url id="messageUrl" action="" namespace="/messages">
                    <s:param name="receiver" value="%{top}"/>
                </s:url>
                <li><a href="<s:property value="messageUrl"/>"><s:property value="%{top}"/></a>
                </li>
            </s:iterator>
        </ul>
    </fieldset>
</div>