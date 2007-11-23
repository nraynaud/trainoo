<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<head>
    <title>Liste des Entraînements</title>
    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="center">
    <span id="loginName"><s:property value="userName"/></span> |
    <s:form id="logoutForm" action="logout" method="POST">
        <s:submit cssClass="logoutButton" value="Déconnexion"/>
    </s:form>


    <h1>Vos Entraînements</h1>
    <span style="display:inline-block;">
        <table>
            <s:iterator value="workouts">
                <tr>
                    <td><s:date name="date" format="E dd/M"/></td>
                </tr>
            </s:iterator>
        </table>
    </span>
    <hr/>

    <h2>Nouvel entrainement</h2>

    <div style="display:block;">
        <s:component template="workoutForm.jsp"/>
        <div class="tip" id="date_tip" style="display:none;">
            <span class="feedback" id="date_feedback"> </span><br/><span>Saisissez la date au format jj/mm/aaaa ex: 03/10/2006.</span>
        </div>

    </div>
</div>


<script language="JavaScript" type="text/javascript"
        src="<s:url value="/static/prototype_packed.js"/>"></script>
<script language="JavaScript" type="text/javascript"
        src="<s:url value="/static/sport.js"/>"></script>
</body>
</html>