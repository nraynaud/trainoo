package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.AccessDeniedException;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
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
public class EditAction extends MessageContentAction {
    public Long id;

    public EditAction(final Application application) {
        super(application);
    }

    public String create() {
        try {
            application.updateMessage(getUser(), id, content, messageKind);
            return SUCCESS;
        } catch (AccessDeniedException e) {
            addActionError("vous n'avez pas le droit de modifier ce message");
            return INPUT;
        }
    }
}
