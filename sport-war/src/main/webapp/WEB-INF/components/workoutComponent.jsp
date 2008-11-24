<%@ page import="static com.nraynaud.sport.formatting.FormatHelper.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.view.StackUtil" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<span class="userName"><%=stringProperty("user.name")%></span>
<span class="date"><s:date name="date" format="dd/M"/></span>
<span class="discipline"><%=stringProperty("discipline")%></span>
<span class="duration"><%=formatDuration(StackUtil.<Long>property("duration"), "")%></span>
<span class="distance"><%=formatDistance(StackUtil.<Double>property("distance"), "")%></span>