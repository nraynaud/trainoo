<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<%final Iterable<User> users = top();%>

<ul class="userList">
    <%
        for (final User participant : users) {
            out.append("<li>")
                    .append(bibLink(participant, 10))
                    .append("</li>");
        }
    %>
</ul>
