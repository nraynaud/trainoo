<%@ page import="static com.nraynaud.sport.web.action.bib.EditAction.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p:layoutParams title="Mon Dossard"/>

<s:form action="edit" namespace="/bib">
    <s:actionerror/>
    <s:actionmessage/>
    <s:fielderror/>
    <div id="tinyCenter">
        <p>En mettant à jour votre dossard, les autres pourront mieux vous connaître.<br>
            Aucun champ n'est obligatoire.
        </p>

        <p><label for="town">Ma ville&nbsp;: </label><br>
            <s:textfield id="town" name="town" cssStyle="width:100%;"/>
        </p>
        <p:javascript>makeItCount('town', <%=TOWN_MAX_LENGTH%>);</p:javascript>

        <p><label for="description">Moi&nbsp;: </label><br>
            <span style="font-size:smaller; color:gray;">C'est l'occasion de vous présenter.</span><br>
            <s:textarea id="description" name="description" rows="5" cssStyle="width:100%;"/>
        </p>
        <p:javascript>makeItCount('description', <%=DESCRIPTION_MAX_LENGTH%>);</p:javascript>

        <p><label for="webSite">Mon site&nbsp;: </label><br>
            <s:textfield id="webSite" name="webSite" cssStyle="width:100%;"/>
        </p>
        <p:javascript>makeItCount('webSite', <%=WEBSITE_MAX_LENGTH%>);</p:javascript>
        <p><s:submit value="Valider !"/></p>
    </div>
</s:form>