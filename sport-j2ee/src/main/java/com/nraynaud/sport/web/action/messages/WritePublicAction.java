package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.Topic;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.RedirectBack;
import static com.opensymphony.xwork2.Action.INPUT;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
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
public class WritePublicAction extends ChainBackAction {
    public String content;

    public Long aboutId;

    public Topic.Kind topicKind;

    public static final String CONTENT_MAX_LENGTH = "4000";

    public WritePublicAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        try {
            application.createPublicMessage(getUser(), content, new Date(), aboutId, topicKind);
        } catch (WorkoutNotFoundException e) {
            throw new DataInputException(e);
        }
        return SUCCESS;
    }

    @TypeConversion(converter = "com.opensymphony.xwork2.util.EnumTypeConverter")
    public void setTopicKind(final Topic.Kind topicKind) {
        this.topicKind = topicKind;
    }

    @RequiredStringValidator(message = "Vous avez oublié le message.")
    @StringLengthFieldValidator(message = "Le message doit faire moins de ${maxLength} caratères.",
            maxLength = CONTENT_MAX_LENGTH)
    public void setContent(final String content) {
        this.content = content;
    }
}
