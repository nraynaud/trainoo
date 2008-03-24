<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Group" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.data.GroupData" %>
<%@ page import="com.nraynaud.sport.data.GroupPageData" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="static com.nraynaud.sport.web.action.groups.CreateAction.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<%final GroupPageData groupPage = top(GroupPageData.class);%>
<%final Group group = groupPage.group;%>
<p:layoutParams title="<%=group == null ? "Les groupes" : "Groupe : " + group.getName()%>"/>

<% if (group != null) {%>
<div id="globalLeft" class="content">
    <span class="label">Créé le&nbsp;: </span><span class="userInteresting"><%=new SimpleDateFormat(
        "dd/MM/yyyy").format(
        group.getCreationDate())%></span> <span class="label">par&nbsp;:</span> <span
        class="userInteresting"><%=group.getOwner().getName()%></span>

    <div class="<%=defaultOrUserClass(group.getDescription())%>"><%=escapedOrNullmultilines(group.getDescription(),
            "Aucune description")%>
    </div>
    <%
        if (isLogged()) {
            if (!groupPage.isMember) {
    %>
    <s:form action="join" namespace="/groups" cssStyle="text-align: right;display:block;">
        <s:hidden name="fromAction" value="%{actionDescription}"/>
        <s:hidden name="groupId" value="%{id}"/>
        <s:submit value="Rejoindre le groupe" cssStyle="width:150px"
                  title="Rejoindre le groupe vous permettra de suivre les nouveaux messages depuis votre vestiaire."/>
    </s:form>
    <%
    } else {
    %>
    <s:form action="part" namespace="/groups" cssStyle="text-align: right;display:block;">
        <s:hidden name="fromAction" value="%{actionDescription}"/>
        <s:hidden name="groupId" value="%{id}"/>
        <s:submit value="Quitter le groupe" cssStyle="width:150px"/>
    </s:form>
    <%
            }
            if (group.getOwner().equals(currentUser()))
                out.append("<div style='text-align: right'>")
                        .append(selectableLink("/groups", "edit", "Mettre à jour", null, "id",
                                String.valueOf(group.getId())))
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

    <div class="content">
        <%
            call(pageContext, "distanceByDiscipline.jsp", groupPage.statistics);
            call(pageContext, "workoutTable.jsp", groupPage.statistics.workouts, "displayEdit", "false", "displayName",
                    "true");%>
    </div>
    <%
        if (!groupPage.users.isEmpty()) {
    %>
    <h2>Les membres</h2>

    <div class="content">
        <ul>
            <% for (final User user : groupPage.users) {
                out.append("<li>").append(bibLink(user)).append("</li>\n");
            }%>
        </ul>
    </div>
    <%
            } else {
                out.append("<h2>Aucun membre</h2>\n");
            }
        }
    %>
    <h2>Tous les groupes</h2>

    <div class="content">
        <table>
            <s:iterator value="%{allGroups}">
                <%final GroupData groupData = top(GroupData.class);%>
                <tr>
                    <td><%=selectableLink("/groups", "", groupData.name.toString(), null, "id",
                            String.valueOf(groupData.id))%>
                    </td>
                    <%final int newCount = groupData.newMessagesCount; %>
                    <td><%=newCount > 0 ? newCount + (newCount == 1 ? " nouveau message" : " nouveaux messages") : ""%>
                    </td>
                    <td><%final long count = property("memberCount", Long.class).longValue();%>
                        <%=count > 1 ? count + " membres" : count == 1 ? "un membre" : "aucun membre"%>
                    </td>
                </tr>
            </s:iterator>
        </table>
    </div>
    <%if (isLogged()) {%>
    <h2>Créer un groupe</h2>

    <div class="content" style="width:20em">
        <s:form action="create" namespace="/groups">

            <s:actionerror/>
            <s:fielderror/>

            <s:hidden name="fromAction" value="%{actionDescription}"/>
            <s:textfield id="name" name="name" cssStyle="width:99%"/>
            <p:javascript>makeItCount('name', <%=MAX_NAME_LENGTH%>, <%=MIN_NAME_LENGTH%>);</p:javascript>
            <s:submit value="Créer !"/>
        </s:form>
    </div>
    <%}%>
</div>