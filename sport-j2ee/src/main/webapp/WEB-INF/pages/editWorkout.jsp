<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p:layoutParams title="Modification d'un entraînement"/>

<s:component template="workoutForm.jsp">
    <s:param name="submit" value="'modifier'"/>
</s:component>
<p:javascript>Field.activate('date');</p:javascript>