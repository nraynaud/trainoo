<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.*" %>
<%@ page import="com.nraynaud.sport.data.PaginatedCollection" %>
<%@ page import="com.nraynaud.sport.formatting.DateHelper" %>
<%@ page import="static com.nraynaud.sport.web.action.messages.WritePublicAction.CONTENT_MAX_LENGTH" %>
<%@ page import="com.nraynaud.sport.web.view.StackUtil" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="java.util.HashMap" %>

<% final PaginatedCollection<Message> messages = top();
    for (final Message message : messages) {
        final int stackIndex = push(message);
        try {
            final boolean isEditingMessage = message.getId()
                    .toString()
                    .equals(getFirstValue(EDIT_MESSAGE));
%>
<div class="block messageBlock">
<div class="decoLeft">
    <div class="decoRight">
        <div class="heading">
                <span class="primary"><span class="decoPrimary">
                    Par <%=bibLink(message.getSender(), 20)%>&nbsp;:
                </span></span>
                <span class="secondary"><%=DateHelper.humanizePastDate(message.getDate(),
                        "'Aujourd''hui à 'HH'h'mm",
                        "'Hier à 'HH'h'mm",
                        "'Avant-hier à 'HH'h'mm",
                        "'Le 'd MMMM")%></span>
        </div>
        <%
            final boolean canDelete = message.canDelete(currentUser());
            final boolean canEdit = message.canEdit(currentUser());
        %>
        <div class="content textContent">
            <%if (isEditingMessage) {%>
            <form method="POST" action="<%=createUrl("/messages", "edit")%>">
                <%
                    allowOverrides();
                    try {
                %>
                <s:actionerror/>
                <s:fielderror>
                    <s:param value="'content'"/>
                </s:fielderror>
                <a name="errorMessage"> </a>
                <input type="hidden" name="id" value="<%=message.getId()%>">
                <input type="hidden" name="messageKind" value="<%=message.getMessageKind()%>">
                <input type="hidden" name="fromAction"
                       value="<%=currentAction().removeParam(EDIT_MESSAGE).removeParam("error")%>">
                <input type="hidden" name="onErrorAction" value="<%=currentAction().addParam("error", "editMessage")%>">
                        
                    <span class="input">
                        <%=textArea("editContent", "content", StackUtil.<UserString>property("content").nonEscaped())%>
                    </span>
                <p:javascript>makeItCount('editContent', <%=CONTENT_MAX_LENGTH%>);
                    $('editContent').focus();</p:javascript>
                    
                    <span class="actions">
                        <a href="<%=currentUrlLinkWithAndWithoutParams(EDIT_MESSAGE, new HashMap<String, String>())%>"
                           title="Annuler">Annuler</a>
                        <input type="submit" name="submit" value="Valider">
                    </span>

                <%
                    } finally {
                        disAllowOverrides();
                    }
                %>
            </form>
            <%} else {%>
            <%
                if (message instanceof PublicMessage && boolParam("showTopicLink")) {
                    final PublicMessage publicMessage = (PublicMessage) message;
                    if (publicMessage.getTopic() == Topic.Kind.WORKOUT) {
                        if (!boolParam("hideWorkoutSubject")) {
            %>
            <div class="subHeading">à propos de la <a
                    href="<%=createUrl("/workout", "", "id", String.valueOf(message.getWorkout().getId()))%>">Sortie <%
                call(pageContext, "workoutComponent.jsp", message.getWorkout());%></a>
            </div>
            <% }
            } else {
                final Group group = publicMessage.getGroup();%>
            <div class="subHeading">dans le groupe <a
                    href="<%=createUrl("/groups", "", "id", String.valueOf(group.getId()))%>">
                <%=group.getName()%>
            </a>
            </div>
            <%
                    }
                }
            %>
            <% if (!boolParam("hideToolbar")) {%>
            <div class="smallButtonList">
                <% if (canEdit) {%>
                <a href="<%=currentUrlLinkWithAndWithoutParams(null, new HashMap<String, String>(),
                        EDIT_MESSAGE, String.valueOf(message.getId()))%>"
                   title="Modifier ce message" class="button editButton">Modifier</a>
                <%}%>
                <%if (canDelete) {%>
                <form action="<%=deleteUrl(message)%>" method="POST">
                    <input type="hidden" name="id" value="<%=message.getId()%>">
                    <input type="hidden" name="fromAction" value="<%=currentAction()%>">
                    <%final String deleteId = uniqueId("editDelete");%>
                    <label for="<%=deleteId%>" class="button deleteButton">Supprimer</label>
                    <input id="<%=deleteId%>" type="image" src="<%=stat("/static/blank.gif")%>" value="Supprimer"
                           title="Supprimer ce message" name="submit" class="image">
                </form>
                <%}%>
            </div>
            <%}%>
            <p class="messageContent"><%= multilineText(message.getContent())%>
            </p>
            <%}%>
        </div>
    </div>
</div>
</div>
<%

        } finally {
            pop(stackIndex);
        }
    }

%>
<%!
    private static final String EDIT_MESSAGE = "editMessage";

    private static String deleteUrl(final Message message) {
        return message instanceof PrivateMessage ? createUrl("/messages", "delete") : createUrl("/messages",
                "deletePublic");
    }
%>
