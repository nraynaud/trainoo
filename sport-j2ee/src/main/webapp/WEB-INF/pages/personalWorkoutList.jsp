<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>

<p:defineTitle value="Vos derniers entraînements"/>


<h1>Vos Entraînements</h1>
    <span style="display:inline-block;">
        <s:component template="workoutTable.jsp"/>
    </span>
<hr/>

<h2>Nouvel entraînement</h2>

<div style="display:block;">
    <s:component template="workoutForm.jsp"/>
    <div class="tip" id="date_tip" style="display:none;">
        <span class="feedback" id="date_feedback"> </span><br/><span>Saisissez la date au format jj/mm/aaaa ex: 03/10/2006.</span>
    </div>
</div>

