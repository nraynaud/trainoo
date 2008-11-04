<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<p:layoutParams title="Statistiques"/>

distance totale parcourue (tous sports confondus)&nbsp;:<%=Helpers.property("totalDistance", Double.class)%>km