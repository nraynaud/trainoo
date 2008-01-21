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

    <div style="">

        <s:iterator value="messages">
            <div class="message" style="border-top: #4B4C3C solid thin;padding-top:0.1em;margin-top:0.5em">
                <span class="messageHeading"><s:date name="date" format="E dd/M à hh:mm"/>
                        <span class="message_from" style="color:#57470C; font-weight:bold;">
                    <s:property value="sender.name" escape="true"/>
                        </span> a écrit&nbsp;:
                </span>

                <p><s:property escape="true" value="content"/></p>
            </div>
        </s:iterator>
        <s:form action="/messages">
            <fieldset>
                <legend>Nouveau message</legend>
                <label for="receiver">destinataire&nbsp;:</label><br>
                <s:textfield name="receiver" id="receiver" maxlength="20"/>
                <div id="receiver_choices" class="autocomplete">&nbsp;</div>
                <p:javascript>new Ajax.Autocompleter("receiver", "receiver_choices", "/feedback", {paramName: "data", minChars: 1});</p:javascript>
                <s:textarea id="messageContent" name="content" rows="5"/><br>
                <s:submit value="Envoyer"/>
            </fieldset>
        </s:form>
    </div>


</div>