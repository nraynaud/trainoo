<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<ul>
    <s:iterator value="participants">
        <li><span class="name"><s:property value="name" escape="true"/></span><span class="id"><s:property value="id" /></span></li>
    </s:iterator>
</ul>
