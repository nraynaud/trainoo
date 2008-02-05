<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.nraynaud.sport.Message" %>
<%@ page import="com.nraynaud.sport.PrivateMessage" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.Workout" %>

<s:iterator value="top">
    <%
        final Message message = (Message) top();
        final String cssClasses;
        if (message instanceof PrivateMessage) {
            final PrivateMessage privateMessage = (PrivateMessage) message;
            cssClasses = (currentUser().equals(privateMessage.getReceiver()) ? "received" : "sent")
                    + (privateMessage.isNew() ? " newMessage" : "");
        } else {
            cssClasses = "public";
        }
    %>
    <div class="message <%=cssClasses%>">
        <% final Workout workout = message.getWorkout();%>
        <div class="messageHeading">
            <div style="float:left;">
                <s:date name="date" format="E dd/M à HH:mm"/>
                <% final String name = escaped(message.getSender().getName()); %>
                        <span class="message_from">
                            <% if (isLogged()) {%>
                            <s:url id="bibUrl" action="" namespace="/bib" includeParams="none">
                                <s:param name="id" value="sender.id"/>
                            </s:url>
                            <a href="<%=property("bibUrl")%>" title="Voir son dossard"><%=name%>
                            </a>
                            <% } else {%>
                            <%=name%>
                            <%}%>
                        </span>
                a écrit&nbsp;:
            </div>
            <%if (message.canDelete(currentUser())) {%>
            <div style="float:right;">
                <form name="delete" action="<%=deleteUrl(message)%>" method="post">
                    <s:hidden name="id" value="%{id}"/>
                    <s:hidden name="fromAction" value="%{actionDescription}"/>
                    <s:submit value="X" title="supprimer"/>
                </form>
            </div>
            <%}%>
        </div>
        <% if (workout != null) {%>
        <div class="workout">à propos de la sortie&nbsp;:
            <span class="tinyWorkout"><% call(pageContext, "workoutComponent.jsp", workout);%></span>
        </div>
        <% } %>
        <p class="messageContent"><%= multilineText(message.getContent())%>
        </p>
    </div>
</s:iterator><%!
    private static String deleteUrl(final Message message) {
        return message instanceof PrivateMessage ? urlFor("/messages", "delete") : urlFor("/messages", "deletePublic");
    }

    private static String urlFor(final String namespace, final String action) {
        return namespace + '/' + action;
    }
%>