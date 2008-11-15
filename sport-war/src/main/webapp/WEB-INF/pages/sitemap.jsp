<?xml version="1.0" encoding="UTF-8"?>
<%@ page import="com.nraynaud.sport.data.SitemapData" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>
<%@ page session="false" contentType="text/xml;charset=UTF-8" language="java" %>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
    <url>
        <loc>http://trainoo.com/</loc>
        <changefreq>hourly</changefreq>
        <priority>0.8</priority>
    </url>
    <%
        final SitemapData data = Helpers.top(SitemapData.class);
        for (final Number id : data.userIds) {
    %>
    <url>
        <loc>http://trainoo.com/bib/?id=<%=id%></loc>
        <changefreq>weekly</changefreq>
        <priority>0.7</priority>
    </url>
    <url>
        <loc>http://trainoo.com/statistics/?id=<%=id%></loc>
        <changefreq>daily</changefreq>
        <priority>0.7</priority>
    </url>
    <%
        }
        for (final Number id : data.workoutIds) {
    %>
    <url>
        <loc>http://trainoo.com/workout/?id=<%=id%></loc>
        <changefreq>daily</changefreq>
        <priority>0.7</priority>
    </url>
    <%
        }
    %>
</urlset>
