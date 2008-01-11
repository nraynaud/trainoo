package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserAlreadyExistsException;
import com.nraynaud.sport.web.*;
import com.nraynaud.sport.web.result.Redirect;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.servlet.http.HttpServletRequest;

@Results({
@Result(type = Redirect.class, value = Constants.WORKOUTS_ACTION),
@Result(name = Action.INPUT, value = "/WEB-INF/pages/signup.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class SignupAction extends DefaultAction implements ServletRequestAware {

    private final Application application;

    private String login;
    private String password;
    private String passwordConfirmation;
    private HttpServletRequest request;

    public SignupAction(final Application application) {
        this.application = application;
    }

    @StringLengthFieldValidator(message = "Votre surnom doit comporter entre 4 et 15 caractères.",
            minLength = "4",
            maxLength = "15")
    @RequiredStringValidator(message = "Le surnom est obligatoire.")
    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    @StringLengthFieldValidator(message = "Votre mot de passe doit comporter entre 5 et 15 caractères.",
            minLength = "5",
            maxLength = "15")
    @RequiredStringValidator(message = "Le mot de passe est obligatoire.")
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(final String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public void validate() {
        if (request.getMethod().equals("POST") && (passwordConfirmation == null || !passwordConfirmation.equals(
                password)))
            addFieldError("passwordConfirmation", "Le mot de passe et la confirmation doivent être identiques.");
    }

    @SuppressWarnings({"DuplicateStringLiteralInspection"})
    @PostOnly
    public String create() {
        if (request.getMethod().equals("POST")) {
            try {
                final User user = application.createUser(login, password);
                SportSession.openSession(user, application, request);
            } catch (UserAlreadyExistsException e) {
                addFieldError(Constants.LOGIN_RESULT, "Désolé, ce surnom est déjà pris !");
                return Action.INPUT;
            }
        }
        return Action.SUCCESS;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        return Action.INPUT;
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }
}
