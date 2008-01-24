<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p:layoutParams title="Mon Dossard"/>

<s:form action="edit" namespace="/bib">
    <s:actionerror/>
    <s:actionmessage/>
    <s:fielderror/>
    <div id="tinyCenter" style="width:40%;margin-left:auto; margin-right:auto">
        <p><label for="town">Ma ville&nbsp;: </label><br>
            <s:textfield id="town" name="town" maxlength="25" cssStyle="width:100%;display:block;"/></p>

        <p><label for="description">Moi&nbsp;: </label><br>
            <span style="font-size:smaller; color:gray;">C'est l'occasion de vous présenter.</span><br>
            <s:textarea id="description" name="description" rows="5" cssStyle="width:100%;display:block;"/>
        </p>

        <p><label for="webSite">Mon site&nbsp;: </label><br>
            <s:textfield id="webSite" name="webSite" maxlength="250" cssStyle="width:100%;display:block;"/></p>

        <p><s:submit value="Valider !"/></p>
    </div>
</s:form>