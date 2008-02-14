package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
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
public class WriteAction extends ChainBackAction {
    public String content;
    public String receiver;
    public Long aboutWorkoutId;

    public static final String CONTENT_MAX_LENGTH = "4000";

    public WriteAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        try {
            application.createPrivateMessage(getUser(), receiver, content, new Date(), aboutWorkoutId);
            return SUCCESS;
        } catch (UserNotFoundException e) {
            addFieldError("receiver", "L'utilisateur '" + receiver + "' n'existe pas.");
            return INPUT;
        } catch (WorkoutNotFoundException e) {
            throw new DataInputException(e);
        }
    }

    @RequiredStringValidator(message = "Vous avez oublié le message.")
    @StringLengthFieldValidator(message = "Le message doit faire moins de ${maxLength} caratères.",
            maxLength = CONTENT_MAX_LENGTH)
    public void setContent(final String content) {
        this.content = content;
    }

    @RequiredStringValidator(message = "Vous avez oublié le destinataire")
    public void setReceiver(final String receiver) {
        this.receiver = receiver.length() > 0 ? receiver : null;
    }
}
