package com.nraynaud.sport.web.action.privatedata;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.mail.MailException;
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
public class ChangePasswordAction extends PasswordAction {
    public String oldPassword;

    public ChangePasswordAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        final boolean result;
        try {
            result = application.checkAndChangePassword(getUser(), oldPassword, password);
            if (result) {
                addActionMessage("Votre mot de passe a bien été mis à jour.");
                return SUCCESS;
            } else {
                addActionError("Votre ancien mot de passe est erronné.");
                return INPUT;
            }
        } catch (MailException e) {
            addActionError("Le mail de confirmation n'a pu être envoyé, votre mot de passe n'a PAS été changé.");
            return INPUT;
        }
    }
}
