package com.nraynaud.sport.web.action.privatedata;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/privateData/edit.jsp"),
@Result(name = INPUT, value = "/WEB-INF/pages/privateData/edit.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class ChangeEmailAction extends DefaultAction {
    public String email;

    public ChangeEmailAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        application.updateEmail(getUser(), email);
        return SUCCESS;
    }
}
