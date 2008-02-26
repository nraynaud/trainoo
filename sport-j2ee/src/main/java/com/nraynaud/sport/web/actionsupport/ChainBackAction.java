package com.nraynaud.sport.web.actionsupport;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.ChainBackCapable;

public class ChainBackAction extends DefaultAction implements ChainBackCapable {
    public String fromAction = "/|workouts?";
    public String onErrorAction;

    public ChainBackAction(final Application application) {
        super(application);
    }

    public ActionDetail getFromAction() {
        return ActionDetail.decodeActionDetail(fromAction);
    }

    public ActionDetail getOnErrorAction() {
        return ActionDetail.decodeActionDetail(onErrorAction);
    }
}
