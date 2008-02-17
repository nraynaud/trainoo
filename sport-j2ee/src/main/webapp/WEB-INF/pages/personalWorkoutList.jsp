<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.ConversationSummary" %>
<%@ page import="com.nraynaud.sport.data.UserPageData" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<p:layoutParams title="Mon Vestiaire"/>

<%final UserPageData data = (UserPageData) top();%>
<h2>J'ai parcouru <%=data.getStatisticsData().globalDistance%>km</h2>
<%call(pageContext, "distanceByDiscipline.jsp", data.getStatisticsData());%>
<div id="globalLeft">
    <h2>Mes dernières sorties</h2>
    <% call(pageContext, "workoutTable.jsp", data.getStatisticsData().workouts, "displayEdit", "true");%>

    <h2>Nouvel entraînement</h2>

    <div>
        <s:url id="createteurl" namespace="/workout" action="create" includeParams="none">
            <s:param name="id" value="id"/>
        </s:url>
        <%call(pageContext, "workoutForm.jsp", null, "action", "createteurl", "submit", literal("Ajouter"));%>
    </div>
</div>

<div id="globalRight">
    <%
        call(pageContext, "privateMessageForm.jsp");
    %>
    <fieldset>
        <legend>Mes correspondances</legend>
        <ul>
            <s:iterator value="%{privateMessageReceivers}">
                <%
                    final ConversationSummary summary = (ConversationSummary) top();
                %>
                <li>
                    <%=selectableUrl("/messages", "", summary.correspondentName.toString(), "receiver",
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
    </fieldset>
    <h2>Les groupes dont je suis membre</h2>
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