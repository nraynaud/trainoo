<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.*" %>
<%@ page import="com.nraynaud.sport.data.PaginatedCollection" %>
<%@ page import="com.nraynaud.sport.web.ActionDetail" %>
<%@ page import="static com.nraynaud.sport.web.action.messages.WritePublicAction.CONTENT_MAX_LENGTH" %>
<%@ page import="com.nraynaud.sport.web.view.Helpers" %>

<div class="pagination">
<% final PaginatedCollection<Message> messages = top(PaginatedCollection.class);
    for (final Message message : messages) {
        push(message);
        try {
            final boolean isEditingMessage = message.getId()
                    .toString()
                    .equals(getFirstValue(EDIT_MESSAGE));
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
        <div style="float:left;margin-top:5px;">
            <s:date name="date" format="E dd/M à HH:mm:ss"/>
            <span class="message_from"><%=bibLink(message.getSender())%></span>
            a écrit&nbsp;:
        </div>
        <%
            final boolean canDelete = message.canDelete(currentUser());
            final boolean canEdit = message.canEdit(currentUser());
            if (!boolParam("showTopicLink") && (canDelete || canEdit)) {
        %>
        <div style="float:right;margin-top:5px;">
            <%=canEdit ? currenUrlWithParams("Modifier", false, EDIT_MESSAGE, String.valueOf(message.getId())) : ""%>
            <%if (canDelete) {%>
            <form name="delete" action="<%=deleteUrl(message)%>" method="post"
                  style="display:inline;vertical-align:top;padding:0; margin:0;">
                <s:hidden name="id" value="%{id}"/>
                <s:hidden name="fromAction" value="%{actionDescription}"/>
                <s:submit value="X" title="supprimer" template="bare-submit"/>
            </form>
            <%}%>
        </div>
        <%}%>
    </div>
    <% if (message.getWorkout() != null && !boolParam("hideWorkoutSubject")) {%>
    <div class="workout">à propos de la sortie&nbsp;:
        <span class="tinyWorkout"><% call(pageContext, "workoutComponent.jsp", message.getWorkout());%></span>
    </div>
    <% } %>
    <%if (isEditingMessage) {%>
    <form id="edit" name="edit" onsubmit="return true;" action="/messages/edit" method="post">
        <%
            allowOverrides();
            try {
        %>
        <s:actionerror/>
        <s:fielderror>
            <s:param value="'content'"/>
        </s:fielderror>
        <a name="errorMessage"> </a>
        <s:hidden name="id"/>
        <input type="hidden" name="messageKind" value="<%=message.getMessageKind()%>"/>
        <input type="hidden" name="fromAction"
               value="<%=property("actionDescription",ActionDetail.class).removeParam(EDIT_MESSAGE).removeParam("error")%>"/>
        <input type="hidden" name="onErrorAction"
               value="<%=property("actionDescription",ActionDetail.class).addParam("error", "editMessage")%>"/>

        <div><s:textarea id="editContent" name="content" rows="5" cssClass="messageContentArea"
                         cssStyle="border-width:2px" value="%{content.nonEscaped()}"/></div>
        <p:javascript>makeItCount('editContent', <%=CONTENT_MAX_LENGTH%>);
            $('editContent').focus();</p:javascript>
        <p class="submit"><%=currenUrlWithoutParam("Annuler", EDIT_MESSAGE)%> <s:submit value="Valider"
                                                                                        template="bare-submit"/></p>
        <%
            } finally {
                disAllowOverrides();
            }
        %>
    </form>
    <%} else {%>
    <p class="messageContent"><%= multilineText(message.getContent())%>
    </p>
    <%}%>
    <s:if test="%{parameters.showTopicLink}">
        <div class="messageFooter">
            <% if (message instanceof PublicMessage) {
                final PublicMessage publicMessage = (PublicMessage) message;
                if (publicMessage.getTopic() == Topic.Kind.WORKOUT) {
                    out.append(Helpers.selectableLink("/workout", "", "Voir la page de l'entraînement", null, "id",
                            String.valueOf(publicMessage.getWorkout().getId())));
                } else {
                    final Group group = publicMessage.getGroup();
                    out.append(
                            Helpers.selectableLink("/groups", "",
                                    "<span style='font-weight:normal'>Groupe </span>" + group.getName(), null, "id",
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
    private static final String EDIT_MESSAGE = "editMessage";

    private static String deleteUrl(final Message message) {
        return message instanceof PrivateMessage ? urlFor("/messages", "delete") : urlFor("/messages", "deletePublic");
    }

    private static String urlFor(final String namespace, final String action) {
        return namespace + '/' + action;
    }
%>