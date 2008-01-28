package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.data.ConversationData;
import com.nraynaud.sport.web.*;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.Redirect;
import com.nraynaud.sport.web.result.RedirectBack;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.Date;

@Results({
@Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION),
@Result(name = Action.INPUT, type = ChainBack.class, value = "LOL"),
@Result(name = "site-index", type = Redirect.class, value = "globalWorkouts", params = {"namespace", "/"})
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class MessagesAction extends DefaultAction {
    private String content;
    private String receiver;
    private final Application application;
    private SportRequest request;
    private Long aboutWorkoutId;
    private boolean publicMessage = false;
    private ConversationData conversationData;

    private String fromAction;

    public static final String CONTENT_MAX_LENGTH = "4000";

    public MessagesAction(final Application application) {
        this.application = application;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        return "site-index";
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

    public ConversationData getConversationData() {
        if (conversationData == null && receiver != null) {
            try {
                conversationData = application.fetchConvertationData(getUser(), receiver, aboutWorkoutId);
            } catch (WorkoutNotFoundException e) {
                throw new DataInputException(e);
            }
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

    //transparent action
    public String getActionDescription() {
        return getFromAction();
    }
}
