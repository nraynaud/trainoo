<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.BibPageData" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<link href="<%=stat("/static/sport.css")%>" rel="stylesheet" type="text/css">
<link href="<%=stat("/static/reset.css")%>" rel="stylesheet" type="text/css">
<link href="<%=stat("/static/pimp/sport_pimp.css")%>" rel="stylesheet" type="text/css">
<style type="text/css">

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

Salut <%=stringProperty("name")%> !
<br>
Compte trainoo :
<form action="" method="POST">
    <% final String trainoo_account = stringProperty("trainoo_account");
        if (trainoo_account == null) {%>
    <input type="text" name="trainoo_account"> <input type="submit" value="Envoyer !">
    <% } else { %>
    <%=trainoo_account%> <input type="hidden" name="trainoo_account" value="0"> <input type="submit"
                                                                                       value="Supprimer !">
    <%}%>
</form>
<%
    if (trainoo_account != null) {
        final BibPageData data = property("model", BibPageData.class);
        if (data == null) {
%>
Désolé, ce compte est inconnu sur trainoo.com.
<%
} else {
%>
<div style="width:450px">
    <div class="block sheetBlock userSheetBlock">
        <div class="header">
            <div class="deco"></div>
            <h3>Dernières sorties de <%=data.user.getName()%>
            </h3>
        </div>
        <div class="content">
            <div class="deco"></div>
            <%
                call(pageContext, "workoutTable.jsp", data.workouts);
            %>
        </div>
        <div class="footer">
            <div class="deco"></div>
        </div>
    </div>
</div>
<%
        }
    }
%>