<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.UserString" %>
<%@ page import="com.nraynaud.sport.data.ConversationSummary" %>
<%@ page import="com.nraynaud.sport.data.GroupData" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page import="com.nraynaud.sport.data.UserPageData" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<p:layoutParams title="Mon Vestiaire" showTitleInPage="true"/>

<%final UserPageData data = top(UserPageData.class);%>
<div id="globalLeft">

    <h2>
        <%
            if (currentUser() != null && currentUser().getNikePluEmail() != null) {
        %>
        <a class="refreshNikePlus" title="Rafraichir les données Nike+"
           href="<%=createUrl("/privatedata", "refreshNikePlus", "fromAction", findFromAction())%>">
            Rafraichir les donnés Nike+
        </a>
        <% } %>
        Mes dernières sorties
    </h2>

    <div class="block sheetBlock userSheetBlock">
        <div class="header">
            <div class="deco"></div>
    <%call(pageContext, "distanceByDiscipline.jsp", data.getStatisticsData());%>
        </div>
        <div class="content">
            <div class="deco"></div>
    <%
        paginate(pageContext, "workoutTable.jsp",
                view(data.getStatisticsData().workouts, "workoutPage"),
                "displayEdit", "true");%>
        </div>
        <div class="footer">
            <div class="deco"></div>
        </div>
    </div>

    <h2>Nouvel entraînement</h2>

    <div class="block">
        <div class="content">
            <%
                call(pageContext, "workoutForm.jsp", null, "action",
                        createUrl("/workout", "create"), "submit", "Ajouter");
            %>
        </div>
    </div>
</div>

<div id="globalRight">
    <%if (property("groupMembership", Iterable.class).iterator().hasNext()) {%>
    <h2>Les groupes dont je suis membre</h2>

    <div class="block">
        <div class="content textContent">
            <table class="groupList">
                <tbody>
                    <s:iterator value="%{groupMembership}">
                        <%final GroupData groupData = top(GroupData.class);%>
                        <tr>
                            <th><%=link("/groups", "", groupData.name.toString(), null, "id",
                                    String.valueOf(groupData.id))%>
                            </th>
                            <%final int newCount = groupData.newMessagesCount; %>
                            <td><%=newCount > 0 ? newCount + (newCount
                                    == 1 ? " nouveau message" : " nouveaux messages") : ""%>
                            </td>
                        </tr>
                    </s:iterator>
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
                        final ConversationSummary summary = top(ConversationSummary.class);
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
