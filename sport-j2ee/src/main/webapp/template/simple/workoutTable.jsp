<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<s:if test="%{workouts.size > 0}">
    <table>
        <tr>
            <th>Date</th>
            <th>Durée</th>
            <th>Distance</th>
        </tr>
        <s:iterator value="workouts">
            <tr>
                <td><s:date name="date" format="E dd/M/yy"/></td>
                <td><p:duration name="duration"/></td>
                <td><p:distance name="distance"/></td>
                <s:if test="%{parameters.displayName}">
                    <td><s:property value="user.name"/></td>
                </s:if>
                <td>
                    <s:url action="workouts" method="edit" includeParams="get">
                        <s:param name="id" value="id"/>
                    </s:url>
                </td>
            </tr>
        </s:iterator>
    </table>
</s:if>