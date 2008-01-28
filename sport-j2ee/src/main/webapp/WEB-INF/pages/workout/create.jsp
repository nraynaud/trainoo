<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>


<p:layoutParams title="Création d'un entraînement"/>

<s:url id="createteurl" namespace="/workout" action="create"/>
<% Helpers.call(pageContext, "workoutForm.jsp", "action", "createteurl", "submit", "'Ajouter'");%>

<p:javascript>Field.activate('date');</p:javascript>