<%@ page import="com.nraynaud.sport.data.StatisticsPageData" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<p:layoutParams title="Statistiques"/>

<%
    final StatisticsPageData data = top(StatisticsPageData.class);
    if (data != null) {%>
Distance totale parcourue (tous sports confondus)&nbsp;:<%=data.totalDistance%>km <br>

<div style="width:40%">
    <div class="block sheetBlock userSheetBlock">
        <div class="header">
            <div class="deco"></div>
            <h3>Distance par année</h3>
        </div>
        <div class="content">
            <div class="deco"></div>
            <ul class="sheet">
                <% {
                    boolean even = true;
                    for (final StatisticsPageData.PeriodData<Long> longPeriodData : data.distanceByYear) {
                        even = !even;
                %>
                <li class="<%=even ? "odd":"even"%>">
                    <a>
                        <span class="period"><%=longPeriodData.period%></span>
                        <span class="data"><%=longPeriodData.data%>km</span>
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
            <h3>Distance par mois</h3>
        </div>
        <div class="content">
            <div class="deco"></div>
            <ul class="sheet">
                <%
                    {
                        boolean even = true;
                        for (final StatisticsPageData.PeriodData<Long> longPeriodData : data.distanceByMonth) {
                            even = !even;
                %>
                <li class="<%=even ? "odd":"even"%>">
                    <a>
                        <span class="period"><%=longPeriodData.period%></span>
                        <span class="data"><%=longPeriodData.data%>km</span>
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
<%
    }
%>