package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.MessageKind;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.opensymphony.xwork2.conversion.annotations.TypeConversion;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

public abstract class MessageContentAction extends ChainBackAction {
    public String content;

    public MessageKind messageKind;
    public static final String CONTENT_MAX_LENGTH = "4000";

    public MessageContentAction(final Application application) {
        super(application);
    }

    @RequiredStringValidator(message = "Le corps du message est vide.")
    @StringLengthFieldValidator(message = "Le message doit faire moins de ${maxLength} caratères.",
            maxLength = CONTENT_MAX_LENGTH)
    public void setContent(final String content) {
        this.content = content;
    }

    @TypeConversion(converter = "com.nraynaud.sport.web.converter.EnumTypeConverter")
    public void setMessageKind(final MessageKind messageKind) {
        this.messageKind = messageKind;
    }
}
