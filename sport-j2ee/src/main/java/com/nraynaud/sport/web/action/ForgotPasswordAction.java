package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.mail.MailException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.validator.annotations.EmailValidator;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/forgotPassword.jsp"),
@Result(name = INPUT, value = "/WEB-INF/pages/forgotPassword.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class ForgotPasswordAction extends DefaultAction {

    public String email;

    public ForgotPasswordAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        try {
            application.forgotPassword(email);
        } catch (UserNotFoundException e) {
            addActionError("Votre adresse e-mail n'a pas été trouvée, l'aviez-vous enregistrée dans votre compte ?");
            return INPUT;
        } catch (MailException e) {
            addActionError(
                    "Une erreur s'est produite lors de l'expédition de l'e-mail, votre mot de passe n'a PAS été modifié.");
            return INPUT;
        }
        addActionMessage("Un nouveau mot de passe vous a été attribué, il vous a été expédié par e-mail.");
        return SUCCESS;
    }

    @EmailValidator(message = "Votre email est invalide.")
    @RequiredStringValidator(message = "Votre email est invalide.")
    public String getEmail() {
        return email;
    }
}
