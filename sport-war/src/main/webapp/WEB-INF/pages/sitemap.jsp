<?xml version="1.0" encoding="UTF-8"?>
<%@ page import="com.nraynaud.sport.data.SitemapData" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/xml;charset=UTF-8" language="java" %>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
    <%=url("http://trainoo.com/", "hourly", "0.8")%>
    <%
        final SitemapData data = Helpers.top(SitemapData.class);
        for (final Number id : data.userIds) {
            out.println(url("http://trainoo.com/bib/?id=" + String.valueOf(id), "weekly", "0.7"));
            out.println(url("http://trainoo.com/statistics/?id=" + String.valueOf(id), "weekly", "0.7"));
        }
        for (final Number id : data.workoutIds)
            out.println(url("http://trainoo.com/workout/?id=" + String.valueOf(id), "daily", "0.7"));
    %>
</urlset>
<%!
    static String url(final String url, final String freq, final String priority) {
        final String frequencyPart = freq == null ? "" : "<changefreq>" + freq + "</changefreq>";
        final String priorityPart = freq == null ? "" : "<priority>" + priority + "</priority>";
        return "<url><loc>" + url + "</loc>" + frequencyPart + priorityPart + "</url>";
    }
%>