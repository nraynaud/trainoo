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

    <div style="display:block;">
        <s:component template="workoutForm.jsp">
            <s:param name="action" value="'workouts'"/>
            <s:param name="submit" value="'Ajouter'"/>
        </s:component>
    </div>
</div>

<div id="globalRight">
    <h2>Messagerie</h2>

    <div style="border:#4B4C3C solid thin;">

        <s:iterator value="messages">
            <div class="message" style="background-color:#EBE1C6; border-bottom: #4B4C3C solid thin;">
                <p>Le <s:date name="date" format="E dd/M"/>
                        <span class="message_from" style="color:#57470C; font-weight:bold;">
                    <s:property value="sender.name" escape="true"/>
                        </span> a écrit&nbsp;:
                </p>

                <p><s:property escape="true" value="content"/></p>

            </div>
        </s:iterator>
        <s:form action="/messages">
            <s:textfield name="content"/>
            <s:submit value="Envoyer"/>
        </s:form>
    </div>


</div>