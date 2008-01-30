<%@ page import="static com.nraynaud.sport.web.view.Helpers.allowOverrides" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<% allowOverrides();%>
<form action="<s:property value="%{parameters.action + '#workoutMessage'}"/>" method="post">

    <s:actionerror/>
    <s:actionmessage/>

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
                             onfocus="$('info').show(); feedback('date', this.value); $('tip').update('Format&nbsp;: jj/mm/aaaa ex&nbsp;: 03/10/2006.')"
                             onblur="$('info').hide()"
                             onkeyup="feedback('date', this.value)"
                             value="%{date == null  ? 'aujourd\\'hui' : date}"/></td>
            <td><s:textfield id="duration"
                             name="duration"
                             size="6"
                             onfocus="Element.show('info');feedback('duration', this.value); $('tip').update('Format&nbsp;: HHhmm\\'ss ex&nbsp;: 03h41\\'17 ou 40\\'22')"
                             onblur="Element.hide('info')"
                             onkeyup="feedback('duration', this.value)"/></td>

            <td><s:textfield id="distance"
                             name="distance"
                             size="6"
                             onfocus="Element.show('info');feedback('distance', this.value); $('tip').update('En kilomètres.')"
                             onblur="Element.hide('info')"
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
<span id="info" style="display:none;">
    <span class="feedback" id="feedback"> </span><br/>
    <span id="tip"> </span>
</span>
