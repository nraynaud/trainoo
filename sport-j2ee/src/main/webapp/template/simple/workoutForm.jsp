<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<s:form action="workouts">

    <s:actionerror/>
    <s:actionmessage/>
    <s:fielderror/>
    <s:label for="date">Date :</s:label>
    <s:textfield id="date"
                 name="newDate"
                 onfocus="Element.show('date_tip');feedback('date', this.value)"
                 onblur="Element.hide('date_tip')"
                 onkeyup="feedback('date', this.value)"/>
    <br>
    <s:label for="duration">Duration :</s:label>
    <s:textfield id="duration"
                 name="duration"
                 onfocus="Element.show('duration_tip')"
                 onblur="Element.hide('duration_tip')"/>

    <s:submit value="Ajouter"/>
</s:form>