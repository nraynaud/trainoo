package com.nraynaud.sport.web.action.facebook;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;

public class ApplicationAddedAction extends DefaultAction {

    public ApplicationAddedAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        System.out.println("added");
        return SUCCESS;
    }
}
