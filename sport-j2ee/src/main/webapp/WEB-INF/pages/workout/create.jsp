<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>


<p:layoutParams title="Création d'un entraînement"/>

<s:url id="createteurl" namespace="/workout" action="create"/>
<s:component template="workoutForm.jsp">
    <s:param name="action" value="createteurl"/>
    <s:param name="submit" value="'Ajouter'"/>
</s:component>

<p:javascript>Field.activate('date');</p:javascript>