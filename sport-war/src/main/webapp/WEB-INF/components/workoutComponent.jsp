<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<s:if test="%{parameters.extended}">
    <s:set name="dateFormat" value="'EEEE dd/M/yyyy'"/>
</s:if>
<s:else>
    <s:set name="dateFormat" value="'dd/M'"/>
</s:else>
<span class="userName"><%=boolParam("extended") ? bibLink(property("user", User.class)) : stringProperty(
        "user.name")%></span>
<span class="date"><s:date name="date" format="%{dateFormat}"/></span>
<span class="discipline"><%=stringProperty("discipline")%></span>
<span class="duration"><p:duration name="duration"/></span>
<span class="distance"><p:distance name="distance"/></span>