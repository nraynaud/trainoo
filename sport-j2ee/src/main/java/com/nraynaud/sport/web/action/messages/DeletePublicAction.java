package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.ChainBackCapable;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.RedirectBack;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION),
@Result(name = com.opensymphony.xwork2.Action.INPUT, type = ChainBack.class, value = "/WEB-INF/pages/messages.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class DeletePublicAction extends DefaultAction implements ChainBackCapable {
    public String fromAction;
    public Long id;

    public DeletePublicAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        application.deletePublicMessageFor(id, getUser());
        return SUCCESS;
    }

    public String getFromAction() {
        return fromAction;
    }
}
