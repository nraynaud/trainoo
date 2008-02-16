package com.nraynaud.sport.web.actionsupport;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.ChainBackCapable;

public class ChainBackAction extends DefaultAction implements ChainBackCapable {
    public String fromAction = "/|workouts?";

    public ChainBackAction(final Application application) {
        super(application);
    }

    public ActionDetail getFromAction() {
        return new ActionDetail(fromAction);
    }
}
