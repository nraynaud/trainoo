package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.ConversationData;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.SportRequest;
import com.nraynaud.sport.web.result.Redirect;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.Date;

@Results({
@Result(type = Redirect.class, value = Constants.WORKOUTS_ACTION),
@Result(name = Action.INPUT, value = "/WEB-INF/pages/messages.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class MessagesAction extends DefaultAction {

    private String content;
    private String receiver;
    private final Application application;
    private SportRequest request;
    private Long aboutWorkoutId;
    private ConversationData conversationData;

    public static final String CONTENT_MAX_LENGTH = "4000";

    public MessagesAction(final Application application) {
        this.application = application;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        return INPUT;
    }

    @PostOnly
    public String create() {
        try {
            application.createMessage(getUser(), receiver, content, new Date(), aboutWorkoutId);
        } catch (UserNotFoundException e) {
            addFieldError("receiver", "L'utilisateur '" + getReceiver() + "' n'existe pas.");
            return INPUT;
        }
        return SUCCESS;
    }

    public ConversationData getConversationData() {
        if (conversationData == null) {
            conversationData = application.fetchConvertationData(getUser(), receiver, aboutWorkoutId);
        }
        return conversationData;
    }

    @RequiredStringValidator(message = "Vous avez oublié le message.")
    @StringLengthFieldValidator(message = "La ville doit faire moins de ${maxLength} caratères.",
            maxLength = CONTENT_MAX_LENGTH)
    public void setContent(final String content) {
        this.content = content;
    }

    public void setRequest(final SportRequest request) {
        this.request = request;
    }

    public User getUser() {
        return request.getSportSession().getUser();
    }

    public void setReceiver(final String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public String getReceiver() {
        return receiver;
    }

    public Long getAboutWorkoutId() {
        return aboutWorkoutId;
    }

    public void setAboutWorkoutId(final Long aboutWorkoutId) {
        this.aboutWorkoutId = aboutWorkoutId;
    }
}
