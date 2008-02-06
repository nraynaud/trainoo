<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:if test="%{distanceByDisciplines.size > 0}">
    <span class="smaller">|
    <s:iterator value="distanceByDisciplines">
        <s:url id="disciplineUrl" includeParams="get">
            <s:param name="discipline" value="discipline"/>
        </s:url>
        <a href="<s:property value="%{disciplineUrl}"/>"><s:property value="discipline"/>&nbsp;(<s:property
                value="distance"/>km)</a>&nbsp;|
    </s:iterator></span>
</s:if>