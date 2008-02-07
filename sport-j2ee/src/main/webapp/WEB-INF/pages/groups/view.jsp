<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.GroupData" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<p:layoutParams title="liste des groupes"/>

<div id="tinyCenter">
    <h2>Les groupes</h2>
    <table>
        <s:iterator>
            <%final GroupData groupData = (GroupData) top();%>
            <tr>
                <td><s:property value="name"/></td>
                <td><s:property value="memberCount"/> membre(s)</td>

                <% if (isLogged()) {%>
                <td>
                    <% if (!groupData.isMember) { %>
                    <s:form action="join" namespace="/groups">
                        <s:hidden name="fromAction" value="%{actionDescription}"/>
                        <s:hidden name="groupId" value="%{id}"/>
                        <s:submit value="Rejoindre" cssStyle="with:100%"/>
                    </s:form>
                    <%} else {%>
                    <s:form action="part" namespace="/groups">
                        <s:hidden name="fromAction" value="%{actionDescription}"/>
                        <s:hidden name="groupId" value="%{id}"/>
                        <s:submit value="Quitter" cssStyle="with:100%"/>
                    </s:form>
                    <% }%>
                </td>
                <% } %>
            </tr>
        </s:iterator>
    </table>

    <%if (isLogged()) {%>
    <h2>créer un groupe</h2>
    <s:form action="create" namespace="/groups">
        <s:hidden name="fromAction" value="%{actionDescription}"/>
        <s:textfield name="name"/>
        <s:submit value="Créer !"/>
    </s:form>
    <%}%>
</div>