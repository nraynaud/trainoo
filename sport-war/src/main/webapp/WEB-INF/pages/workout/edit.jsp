<%@ page import="com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="com.nraynaud.sport.web.view.WorkoutPageDetails" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<% final WorkoutPageDetails pageDetail = top();%>
<p:layoutParams title='<%=pageDetail.pageTile%>' showTitleInPage="false"/>

<%
    call(pageContext, "workoutForm2.jsp", null,
            "actionUrl", pageDetail.doAction,
            "title", pageDetail.pageTile,
            "fromAction", pageDetail.cancelAction,
            "workoutView", pageDetail.workoutView);
%>

<div id="globalLeft">
    &nbsp;
</div>

<div id="globalRight">

    <h2>
        Compte rendu
    </h2>

    <div class="block debriefBlock">
        <div class="content textContent">
            <p>
                <span class="input">
                    <%=textArea("externalComment", "externalComment", pageDetail.workoutView.comment)%>
                </span>
            </p>
            <p:javascript>makeItCount('externalComment', <%=AbstractWorkoutAction.MAX_COMMENT_LENGTH%>);</p:javascript>
        </div>
    </div>
</div>
