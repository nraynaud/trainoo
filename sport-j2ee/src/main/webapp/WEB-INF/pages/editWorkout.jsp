<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Modification d'un entraînement"/>

<s:component template="workoutForm.jsp">
    <s:url id="updateurl" action="editWorkout" method="update" includeParams="get">
        <s:param name="id" value="id"/>
    </s:url>
    <s:param name="action" value="updateurl"/>
    <s:param name="submit" value="'Modifier'"/>
</s:component>

<p:javascript>Field.activate('date');</p:javascript>