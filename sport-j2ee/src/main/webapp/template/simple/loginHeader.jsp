<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.NewMessageData" %>
<%@ page import="java.util.List" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="loginHeader">
    <div class="left"><%= selectableUrl("/", "", "Tableau général")%>
    </div>
    <div class="right">
        <% if (isLogged()) {
            final List<NewMessageData> newMessages = (List<NewMessageData>) property("newMessages");
            if (newMessages.size() > 0) {
                int count = 0;
                for (final NewMessageData privateMessage : newMessages) {
                    count += privateMessage.messageCount;
                }%>
        <s:url id="messagesURL" action="" namespace="/messages">
            <s:param name="receiver" value="newMessages.get(0).sender"/>
        </s:url>
        <a href="<s:property value="%{messagesURL}"/>" style="color:red"><%=count%> nouveau(x) message(s)</a>
        <%}%>
        <span id="loginName"><!--<%=currentUser().getId()%> --><%= escaped(currentUser().getName())%>
            <span style="font-size:x-small;"><%= selectableUrl("/privatedata", "", "(mot de passe)")%></span>
        </span>
        <%= selectableUrl("/", "workouts", "Mon vestiaire")%><%= selectableUrl("/bib", "", "Mon dossard", "id",
            String.valueOf(currentUser().getId()))%>
        <a href="<s:url action="logout" namespace="/"/>">Déconnexion</a>
        <% } else {
            out.append(loginUrl("Connexion")).append(' ').append(signupUrl("Inscription"));
        } %>
    </div>
</div>
<%!

%>