<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.data.ConversationSummary" %>
<%@ page import="com.nraynaud.sport.data.GroupData" %>
<%@ page import="static com.nraynaud.sport.web.view.PaginationView.view" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.PaginatedCollection" %>
<%@ page import="com.nraynaud.sport.data.UserPageData" %>
<%@ page import="com.nraynaud.sport.web.view.TableContent" %>
<%@ page import="java.util.Collections" %>
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

    <p><%call(pageContext, "distanceByDiscipline.jsp", data.getStatisticsData());%></p>
    <% final PaginatedCollection.Transformer<Workout, TableContent> contentTransformer = new PaginatedCollection.Transformer<Workout, TableContent>() {
        public TableContent transform(final PaginatedCollection<Workout> collection) {
            final TableContent.TableSheet sheet = new TableContent.TableSheet("", collection,
                    SECONDARY_TABLE_RENDERER);
            return new TableContent(Collections.singleton(sheet));
        }
    };
        paginate(pageContext, "workoutTable.jsp",
                view(data.getStatisticsData().workouts, "workoutPage", contentTransformer),
                "displayEdit", "true");%>

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
                            <th><%=selectableLink("/groups", "", groupData.name.toString(), null, "id",
                                    String.valueOf(groupData.id))%>
                            </th>
                            <%final int newCount = groupData.newMessagesCount; %>
                            <td><%=newCount > 0 ? newCount + (newCount
                                    == 1 ? " nouveau message" : " nouveaux messages") : ""%>
                            </td>
                            <td><%final long count = property("memberCount", Long.class).longValue();%>
                                <%=count > 1 ? count + " membres" : count == 1 ? "un seul membre" : "aucun membre"%>
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
                    %>
                    <li class="<%=newCount > 0 ? "hasNewMessage" : "noNewMessage"%>">
                        <%=selectableLink("/messages", "", summary.correspondentName.toString(), null, "receiver",
                                summary.correspondentName.nonEscaped())%>
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
