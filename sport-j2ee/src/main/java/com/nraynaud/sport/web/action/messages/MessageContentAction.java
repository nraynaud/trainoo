package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

public abstract class MessageContentAction extends ChainBackAction {
    public String content;
    public static final String CONTENT_MAX_LENGTH = "4000";

    public MessageContentAction(final Application application) {
        super(application);
    }

    @RequiredStringValidator(message = "Vous avez oublié le message.")
    @StringLengthFieldValidator(message = "Le message doit faire moins de ${maxLength} caratères.",
            maxLength = CONTENT_MAX_LENGTH)
    public void setContent(final String content) {
        this.content = content;
    }
}
