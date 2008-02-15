<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Group" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.data.GroupData" %>
<%@ page import="com.nraynaud.sport.data.GroupPageData" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<%final GroupPageData groupPage = (GroupPageData) top();%>
<%final Group group = groupPage.group;%>
<p:layoutParams title="<%=group == null ? "Liste des groupes" : "Groupe : " + group.getName()%>"/>

<% if (group != null) {%>
<div id="globalLeft">
    <span class="label">Créé le&nbsp;: </span><span class="userSupplied"><%=new SimpleDateFormat("dd/MM/yyyy").format(
        group.getCreationDate())%></span> <span class="label">par&nbsp;:</span> <span
        class="userSupplied"><%=group.getOwner().getName()%></span>

    <div class="<%=defaultOrUserClass(group.getDescription())%>"><%=escapedOrNullmultilines(group.getDescription(),
            "Aucune description")%>
    </div>
    <%
        if (isLogged()) {
            if (group.getOwner().equals(currentUser()))
                out.append("<div style='text-align: right'>")
                        .append(selectableUrl("/groups", "edit", "Mettre à jour", "id", String.valueOf(group.getId())))
                        .append("</div>");
            call(pageContext, "publicMessageForm.jsp", group);
        }
        if (groupPage.messages.isEmpty()) {%>
    <h2>Aucun message pour l'instant.</h2>
    <%
        }
        call(pageContext, "messageList.jsp", groupPage.messages, "pageVariable", "'messagesStartIndex'");
    %>
</div>
<%}%>
<div id="<%=group == null ? "tinyCenter" : "globalRight"%>">
    <%
        if (group != null) {
    %>
    <h2>Les entraînements du groupe</h2>
    <%
        call(pageContext, "distanceByDiscipline.jsp", groupPage.statistics);
        call(pageContext, "workoutTable.jsp", groupPage.statistics.workouts, "displayEdit", "false", "displayName",
                "true");
        if (!groupPage.users.isEmpty()) {
    %>
    <h2>Les membres</h2>
    <ul>
        <% for (final User user : groupPage.users) {
            out.append("<li>")
                    .append(selectableUrl("/bib", "", user.getName().toString(), "id", String.valueOf(user.getId())))
                    .append("</li>\n");
        }%>
    </ul>
    <%
            } else {
                out.append("<h2>Aucun membre</h2>\n");
            }
        }
    %>
    <h2>Tous les groupes</h2>
    <table>
        <s:iterator value="%{allGroups}">
            <%final GroupData groupData = (GroupData) top();%>
            <tr>
                <td><%=selectableUrl("/groups", "", groupData.name.toString(), "id", String.valueOf(groupData.id))%>
                </td>
                <%final int newCount = groupData.newMessagesCount; %>
                <td><%=newCount > 0 ? newCount + (newCount == 1 ? " nouveau message" : " nouveaux messages") : ""%>
                </td>
                <td><%final long count = ((Long) property("memberCount")).longValue();%>
                    <%=count > 1 ? count + " membres" : count == 1 ? "un membre" : "aucun membre"%>
                </td>

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
    <h2>Créer un groupe</h2>
    <s:form action="create" namespace="/groups">
        <s:hidden name="fromAction" value="%{actionDescription}"/>
        <s:textfield name="name"/>
        <s:submit value="Créer !"/>
    </s:form>
    <%}%>
</div>