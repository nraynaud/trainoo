package com.nraynaud.sport.web.action.groups;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;

public abstract class GroupAction extends ChainBackAction {
    public static final String MAX_NAME_LENGTH = "20";
    public static final String MIN_NAME_LENGTH = "3";
    public String name;
    public Long id;

    public GroupAction(final Application application) {
        super(application);
    }

    @StringLengthFieldValidator(message = "Le nom doit faire moins de ${maxLength} caratères.",
            maxLength = MAX_NAME_LENGTH, minLength = MIN_NAME_LENGTH)
    public void setName(final String name) {
        this.name = name;
    }
}
