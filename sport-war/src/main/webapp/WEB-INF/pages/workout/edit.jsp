<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Modification d'un entraînement"/>

<div id="tinyCenter">
    <%
        final String id = property("id", String.class);
        call(pageContext, "workoutForm.jsp", null, "action", createUrl("/workout", "edit", "id", id), "showDelete",
                true, "submit", "Modifier");
    %>
    <%=selectableLink("/workout", "participants", "Ajouter des participants", "Ajouter des participants", "id", id)%>
    <div style="text-align:center;margin-top:2em; clear:both;"><a href="<%=createUrl("/",  "workouts")%>">Annuler et
        revenir à mon vestiaire</a>
    </div>
</div>

<p:javascript>$('date').focus();</p:javascript>