<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.ConversationSummary" %>
<%@ page import="com.nraynaud.sport.data.UserPageData" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<p:layoutParams title="Mon Vestiaire" showTitleInPage="false"/>

<%final UserPageData data = top(UserPageData.class);%>
<div id="globalLeft">
    <h2>Mes dernières sorties</h2>

    <div class="content">
        <p><%call(pageContext, "distanceByDiscipline.jsp", data.getStatisticsData());%></p>
        <% call(pageContext, "workoutTable.jsp", data.getStatisticsData().workouts, "displayEdit", "true");%>
    </div>
    <h2>Nouvel entraînement</h2>

    <div class="content">
        <s:url id="createteurl" namespace="/workout" action="create" includeParams="none">
            <s:param name="id" value="id"/>
        </s:url>
        <%call(pageContext, "workoutForm.jsp", null, "action", "createteurl", "submit", literal("Ajouter"));%>
    </div>
</div>

<div id="globalRight">
    <%if (property("groupMembership", Iterable.class).iterator().hasNext()) {%>
    <h2>Les groupes dont je suis membre</h2>

    <div class="content">
        <ul>
            <s:iterator value="%{groupMembership}">
                <s:url id="groupUrl" namespace="/groups" action="" includeParams="none">
                    <s:param name="id" value="%{id}"/>
                </s:url>
                <li><a href="<s:property value="%{groupUrl}"/>"><%=stringProperty("name")%>
                </a>
                    <s:if test="%{top.newMessagesCount > 0}">
                        <span class="newMessages"><s:property
                                value="%{top.newMessagesCount}"/> <s:if
                                test="%{top.newMessagesCount > 1}">nouveaux</s:if><s:else>nouveau</s:else></span>
                    </s:if></li>
            </s:iterator>
        </ul>
    </div>
    <%}%>
    <h2>Mes correspondances</h2>

    <div class="content">
        <ul>
            <s:iterator value="%{privateMessageReceivers}">
                <%
                    final ConversationSummary summary = top(ConversationSummary.class);
                %>
                <li>
                    <%=selectableLink("/messages", "", summary.correspondentName.toString(), null, "receiver",
                            summary.correspondentName.nonEscaped())%>
                    <%
                        final long newCount = summary.newMessageCount;
                        if (newCount > 0) {
                    %>
                    <span class="newMessages"><%=newCount%> <%=newCount > 1 ? "nouveaux" : "nouveau"%></span>
                    <%}%>
                </li>
            </s:iterator>
        </ul>
    </div>
    <%
        call(pageContext, "privateMessageForm.jsp");
    %>
</div>