<%@ page import="static com.nraynaud.sport.web.view.Helpers.allowOverrides" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<% allowOverrides();%>
<form action="<s:property value="%{parameters.action}"/>" method="post">

    <s:actionerror/>
    <s:actionmessage/>
    <s:fielderror/>

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
                             onfocus="Element.show('date_tip');feedback('date', this.value)"
                             onblur="Element.hide('date_tip')"
                             onkeyup="feedback('date', this.value)"/></td>
            <td><s:textfield id="duration"
                             name="duration"
                             size="6"
                             onfocus="Element.show('duration_tip');feedback('duration', this.value)"
                             onblur="Element.hide('duration_tip')"
                             onkeyup="feedback('duration', this.value)"/></td>

            <td><s:textfield id="distance"
                             name="distance"
                             size="6"
                             onfocus="Element.show('distance_tip');feedback('distance', this.value)"
                             onblur="Element.hide('distance_tip')"
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
<div class="tip" id="date_tip" style="display:none;">
    <span class="feedback"
          id="date_feedback"> </span><br/><span>Format&nbsp;: jj/mm/aaaa ex&nbsp;: 03/10/2006.</span>
</div>
<div class="tip" id="duration_tip" style="display:none;">
    <span class="feedback"
          id="duration_feedback"> </span><br/><span>Format&nbsp;: HHhmm'ss ex&nbsp;: 03h41'17 ou 40'22</span>
</div>
<div class="tip" id="distance_tip" style="display:none;">
    <span class="feedback"
          id="distance_feedback"> </span><br/><span>En kilomètres.</span>
</div>