<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:actionerror/>
<s:actionmessage/>
<s:fielderror/>
<s:form action="/messages">
    <fieldset>
        <legend>Écrire</legend>
        <s:token/>
        <label for="receiver">Destinataire&nbsp;:</label><br>
        <s:textfield name="receiver" id="receiver" maxlength="20"/>

        <div id="receiver_choices" class="autocomplete">&nbsp;</div>
        <p:javascript>
            new Ajax.Autocompleter("receiver", "receiver_choices", "/feedback",
            {paramName: "data", minChars:1, parameters:"type=logins"});
        </p:javascript>
        <s:textarea id="messageContent" name="content" rows="5"/><br>
        <s:submit value="Envoyer"/>
    </fieldset>
</s:form>
<s:iterator value="messages">
    <div class="message" style="">
                <span class="messageHeading"><s:date name="date" format="E dd/M à HH:mm"/>
                        <span class="message_from">
                    <s:property value="sender.name" escape="true"/>
                        </span> a écrit&nbsp;:
                </span>

        <p class="messageContent"><s:property value="content" escape="true"/></p>
    </div>
</s:iterator>