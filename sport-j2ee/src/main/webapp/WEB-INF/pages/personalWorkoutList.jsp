<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.UserPageData" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html; charset=UTF-8" %>

<p:layoutParams title="Mon Vestiaire"/>

<%final UserPageData data = (UserPageData) top();%>
<h2>Vous avez parcouru <%=data.getStatisticsData().globalDistance%>km</h2>
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

<div id="globalRight" class="messages">
    <h2>Messagerie</h2>
    <%
        call(pageContext, "privateMessageForm.jsp");
    %>
    <fieldset>
        <legend>Mes correspondances</legend>
        <ul>
            <s:iterator value="%{privateMessageReceivers}">
                <s:url id="messageUrl" action="" namespace="/messages">
                    <s:param name="receiver" value="%{top.correspondentName}"/>
                </s:url>
                <li><a href="<s:property value="messageUrl"/>"><s:property value="%{top.correspondentName}"/>
                    (<s:property value="%{top.messageCount}"/>)</a>
                    <s:if test="%{top.newMessageCount > 0}">
                        <span style="font-size: small;vertical-align:super;color:red;font-weight:normal;"><s:property
                                value="%{top.newMessageCount}"/> <s:if
                                test="%{top.newMessageCount > 1}">nouveaux</s:if><s:else>nouveau</s:else></span>
                    </s:if>
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
            <li><a href="<s:property value="%{groupUrl}"/>"><s:property value="name"/></a></li>
        </s:iterator>
    </ul>
</div>