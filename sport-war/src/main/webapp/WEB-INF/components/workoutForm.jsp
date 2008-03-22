<%@ page import="static com.nraynaud.sport.web.view.Helpers.allowOverrides" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<% allowOverrides();%>
<form action="<s:property value="%{parameters.action + '#workoutMessage'}"/>" method="post">

    <s:actionerror/>

    <s:fielderror id="workoutMessage">
        <s:param value="'discipline'"/>
        <s:param value="'date'"/>
        <s:param value="'duration'"/>
        <s:param value="'distance'"/>
    </s:fielderror>
    <a name="workoutMessage"></a>

    <s:hidden name="fromAction" value="%{actionDescription}"/>
    <div style="clear:both;">
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
                              required="true" value="discipline"/></td>
                <p:javascript>
                    function showWorkoutToolTip(event, tip, fieldName, val) {
                    var info = $('info');
                    $('info').style .visibility='visible';
                    $('tip').update(tip);
                    showToolTip(event, info);
                    feedback(fieldName, val);
                    }
                </p:javascript>
                <td><s:textfield id="date"
                                 name="date"
                                 size="10"
                                 maxlength="15"
                                 onfocus="showWorkoutToolTip(event,'ex&nbsp;: 03/10/2006 ou \"hier\"' , 'date', this.value);"
                                 onblur="hideToolTip();"
                                 onkeyup="feedback('date', this.value)"
                                 onmouseover=""
                                 onmouseout=""
                                 value="%{date == null  ? 'aujourd\\'hui' : date}"/>
                </td>
                <td><s:textfield id="duration"
                                 name="duration"
                                 size="6"
                                 maxlength="10"
                                 onfocus="showWorkoutToolTip(event,'ex&nbsp;: 03h41\\\'17 ou 40\\\'22' , 'duration', this.value);"
                                 onblur="hideToolTip();"
                                 onkeyup="feedback('duration', this.value)"/></td>

                <td><s:textfield id="distance"
                                 name="distance"
                                 size="6"
                                 maxlength="10"
                                 onfocus="showWorkoutToolTip(event, 'En kilomètres.', 'distance', this.value);"
                                 onblur="hideToolTip();"
                                 onkeyup="feedback('distance', this.value)"/></td>
            </tr>
        </table>
    </div>
    <div style="float:right;">
        <input type="submit" name="submit" value="<s:property value="%{parameters.submit}"/>">
    </div>

</form>
<s:if test="%{parameters.showDelete}">
    <div style="float:right;">
        <s:form namespace="/workout" action="delete">
            <s:hidden name="id" value="%{id}"/>
            <input type="submit" name="delete" value="Supprimer"/>
        </s:form>
    </div>
</s:if>
<div id="info" style="visibility:hidden;clear:left;">
    <span id="tip">&nbsp;</span><br>
    &nbsp;<span class="feedback" id="feedback">&nbsp;</span>
</div>