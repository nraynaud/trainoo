<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<table class="workouts">
    <s:if test="%{workouts.size > 0}">
        <s:iterator value="workouts" status="rowstatus">
            <tr class="<s:if test="#rowstatus.odd == true ">odd</s:if><s:else>even</s:else>">
                <td><s:date name="date" format="E dd/M"/></td>
                <s:if test="%{parameters.displayName}">
                    <td><s:property value="user.name"/></td>
                </s:if>
                <td><s:property value="discipline" escape="true"/></td>
                <td><p:duration name="duration"/></td>
                <td><p:distance name="distance"/></td>
                <s:if test="%{parameters.displayEdit}">
                    <td>
                        <s:url id="editurl" action="editWorkout" includeParams="get">
                            <s:param name="id" value="id"/>
                        </s:url>
                        <s:a href="%{editurl}">modifier</s:a>
                    </td>
                </s:if>
            </tr>
        </s:iterator>

    </s:if>
    <s:else>
        <tr>
            <td>Rien, il va falloir bouger&nbsp;!</td>
        </tr>
    </s:else>
</table>