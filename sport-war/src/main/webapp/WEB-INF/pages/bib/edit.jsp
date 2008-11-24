<%@ page import="static com.nraynaud.sport.web.action.bib.EditAction.*" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p:layoutParams title="Mon dossard"/>

<div id="tinyCenter">
    <div class="block">
        <div class="content textContent">
            <form method="POST" action="<%=createUrl("/bib", "edit")%>">
                <s:actionerror/>
                <s:fielderror/>
                <p>En mettant à jour votre dossard, les autres pourront mieux vous connaître.<br/>
                    Aucun champ n'est obligatoire.
                </p>

                <span class="label">
                    <label for="town">Ma ville&nbsp;:</label>
                </span>
                <span class="input fullWidth">
                    <input class="text" id="town" name="town" value="<%=escapedOrNull(stringProperty("town"), "")%>"/>
                </span>

                <p:javascript>makeItCount('town', <%=TOWN_MAX_LENGTH%>);</p:javascript>

                <span class="label">
                    <label for="description">Moi&nbsp;:</label>
                    <span class="help">C'est l'occasion de vous présenter.</span>
                </span>
                <span class="input fullWidth">
                    <%=textArea("description", "description", escapedOrNull(stringProperty("description"), ""))%>
                </span>
                <p:javascript>makeItCount('description', <%=DESCRIPTION_MAX_LENGTH%>);</p:javascript>

                <span class="label">
                    <label for="webSite">Mon site&nbsp;:</label>
                </span>
                <span class="input fullWidth">
                    <input class="text" id="webSite" name="webSite"
                           value="<%=escapedOrNull(stringProperty("webSite"), "")%>"/>
                </span>
                <p:javascript>makeItCount('webSite', <%=WEBSITE_MAX_LENGTH%>);</p:javascript>

                <span class="actions">
                    <input type="submit" class="submit" value="Valider !"/>
                </span>
            </form>
        </div>
    </div>
</div>
