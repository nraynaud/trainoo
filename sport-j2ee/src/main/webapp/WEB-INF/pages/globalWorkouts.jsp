<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Les derniers entraînements"/>

<h2><s:property value="globalDistance"/>km parcourus par les membres.</h2>
<s:component template="distanceByDiscipline.jsp"/>
<div style="">
    <div style="width:49%; float:left;">
        <h2>Les dernières sorties</h2>
        <s:component template="workoutTable.jsp">
            <s:param name="displayName" value="true"/>
        </s:component>
    </div>
    <div style="width:49%; float:right;">
        <h1>Bienvenue !</h1>

        <p style="width:75%">Vous pratiquez le sport en dehors de la Télé&nbsp;? Vous
            suivrez ici votre vie sportive.</p>
    </div>
</div>