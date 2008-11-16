<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.formatting.FormatHelper" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<span class="userName"><%=stringProperty("user.name")%></span>
<span class="date"><s:date name="date" format="dd/M"/></span>
<span class="discipline"><%=stringProperty("discipline")%></span>
<span class="duration"><%=FormatHelper.formatDuration(property("duration", Long.class), "")%></span>
<span class="distance"><%=FormatHelper.formatDistance(property("distance", Double.class), "")%></span>