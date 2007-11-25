<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:if test="%{workouts.size > 0}">
    <table>
        <tr>
            <th>Date</th>
            <th>Durée</th>
        </tr>
        <s:iterator value="workouts">
            <tr>
                <td><s:date name="date" format="E dd/M/yy"/></td>
                <td><s:property value="duration"/></td>
                <s:if test="%{parameters.displayName}">
                    <td><s:property value="user.name"/></td>
                </s:if>
            </tr>
        </s:iterator>
    </table>
</s:if>