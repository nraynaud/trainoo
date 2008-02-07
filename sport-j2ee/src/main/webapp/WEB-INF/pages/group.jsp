<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p:layoutParams title="liste des groupes"/>

<div id="tinyCenter">
    <h2>Les groupes</h2>
    <s:iterator>
        <s:property value="name"/>|
        <s:property value="user.name"/>|
        <s:property value="members.size"/>

        <%if (isLogged()) {%>
        <s:form action="join" namespace="/groups">
            <s:hidden name="fromAction" value="%{actionDescription}"/>
            <s:hidden name="groupId" value="%{id}"/>
            <s:submit value="Rejoindre"/>
        </s:form>
        <%}%>
        <br>
    </s:iterator>

    <%if (isLogged()) {%>
    <h2>créer un groupe</h2>
    <s:form action="create" namespace="/groups">
        <s:hidden name="fromAction" value="%{actionDescription}"/>
        <s:textfield name="name"/>
        <s:submit value="Créer !"/>
    </s:form>
    <%}%>
</div>