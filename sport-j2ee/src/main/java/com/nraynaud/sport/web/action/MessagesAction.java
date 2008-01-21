package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.web.*;
import com.nraynaud.sport.web.result.Redirect;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

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

    public MessagesAction(final Application application) {
        this.application = application;
    }

    @RequiredStringValidator(message = "Vous avez oublié le message.")
    public void setContent(final String content) {
        this.content = content;
    }

    @PostOnly
    @WithToken
    public String create() {
        try {
            application.createMessage(getUser(), receiver, content, new Date());
        } catch (UserNotFoundException e) {
            addFieldError("receiver", "L'utilisateur '" + getReceiver() + "' n'existe pas.");
            return INPUT;
        }
        return SUCCESS;
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
}
