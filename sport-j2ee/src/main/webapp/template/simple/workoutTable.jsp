<%@ page import="static com.nraynaud.sport.web.view.Helpers.isLogged" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<table class="workouts">
    <s:if test="%{workouts.size > 0}">
        <s:iterator value="workouts" status="rowstatus">
            <tr class="<s:if test="#rowstatus.odd == true ">odd</s:if><s:else>even</s:else>">
                <td><s:date name="date" format="E dd/M"/></td>
                <s:if test="%{parameters.displayName}">
                    <td>
                        <% if (isLogged()) {%>
                        <s:url id="bibUrl" namespace="bib" action="" includeParams="get">
                            <s:param name="id" value="user.id"/>
                        </s:url>
                        <s:a href="%{bibUrl}" title="Voir son dossard"><s:property value="user.name"/></s:a>
                        <% } else {%>
                        <s:property value="user.name"/>
                        <%}%>
                    </td>
                </s:if>
                <td><s:property value="discipline"/></td>
                <td><p:duration name="duration"/></td>
                <td><p:distance name="distance"/></td>
                <s:if test="%{parameters.displayEdit}">
                    <td>
                        <s:url id="editurl" namespace="workout" action="edit" includeParams="get">
                            <s:param name="id" value="id"/>
                        </s:url>
                        <s:a href="%{editurl}">modifier</s:a>
                    </td>
                </s:if>
                <% if (isLogged()) {%>
                <td>
                    <s:url id="answerUrl" action="messages" namespace="/" includeParams="get">
                        <s:param name="receiver" value="user.name"/>
                        <s:param name="aboutWorkoutId" value="id"/>
                    </s:url>
                    <s:a href="%{answerUrl}" title="Commenter"><img src="/static/bulle.png" alt="commenter"></s:a>
                </td>
                <%}%>
            </tr>
        </s:iterator>

    </s:if>
    <s:else>
        <tr>
            <td>Rien, il va falloir bouger&nbsp;!</td>
        </tr>
    </s:else>
</table>