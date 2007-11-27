<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Les derniers entraînements"/>
<h1>Les derniers Entraînements</h1>
    <span style="display:inline-block;">
        <s:component template="workoutTable.jsp">
            <s:param name="displayName" value="true"/>
        </s:component>
    </span>