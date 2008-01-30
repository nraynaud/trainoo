package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.web.*;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.RedirectBack;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

import java.util.Date;

@Results({
@Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION),
@Result(name = Action.INPUT, type = ChainBack.class, value = "/WEB-INF/pages/messages.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class WriteAction extends DefaultAction implements ChainBackCapable {
    private final Application application;
    private String content;
    private String receiver;
    private Long aboutWorkoutId;
    private boolean publicMessage = false;

    private String fromAction;

    public static final String CONTENT_MAX_LENGTH = "4000";

    public WriteAction(final Application application) {
        this.application = application;
    }

    @PostOnly
    public String create() {
        if (!publicMessage && receiver == null) {
            addFieldError("receiver", "Vous avez oublié le destinataire.");
            return INPUT;
        }
        if (publicMessage) {
            try {
                application.createPublicMessage(getUser(), content, new Date(), aboutWorkoutId);
            } catch (WorkoutNotFoundException e) {
                throw new DataInputException(e);
            }
        } else
            return sendPrivateMessage();
        return SUCCESS;
    }

    private String sendPrivateMessage() {
        try {
            application.createPrivateMessage(getUser(), receiver, content, new Date(), aboutWorkoutId);
            return SUCCESS;
        } catch (UserNotFoundException e) {
            addFieldError("receiver", "L'utilisateur '" + getReceiver() + "' n'existe pas.");
            return INPUT;
        } catch (WorkoutNotFoundException e) {
            throw new DataInputException(e);
        }
    }

    @RequiredStringValidator(message = "Vous avez oublié le message.")
    @StringLengthFieldValidator(message = "La ville doit faire moins de ${maxLength} caratères.",
            maxLength = CONTENT_MAX_LENGTH)
    public void setContent(final String content) {
        this.content = content;
    }

    public void setReceiver(final String receiver) {
        this.receiver = receiver.length() > 0 ? receiver : null;
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

    public boolean isPublicMessage() {
        return publicMessage;
    }

    public void setPublicMessage(final boolean publicMessage) {
        this.publicMessage = publicMessage;
    }

    public String getFromAction() {
        return fromAction;
    }

    public void setFromAction(final String fromAction) {
        this.fromAction = fromAction;
    }
}
