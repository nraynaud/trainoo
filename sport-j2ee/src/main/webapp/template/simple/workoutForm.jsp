<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<s:form action="%{parameters.action}">
    <s:actionerror/>
    <s:actionmessage/>
    <s:fielderror/>
    <table>
        <tr>
            <th><label for="discipline">Discipline</label></th>
            <th><label for="date">Date</label></th>
            <th><label for="duration">Durée</label></th>
            <th><label for="distance">Distance</label></th>
            <th>&nbsp;</th>
        </tr>
        <tr>
            <td><s:textfield id="discipline"
                             name="discipline"/></td>
            <td><s:textfield id="date"
                             name="date"
                             size="10"
                             onfocus="Element.show('date_tip');feedback('date', this.value)"
                             onblur="Element.hide('date_tip')"
                             onkeyup="feedback('date', this.value)"/></td>
            <td><s:textfield id="duration"
                             name="duration"
                             size="7"
                             onfocus="Element.show('duration_tip');feedback('duration', this.value)"
                             onblur="Element.hide('duration_tip')"
                             onkeyup="feedback('duration', this.value)"/></td>

            <td><s:textfield id="distance"
                             name="distance"
                             size="7"
                             onfocus="Element.show('distance_tip');feedback('distance', this.value)"
                             onblur="Element.hide('distance_tip')"
                             onkeyup="feedback('distance', this.value)"/></td>
            <td>
                <div style="margin-left:2em;"><s:submit value="%{parameters.submit}"/></div>
            </td>
        </tr>
    </table>
    <div class="tip" id="date_tip" style="display:none;">
        <span class="feedback" id="date_feedback"> </span><br/><span>Saisissez la date au format jj/mm/aaaa ex: 03/10/2006.</span>
    </div>
    <div class="tip" id="duration_tip" style="display:none;">
        <span class="feedback" id="duration_feedback"> </span><br/><span>Saisissez la durée de l'entraînement au format HHhmm'ss ex: 03h41'17 ou 40'22</span>
    </div>
    <div class="tip" id="distance_tip" style="display:none;">
        <span class="feedback" id="distance_feedback"> </span><br/><span>Saisissez la distance d'entraînement en kilomètres.</span>
    </div>
</s:form>