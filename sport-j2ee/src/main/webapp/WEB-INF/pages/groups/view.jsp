<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Group" %>
<%@ page import="com.nraynaud.sport.data.GroupData" %>
<%@ page import="com.nraynaud.sport.data.GroupPageData" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<%final GroupPageData groupPage = (GroupPageData) top();%>
<%final Group group = groupPage.group;%>
<p:layoutParams title="<%=group == null ? "Liste des groupes" : "Groupe : " + group.getName()%>"/>

<% if (group != null) {%>
<div id="globalLeft">
    <table class="displayFormLayoutTable">
        <tr>
            <td><span class="label">Créé le&nbsp;: </span></td>
            <td><span class="userSupplied"><%=new SimpleDateFormat("dd/MM/yyyy").format(
                    group.getCreationDate())%></span>
            </td>
        </tr>
        <tr>
            <td><span class="label">par&nbsp;:</span></td>
            <td><span class="userSupplied"><%=escaped(group.getOwner().getName())%></span>
            </td>
        </tr>
    </table>
    <%
        if (isLogged()) {
            call(pageContext, "publicMessageForm.jsp", group);
            call(pageContext, "messageList.jsp", groupPage.messages);
        }%>
</div>
<%}%>
<div id="<%=group == null ? "tinyCenter" : "globalRight"%>">
    <h2>Les<%=group != null ? " autres" : ""%> groupes</h2>
    <table>
        <s:iterator value="%{others}">
            <%final GroupData groupData = (GroupData) top();%>
            <tr>
                <s:url id="groupURL" action="" namespace="/groups" includeParams="get">
                    <s:param name="id" value="%{id}"/>
                </s:url>
                <td><a href="<s:property value="%{groupURL}" />"><s:property value="name"/></a></td>
                <td><s:property value="memberCount"/> membre(s)</td>

                <% if (isLogged()) {%>
                <td>
                    <% if (!groupData.isMember) { %>
                    <s:form action="join" namespace="/groups">
                        <s:hidden name="fromAction" value="%{actionDescription}"/>
                        <s:hidden name="groupId" value="%{id}"/>
                        <s:submit value="Rejoindre" cssStyle="wdith:100%"/>
                    </s:form>
                    <%} else {%>
                    <s:form action="part" namespace="/groups">
                        <s:hidden name="fromAction" value="%{actionDescription}"/>
                        <s:hidden name="groupId" value="%{id}"/>
                        <s:submit value="Quitter" cssStyle="width:100%"/>
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