<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<p:layoutParams title="Vos derniers entraînements"/>


<h1>Vos Entraînements</h1>
    <span style="display:inline-block;">
        <s:component template="workoutTable.jsp">
            <s:param name="displayEdit" value="true"/>
        </s:component>
    </span>
<hr>

<h2>Nouvel entraînement</h2>

<div style="display:block;">
    <s:component template="workoutForm.jsp">
        <s:param name="submit" value="'Ajouter'"/>
    </s:component>
</div>

