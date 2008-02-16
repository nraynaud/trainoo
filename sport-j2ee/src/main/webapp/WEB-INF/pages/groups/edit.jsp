<%@ page import="static com.nraynaud.sport.web.action.groups.EditAction.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<p:layoutParams title="Modifier un groupe"/>

<div id="tinyCenter">
    <s:form namespace="/groups" action="edit">
        <s:actionerror/>
        <s:actionmessage/>
        <s:fielderror/>
        <s:hidden name="id" value="%{id}"/>
        <s:textfield id="name" name="name" cssStyle="width:100%;"/>
        <p:javascript>makeItCount('name', <%=MAX_NAME_LENGTH%>);</p:javascript>
        <div><s:textarea id="description" name="description" rows="5" cssStyle="width:100%;"/></div>
        <p:javascript>makeItCount('description', <%=MAX_DESCRIPTION_LENGTH%>);</p:javascript>
        <s:submit value="Valider"/>
    </s:form>
</div>