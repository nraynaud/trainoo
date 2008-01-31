package com.nraynaud.sport.web.action.privatedata;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.PasswordAction;
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
public class Action extends PasswordAction {
    private String oldPassword;

    private final Application application;

    public Action(final Application application) {
        this.application = application;
    }

    @PostOnly
    public String create() {
        final boolean result = application.checkAndChangePassword(getUser(), oldPassword, password);
        if (result) {
            addActionMessage("Votre mot de passe a bien été mis à jour.");
            return SUCCESS;
        } else {
            addActionError("Votre ancien mot de passe est erronné.");
            return INPUT;
        }
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(final String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
