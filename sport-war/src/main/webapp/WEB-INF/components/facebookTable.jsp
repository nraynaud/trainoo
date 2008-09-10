<%@ page import="com.nraynaud.sport.data.BibPageData" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<style type="text/css">
    <%@include file="/static/sport.css"%>
    <%@include file="/static/reset.css"%>
    <%@include file="/static/pimp/sport_pimp.css"%>
    ul.sheet li span.coms {
        background-image: url( <%=stat("/static/pimp/bulle.png")%> );
    }

    .sheetBlock .header,
        .sheetBlock .secondaryHeader,
        .sheetBlock .content,
        .sheetBlock .footer,
        .sheetBlock .deco,
    /*added*/
    .sheetBlock .deco,
        .sheetBlock .header .disciplineList li.current,
        .sheetBlock .header .disciplineList li.current a {
        background-image: url( <%=stat("/static/pimp/workoutsheet.png")%> );
    }
</style>
<% final BibPageData data = top(BibPageData.class); %>
<div style="max-width:450px">
    <div class="block sheetBlock userSheetBlock">
        <div class="header">
            <div class="deco"></div>
            <h3>Dernières sorties de <%=data.user.getName()%>
            </h3>
        </div>
        <div class="content">
            <div class="deco"></div>
            <%
                call(pageContext, "workoutTable.jsp", data.workouts, "urlPrefix", "http://trainoo.com");
            %>
        </div>
        <div class="footer">
            <div class="deco"></div>
        </div>
    </div>
</div>