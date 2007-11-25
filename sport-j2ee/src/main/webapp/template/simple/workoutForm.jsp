<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<s:form action="workouts">
    <s:actionerror/>
    <s:actionmessage/>
    <s:fielderror/>
    <table>
        <tr>
            <th><label for="date">Date :</label></th>
            <th><label for="duration">Durée :</label></th>
            <th>&nbsp;</th>
        </tr>
        <tr>
            <td><s:textfield id="date"
                             name="newDate"
                             onfocus="Element.show('date_tip');feedback('date', this.value)"
                             onblur="Element.hide('date_tip')"
                             onkeyup="feedback('date', this.value)"/></td>
            <td><s:textfield id="duration"
                             name="duration"
                             onfocus="Element.show('duration_tip')"
                             onblur="Element.hide('duration_tip')"/></td>
            <td><s:submit value="Ajouter"/></td>
        </tr>
    </table>
</s:form>