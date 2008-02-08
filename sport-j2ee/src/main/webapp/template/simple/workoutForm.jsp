<%@ page import="static com.nraynaud.sport.web.view.Helpers.allowOverrides" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<% allowOverrides();%>
<form action="<s:property value="%{parameters.action + '#workoutMessage'}"/>" method="post">

    <s:actionerror/>

    <s:fielderror id="workoutMessage">
        <s:param value="'discipline'"/>
        <s:param value="'date'"/>
        <s:param value="'duration'"/>
        <s:param value="'distance'"/>
    </s:fielderror>
    <a name="workoutMessage">&nbsp;</a>

    <s:hidden name="fromAction" value="%{actionDescription}"/>
    <table>
        <tr>
            <th><label for="discipline">Discipline</label></th>
            <th><label for="date">Date</label></th>
            <th><label for="duration">Durée</label></th>
            <th><label for="distance">Distance</label></th>
        </tr>
        <tr>
            <td><s:select id="discipline" list="{'course', 'vélo', 'VTT', 'marche', 'natation', 'roller'}"
                          name="discipline"
                          required="true"/></td>
            <td><s:textfield id="date"
                             name="date"
                             size="10"
                             onfocus="$('info').style.visibility='visible'; feedback('date', this.value); $('tip').update('ex&nbsp;: 03/10/2006 ou \"hier\"')"
                             onblur="$('info').style.visibility='hidden'"
                             onkeyup="feedback('date', this.value)"
                             value="%{date == null  ? 'aujourd\\'hui' : date}"/></td>
            <td><s:textfield id="duration"
                             name="duration"
                             size="6"
                             onfocus="$('info').style.visibility='visible';feedback('duration', this.value); $('tip').update('ex&nbsp;: 03h41\\'17 ou 40\\'22')"
                             onblur="$('info').style.visibility='hidden'"
                             onkeyup="feedback('duration', this.value)"/></td>

            <td><s:textfield id="distance"
                             name="distance"
                             size="6"
                             onfocus="$('info').style.visibility='visible';feedback('distance', this.value); $('tip').update('En kilomètres.')"
                             onblur="$('info').style.visibility='hidden'"
                             onkeyup="feedback('distance', this.value)"/></td>
            <td>
                <div style="margin-left:1em;">
                    <input type="submit" name="submit" value="<s:property value="%{parameters.submit}"/>">
                </div>
            </td>

            <s:if test="%{parameters.showDelete}">
                <td>
                    <input type="submit" name="delete" value="Supprimer"/>
                </td>
            </s:if>
        </tr>
    </table>
</form>
<span id="info" style="visibility:hidden;">
    <span class="feedback" id="feedback">&nbsp;</span><br/>
    <span id="tip">&nbsp;</span>
</span>
<s:if test="%{parameters.showDelete}">
    <div style="text-align:center;margin-top:2em"><a
            href="<s:url action="workouts" namespace="/" includeParams="none"/>">Annuler et
        revenir à mon vestiaire</a></div>
</s:if>