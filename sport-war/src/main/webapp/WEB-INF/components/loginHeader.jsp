<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page import="com.nraynaud.sport.data.NewMessageData" %>
<%@ page import="java.util.List" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="loginHeader">
    <div class="left"><%= selectableLink("/", "", "Tableau général", null)%> <%=selectableLink("/groups", "", "Groupes",
            null)%>
    </div>
    <div class="right">
        <% if (isLogged()) {
            final List<NewMessageData> newMessages = property("newMessages", List.class);
            if (newMessages.size() > 0) {
                int count = 0;
                for (final NewMessageData privateMessage : newMessages) {
                    count += privateMessage.messageCount;
                }%>
        <s:url id="messagesURL" action="" namespace="/messages">
            <s:param name="receiver" value="newMessages.get(0).sender"/>
        </s:url>
        <a href="<s:property value="%{messagesURL}"/>" style="color:red"><%=count + " " + pluralize(count,
                "nouveau message", "nouveaux messages")%>
        </a>
        <%
            }
            final User user = currentUser();
        %>
        <span id="loginName"><!--<%=user.getId()%> --><%= user.getName()%><%= user.getNikePluEmail()
                != null ? selectableLink(
                "/privatedata", "refreshNikePlus", "<img src='/static/silk/arrow_refresh.png' alt='Rafraîchir nike+'/>",
                "Rafraîchir les données Nike+", "fromAction", findFromAction()) : ""%>
            <span style="font-size:x-small;"><%= selectableLink("/privatedata", "", "mon compte", null)%></span>
        </span>
        <%= selectableLink("/", "workouts", "Mon vestiaire", null)%><%= selectableLink("/bib", "", "Mon dossard", null,
            "id", String.valueOf(user.getId()))%>
        <a href="<s:url action="logout" namespace="/"/>">Déconnexion</a>
        <% } else {
            out.append(selectableLink("/", "forgotPassword", "Mot de passe oublié ?", null))
                    .append(loginUrl("Connexion"))
                    .append(' ')
                    .append(signupUrl("Inscription"));
        } %>
    </div>
</div>