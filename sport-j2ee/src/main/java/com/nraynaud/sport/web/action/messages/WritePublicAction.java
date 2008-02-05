package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.web.ChainBackCapable;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.RedirectBack;
import static com.opensymphony.xwork2.Action.INPUT;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

import java.util.Date;

@Results({
@Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION),
@Result(name = INPUT, type = ChainBack.class, value = "/WEB-INF/pages/messages.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class WritePublicAction extends DefaultAction implements ChainBackCapable {
    private String content;
    public Long aboutWorkoutId;
    public String fromAction;

    public static final String CONTENT_MAX_LENGTH = "4000";

    public WritePublicAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        try {
            application.createPublicMessage(getUser(), content, new Date(), aboutWorkoutId);
        } catch (WorkoutNotFoundException e) {
            throw new DataInputException(e);
        }
        return SUCCESS;
    }

    @RequiredStringValidator(message = "Vous avez oublié le message.")
    @StringLengthFieldValidator(message = "La ville doit faire moins de ${maxLength} caratères.",
            maxLength = CONTENT_MAX_LENGTH)
    public void setContent(final String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public String getFromAction() {
        return fromAction;
    }
}
