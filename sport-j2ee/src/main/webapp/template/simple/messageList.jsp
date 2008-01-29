<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.nraynaud.sport.Message" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>

<s:iterator value="top">
    <%
        final Message message = (Message) top();
        final String cssClass = message.isPublic() ? "public" : currentUser().equals(
                message.getReceiver()) ? "received" : "sent";
    %>
    <div class="message <%=cssClass%>">
        <% final Workout workout = message.getWorkout();%>
                <span class="messageHeading">
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
                </span>
        <% if (workout != null) {%>
        <div class="workout">à propos de la sortie&nbsp;:
            <span class="tinyWorkout"><% call(pageContext, "workoutComponent.jsp", workout);%></span>
        </div>
        <% } %>
        <p class="messageContent"><%= multilineText(message.getContent())%>
        </p>
    </div>
</s:iterator>