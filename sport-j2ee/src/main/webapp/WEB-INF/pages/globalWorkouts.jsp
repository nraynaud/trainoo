<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Les derniers entraînements"/>

<h2><s:property value="globalDistance"/>km parcourus par les membres.</h2>
<s:component template="distanceByDiscipline.jsp"/>
<div id="globalLeft">
    <h2>Les dernières sorties</h2>
    <s:component template="workoutTable.jsp">
        <s:param name="displayName" value="true"/>
    </s:component>
</div>
<div id="globalRight">
    <h1>Bienvenue !</h1>

    <p>Vous pratiquez le sport en dehors de la Télé&nbsp;?<br>
        Vous suivrez ici votre vie active.</p>

    <p><a href="<s:url action='signup'/>">Inscrivez-vous</a>, vous pourrez publier dans ce tableau vos entraînements,
        les commenter ainsi que celui des autres et participer à des sorties collectives.</p>
</div>