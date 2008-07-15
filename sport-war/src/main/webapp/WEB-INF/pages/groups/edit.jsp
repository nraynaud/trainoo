<%@ page import="static com.nraynaud.sport.web.action.groups.EditAction.*" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Modifier un groupe"/>

<div id="tinyCenter">
    <div class="block">
        <div class="content textContent">
            <form method="POST" action="<%=createUrl("/groups", "edit")%>">
                <s:actionerror/>
                <s:actionmessage/>
                <s:fielderror/>
                <input type="hidden" name="id" value="<%=stringProperty("id")%>"/>
                
                <span class="label">
                    <label for="name">Nom du groupe&nbsp;:</label>
                </span>
                <span class="input fullWidth">
                    <input class="text" id="name" name="name" value="<%=stringProperty("name")%>"/>
                </span>
                <p:javascript>makeItCount('name', <%=MAX_NAME_LENGTH%>, <%=MIN_NAME_LENGTH%>);</p:javascript>

                <span class="label">
                    <label for="description">Description&nbsp;:</label>
                </span>
                <span class="input fullWidth">
                    <textarea id="description" name="description" /><%=escapedOrNull(stringProperty("description"), "")%></textarea>
                </span>
                <p:javascript>makeItCount('description', <%=MAX_DESCRIPTION_LENGTH%>);</p:javascript>

                <span class="actions">
                <input type="submit" class="submit" value="Valider !"/>
                </span>
            </form>
        </div>
    </div>
</div>
