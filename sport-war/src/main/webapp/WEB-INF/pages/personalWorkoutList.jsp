<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.UserString" %>
<%@ page import="com.nraynaud.sport.data.ConversationSummary" %>
<%@ page import="com.nraynaud.sport.data.GroupData" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page import="com.nraynaud.sport.data.UserPageData" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<p:layoutParams title="Mon Vestiaire" showTitleInPage="true"/>

<%final UserPageData data = top();%>
<div id="globalLeft">

    <h2>
        <%
            if (currentUser() != null && currentUser().getNikePluEmail() != null) {
        %>
        <a class="refreshNikePlus" title="Rafraichir les données Nike+"
           href="<%=createUrl("/privatedata", "refreshNikePlus", "fromAction", fromActionOrCurrent())%>">
            Rafraichir les donnés Nike+
        </a>
        <% } %>
        Mes dernières sorties
    </h2>

    <div class="block sheetBlock userSheetBlock">
        <%if (data.getStatisticsData().workouts.isEmpty()) {%>
        Je n'ai encore aucun entraînement.<br>
        <i>Courage, allez transpirer&nbsp;!</i>
        <%} else {%>
        <div class="header">
            <div class="deco"></div>
            <%call(pageContext, "disciplineTabs.jsp", data.getStatisticsData());%>
        </div>
        <div class="content">
            <div class="deco"></div>
            <%
                paginate(pageContext, "workoutTable.jsp", view(data.getStatisticsData().workouts, "workoutPage"));%>
        </div>
        <div class="footer">
            <div class="deco"></div>
        </div>
        <%}%>
    </div>
</div>

<div id="globalRight">
    <%
        final Iterable<GroupData> membership = property("groupMembership");
        if (membership.iterator().hasNext()) {
    %>
    <h2>Les groupes dont je suis membre</h2>

    <div class="block">
        <div class="content textContent">
            <table class="groupList">
                <tbody>
                    <%
                        for (final GroupData groupData : membership) {
                    %>
                    <tr>
                        <th><%=link("/groups", "", groupData.name.toString(), null, "id",
                                String.valueOf(groupData.id))%>
                        </th>
                        <%final int newCount = groupData.newMessagesCount; %>
                        <td><%= pluralize(newCount, "", "un nouveau message", newCount + " nouveaux messages")%>
                        </td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
        </div>
    </div>
    <%}%>

    <h2>Mes correspondances</h2>

    <div class="block userListBlock">
        <div class="content">
            <ul class="userList">
                <s:iterator value="%{privateMessageReceivers}">
                    <%
                        final ConversationSummary summary = top();
                        final long newCount = summary.newMessageCount;
                        final UserString name = summary.correspondentName;%>
                    <li class="<%=newCount > 0 ? "hasNewMessage" : "noNewMessage"%>">
                        <%=link("/messages", "", shortString(name, 10), "voir la discussion avec " + name, "receiver",
                                name.nonEscaped())%>
                        <%
                            if (newCount > 0) {
                        %>
                        <span class="newMessages"><%=newCount%> <%=newCount > 1 ? "nouveaux" : "nouveau"%></span>
                        <%}%>
                    </li>
                </s:iterator>
            </ul>
        </div>
    </div>
    <h2>Envoyer un message</h2>
    <%
        call(pageContext, "privateMessageForm.jsp");
    %>
</div>
