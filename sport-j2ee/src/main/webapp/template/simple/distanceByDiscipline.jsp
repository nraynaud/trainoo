<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

|<s:iterator value="distanceByDisciplines" status="row">
    <s:property value="discipline"/>(<s:property value="distance"/>km) |
</s:iterator>