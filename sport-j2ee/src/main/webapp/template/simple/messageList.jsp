<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.*" %>
<%@ page import="com.nraynaud.sport.data.PaginatedCollection" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>

<div class="pagination">
    <% final PaginatedCollection<Message> messages = (PaginatedCollection<Message>) top();%>
    <%
        for (final Message message : messages) {
            push(message);
            try {
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
        <div class="messageHeading">
            <div style="float:left;">
                <s:date name="date" format="E dd/M à HH:mm:ss"/>
                <% final String name = message.getSender().getName().toString(); %>
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
        <% if (message.getWorkout() != null) {%>
        <div class="workout">à propos de la sortie&nbsp;:
            <span class="tinyWorkout"><% call(pageContext, "workoutComponent.jsp", message.getWorkout());%></span>
        </div>
        <% } %>
        <p class="messageContent"><%= multilineText(message.getContent())%>
        </p>
        <s:if test="%{parameters.showTopicLink}">
            <div class="messageFooter">
                <% if (message instanceof PublicMessage) {
                    final PublicMessage publicMessage = (PublicMessage) message;
                    if (publicMessage.getTopic() == Topic.Kind.WORKOUT) {
                        out.append(Helpers.selectableUrl("/workout", "", "Voir la page de l'entraînement", "id",
                                String.valueOf(publicMessage.getWorkout().getId())));
                    } else {
                        final Group group = publicMessage.getGroup();
                        out.append(
                                Helpers.selectableUrl("/groups", "",
                                        "<span style='font-weight:normal'>Groupe </span>" + group.getName(), "id",
                                        String.valueOf(group.getId())));
                    }
                }
                %>
            </div>
        </s:if>
    </div>
    <%
            } finally {
                pop();
            }
        }
    %>
    <%if (messages.hasPrevious()) { %>
    <s:url id="previousPageUrl" includeParams="get">
        <s:param name="%{parameters.pageVariable}" value="previousIndex"/>
    </s:url>
    <div class="paginationPrevious"><s:a href="%{previousPageUrl}">&lt;&lt;-Précédents</s:a></div>
    <%}%>
    <%if (messages.hasNext()) { %>
    <s:url id="nextPageUrl" includeParams="get">
        <s:param name="%{parameters.pageVariable}" value="nextIndex"/>
    </s:url>
    <div class="paginationNext"><s:a href="%{nextPageUrl}">Suivants->></s:a></div>
    <%}%>
</div>
<%!
    private static String deleteUrl(final Message message) {
        return message instanceof PrivateMessage ? urlFor("/messages", "delete") : urlFor("/messages", "deletePublic");
    }

    private static String urlFor(final String namespace, final String action) {
        return namespace + '/' + action;
    }
%>