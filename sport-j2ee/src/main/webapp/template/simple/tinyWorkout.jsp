<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<span class="tinyWorkout">
    <span class="userName"><s:property value="user.name"/></span>
    <span class="date"><s:date name="date" format="dd/M"/></span>
    <span class="discipline"><s:property value="discipline"/></span> 
    <span class="duration"><p:duration name="duration"/></span>
    <span class="distance"><p:distance name="distance"/></span>
</span>