<%@ page import="static com.nraynaud.sport.web.view.Helpers.allowOverrides" %>
<%@ page import="com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<% allowOverrides();%>

<form action="<%=stringParam("action") +"#workoutMessage"%>" method="POST" class="workoutEditor">

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
            <tr>
                <th><label for="energy" style="vertical-align:text-top;">Énergie dépensée&nbsp;:</label></th>
                <td colspan="3"><s:textfield id="energy" name="energy" size="6" maxlength="10"
                                             onfocus="showWorkoutToolTip(event, 'En kilocalories.', 'energy', this.value);"
                                             onblur="hideToolTip();"
                                             onkeyup="feedback('energy', this.value)"/></td>
            </tr>
            <tr>
                <th><label for="comment" style="vertical-align:text-top;">Compte rendu&nbsp;:</label></th>
                <td colspan="3"><s:textarea id="comment" name="comment"></s:textarea></td>
                <p:javascript>makeItCount('comment', <%=AbstractWorkoutAction.MAX_COMMENT_LENGTH%>);</p:javascript>
            </tr>
        </table>
    </div>
    <span class="actions">
        <input type="submit" name="submit" class="submit" value="<s:property value="%{parameters.submit}"/>">
    </span>
</form>
<%if (boolParam("showDelete")) {%>
<form action="<%=createUrl("/workout", "delete")%>">
    <span class="actions">
        <s:hidden name="id" value="%{id}"/>
        <input type="submit" class="submit" name="delete" value="Supprimer"/>
    </span>
</form>
<%}%>
<i>Tous les champs sauf la date sont optionnels.</i>
