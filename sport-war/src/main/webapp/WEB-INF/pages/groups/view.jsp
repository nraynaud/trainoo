<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Group" %>
<%@ page import="com.nraynaud.sport.data.GroupData" %>
<%@ page import="com.nraynaud.sport.data.GroupPageData" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="static com.nraynaud.sport.web.action.groups.CreateAction.*" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<%
    final GroupPageData groupPage = top();
    final Group group = groupPage.group;
%>
<p:layoutParams title='<%=group == null ? "Les groupes" : "Groupe : " + group.getName()%>'/>

<% if (group != null) {%>
<div class="subHeading">
    Créé le <%=new SimpleDateFormat("dd/MM/yyyy").format(group.getCreationDate())%> par <%=bibLink(group.getOwner(),
        10)%>
</div>

<div id="globalLeft">

    <h2>Messages</h2>
    <%
        if (isLogged()) {
            call(pageContext, "publicMessageForm.jsp", group);
        }
        if (groupPage.messages.isEmpty()) {
    %>
    <h2>Aucun message pour l'instant.</h2>
    <%
        } else {
            paginate(pageContext, "messageList.jsp", view(groupPage.messages, "messagesStartIndex"));
        }
    %>
</div>
<% } %>

<div id="<%=group == null ? "tinyCenter" : "globalRight"%>">
<% if (group != null) { %>

<% if (group.getOwner().equals(currentUser()) || group.getDescription() != null) { %>
<h2>
        <span class="buttonList">
        <% if (group.getOwner().equals(currentUser())) { %>
            <a href="<%=createUrl("/groups", "edit", "id", group.getId().toString())%>" title="Modifier"
               class="button editButton">Modifier</a>
        <%}%>
        </span>
    Description
</h2>

<div class="block">
    <div class="content textContent">
        <p><%=escapedOrNullmultilines(group.getDescription(),
                "<em>Aucune description</em>")%>
        </p>

    </div>
</div>
<%}%>

<h2>Membres</h2>

<div class="block userListBlock">
    <div class="content textContent">
        <%call(pageContext, "userListBlock.jsp", groupPage.users);%>
        <% if (groupPage.isMember) { %>
        <form method="POST" action="<%=createUrl("/groups", "part")%>">
            <input type="hidden" name="fromAction" value="<%=currentAction()%>"/>
            <input type="hidden" name="groupId" value="<%=group.getId()%>"/>
                    <span class="actions">
                        <input type="submit" class="submit" value="Quitter le groupe"/>
                    </span>
        </form>
        <%} else {%>
        <form method="POST" action="<%=createUrl("/groups", "join")%>">
            <input type="hidden" name="fromAction" value="<%=currentAction()%>"/>
            <input type="hidden" name="groupId" value="<%=group.getId()%>"/>
                    <span class="actions">
                        <input type="submit" class="submit" value="Rejoindre le groupe"
                               title="Rejoindre le groupe vous permettra de suivre les nouveaux messages depuis votre vestiaire."/>
                    </span>
        </form>
        <%}%>
    </div>
</div>

<h2>Les entraînements du groupe</h2>

<div class="block sheetBlock">
    <div class="header">
        <div class="deco"></div>
        <% call(pageContext, "distanceByDiscipline.jsp", groupPage.workouts); %>
    </div>
    <div class="content">
        <div class="deco"></div>
        <%
            paginate(pageContext, "workoutTable.jsp", view(groupPage.workouts.workouts, "workoutPage"), "displayName",
                    true, "withUser", true);
        %>
    </div>
    <div class="footer">
        <div class="deco"></div>
    </div>
</div>

<%
    if (!groupPage.users.isEmpty()) {
%>

<%
        } else {
            out.append("<h2>Aucun membre</h2>\n");
        }
    }
%>

<% if (group != null) {%>
<h2>Voir les autres groupes</h2>
<%} else {%>
<h2>Tous les groupes</h2>
<%}%>
<div class="block">
    <div class="content textContent">
        <table class="groupList">
            <tbody>
                <%
                    for (final GroupData otherGroup : groupPage.allGroups) {
                %>
                <tr>
                    <th><%=link("/groups", "", otherGroup.name.toString(), null, "id", String.valueOf(otherGroup.id))%>
                    </th>
                    <%final int newCount = otherGroup.newMessagesCount; %>
                    <td><%=pluralize(newCount, "", "un nouveau message", newCount + " nouveaux messages")%>
                    </td>
                    <td><%final long count = otherGroup.memberCount;%>
                        <%=pluralize(count, "aucun membre", "un seul membre", count + " membres")%>
                    </td>
                </tr>
                <%
                    }
                %>

            </tbody>
        </table>
    </div>
</div>

<%if (isLogged()) {%>
<h2>Créer un groupe</h2>

<div class="block">
    <div class="content textContent">
        <form method="POST" action="<%=createUrl("/groups", "create")%>">
            <s:actionerror/>
            <s:fielderror/>
            <input type="hidden" name="fromAction" value="<%=currentAction()%>"/>
                <span class="input">
                    <input class="text" id="name" name="name"/>
                </span>
            <p:javascript>makeItCount('name', <%=MAX_NAME_LENGTH%>, <%=MIN_NAME_LENGTH%>);</p:javascript>
                <span class="actions">
                    <input type="submit" class="submit" value="Créer !"/>
                </span>
        </form>
    </div>
</div>
<%}%>
</div>
