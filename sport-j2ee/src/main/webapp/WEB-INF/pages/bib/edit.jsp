<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p:layoutParams title="Mon Dossard"/>

<s:form action="edit" namespace="/bib">
    <s:actionerror/>
    <s:actionmessage/>
    <s:fielderror/>
    <p><label for="town">Ville&nbsp;: </label><s:textfield id="town" name="town" size="15" maxlength="25"/></p>

    <p><label for="description">Moi&nbsp;: </label><s:textarea id="description" name="description" cols="15" rows="5"/>
    </p>

    <p><label for="webSite">Mon site&nbsp;: </label><s:textfield id="webSite" name="webSite" size="15"/></p>

    <p><s:submit value="Valider !"/></p>

</s:form>