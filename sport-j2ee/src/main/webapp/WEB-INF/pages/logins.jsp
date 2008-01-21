<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<ul>
    <s:iterator value="logins">
        <li><s:property value="top"/></li>
    </s:iterator>
</ul>