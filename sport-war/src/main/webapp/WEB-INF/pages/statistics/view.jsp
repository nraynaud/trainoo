<%@ page import="com.nraynaud.sport.data.StatisticsPageData" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<p:layoutParams title="Statistiques"/>

<%
    final StatisticsPageData data = top(StatisticsPageData.class);
    if (data != null) {%>
distance totale parcourue (tous sports confondus)&nbsp;:<%=data.totalDistance%>km
<%
    }%>