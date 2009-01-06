<%@ page import="com.nraynaud.sport.Helper" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.UserString" %>
<%@ page import="com.nraynaud.sport.data.StatisticsPageData" %>
<%@ page import="com.nraynaud.sport.formatting.FormatHelper" %>
<%@ page import="java.util.LinkedList" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%
    final StatisticsPageData data = top();
    final User user = data.user;
    final boolean lookingOwnStats = user.equals(currentUser());
    final String discipline = stringProperty("discipline");
%>
<p:layoutParams title='<%=lookingOwnStats ? "Mes statistiques" : "Les statistiques de " + user.getName()%>'/>

<form id="disciplineForm" action="" method="GET">
    <input type="hidden" name="id" value="<%=property("id")%>">
    <input id="hiddenDiscipline" type="hidden" name="discipline" value="<%=discipline%>">
</form>

<form id="disciplineSelectForm" action="" style="font-size:20px">
    <%
        final LinkedList<String> values = new LinkedList<String>();
        values.add("");
        for (final UserString userDiscipline : data.userDisciplines)
            values.add(userDiscipline.toString());
        final LinkedList<String> labels = new LinkedList<String>();
        labels.add("Tous les Sports");
        for (final UserString userDiscipline : data.userDisciplines)
            labels.add(userDiscipline.toString());
    %>
    <%=selectComponent("discipline", "discipline", values, labels, Helper.escaped(discipline))%>
    <p:javascript>Event.observe('discipline', 'change', function(e) {
        var d = $('discipline').value;
        if (d.length == 0)
        $('disciplineForm').removeChild($('hiddenDiscipline'));
        else
        $('hiddenDiscipline').value = d;
        $('disciplineForm').submit();
        })
    </p:javascript>
</form>
<br>
Distance totale parcourue&nbsp;:<%=data.totalDistance%>km <br>

<div style="width:50%">
    <div class="block sheetBlock userSheetBlock">
        <div class="header">
            <div class="deco"></div>
            <h3>Cumul par année</h3>
        </div>
        <div class="content">
            <div class="deco"></div>
            <ul class="sheet">
                <% {
                    boolean even = true;
                    for (final StatisticsPageData.PeriodData<StatisticsPageData.WorkoutStat> longPeriodData : data.distanceByYear) {
                        even = !even;
                %>
                <li class="<%=even ? "odd":"even"%>">
                    <a>
                        <span class="period"><%=longPeriodData.period%></span>
                        <span class="data"><%=FormatHelper.formatDistance(longPeriodData.data.distance,
                                "&nbsp;")%></span>
                        <span class="data"><%=FormatHelper.formatDuration(longPeriodData.data.duration,
                                "&nbsp;")%></span>
                        <span class="data"><%=FormatHelper.formatEnergy(longPeriodData.data.energy, "&nbsp;")%></span>
                    </a>
                </li>
                <%
                        }
                    }%>
            </ul>
        </div>
        <div class="footer">
            <div class="deco"></div>
        </div>
    </div>


    <div class="block sheetBlock userSheetBlock">
        <div class="header">
            <div class="deco"></div>
            <h3>Cumul par mois</h3>
        </div>
        <div class="content">
            <div class="deco"></div>
            <ul class="sheet">
                <%
                    {
                        boolean even = true;
                        for (final StatisticsPageData.PeriodData<StatisticsPageData.WorkoutStat> longPeriodData : data.distanceByMonth) {
                            even = !even;
                %>
                <li class="<%=even ? "odd":"even"%>">
                    <a>
                        <span class="period"><%=longPeriodData.period%></span>
                        <span class="data"><%=FormatHelper.formatDistance(longPeriodData.data.distance,
                                "&nbsp;")%></span>
                        <span class="data"><%=FormatHelper.formatDuration(longPeriodData.data.duration,
                                "&nbsp;")%></span>
                        <span class="data"><%=FormatHelper.formatEnergy(longPeriodData.data.energy, "&nbsp;")%></span>
                    </a>
                </li>
                <%
                        }
                    }%>
            </ul>
        </div>
        <div class="footer">
            <div class="deco"></div>
        </div>
    </div>
</div>