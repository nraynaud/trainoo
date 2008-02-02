package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserAlreadyExistsException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.SportSession;
import com.nraynaud.sport.web.actionsupport.PasswordAction;
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
public class SignupAction extends PasswordAction implements ServletRequestAware {

    private final Application application;

    private String login;
    private HttpServletRequest request;
    public static final String LOGIN_MAX_LENGTH = "20";
    private static final String LOGIN_MIN_LENGTH = "4";
    private boolean rememberMe = false;

    public SignupAction(final Application application) {
        this.application = application;
    }

    @StringLengthFieldValidator(message = "Votre surnom doit comporter entre "
            + LOGIN_MIN_LENGTH
            + " et "
            + LOGIN_MAX_LENGTH
            + " caractères.",
            minLength = LOGIN_MIN_LENGTH,
            maxLength = LOGIN_MAX_LENGTH)
    @RequiredStringValidator(message = "Le surnom est obligatoire.")
    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    @SuppressWarnings({"DuplicateStringLiteralInspection"})
    @PostOnly
    public String create() {
        if (request.getMethod().equals("POST")) {
            try {
                final User user = application.createUser(login, password);
                SportSession.openSession(user, request, rememberMe);
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

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(final boolean rememberMe) {
        this.rememberMe = rememberMe;
    }
}
