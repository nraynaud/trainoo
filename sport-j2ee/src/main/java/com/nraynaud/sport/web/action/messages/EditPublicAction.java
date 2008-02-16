package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.AccessDeniedException;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.RedirectBack;
import static com.opensymphony.xwork2.Action.INPUT;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION),
@Result(name = INPUT, type = ChainBack.class, value = "/WEB-INF/pages/messages.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class EditPublicAction extends ChainBackAction {
    public String content;
    public Long id;

    public EditPublicAction(final Application application) {
        super(application);
    }

    public String create() {
        try {
            application.updatePublicMessage(getUser(), id, content);
            return SUCCESS;
        } catch (AccessDeniedException e) {
            addActionError("vous n'avez pas le droit de modifier ce message");
            return INPUT;
        }
    }
}
