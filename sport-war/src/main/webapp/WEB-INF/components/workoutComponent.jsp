<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<span class="userName"><%=stringProperty("user.name")%></span>
<span class="date"><s:date name="date" format="dd/M"/></span>
<span class="discipline"><%=stringProperty("discipline")%></span>
<span class="duration"><p:duration name="duration"/></span>
<span class="distance"><p:distance name="distance"/></span>