<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<p:layoutParams title="Mon Vestiaire"/>

<s:if test="%{globalDistance != null}">
    <h2>Vous avez parcouru <s:property value="globalDistance"/>km</h2>
    <s:component template="distanceByDiscipline.jsp"/>
</s:if>
<div id="globalLeft">
    <h2>Mes dernières sorties</h2>
    <s:component template="workoutTable.jsp">
        <s:param name="displayEdit" value="true"/>
    </s:component>

    <h2>Nouvel entraînement</h2>

    <div>
        <s:component template="workoutForm.jsp">
            <s:param name="action" value="'workouts'"/>
            <s:param name="submit" value="'Ajouter'"/>
        </s:component>
    </div>
</div>

<div id="globalRight" class="messages">
    <h2>Messagerie</h2>

    <s:component template="messages.jsp"/>
</div>