package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserAlreadyExistsException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.Public;
import static com.nraynaud.sport.web.action.LoginAction.openSession;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletActionRedirectResult;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;

@Results({
@Result(type = ServletActionRedirectResult.class,
        value = Constants.WORKOUTS_ACTION),
@Result(name = Action.INPUT, value = "/WEB-INF/signup.jsp")
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

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

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
    public String execute() {
        if (request.getMethod().equals("POST")) {
            try {
                final User user = application.createUser(login, password);
                openSession(user, request);
            } catch (UserAlreadyExistsException e) {
                addFieldError(Constants.LOGIN_RESULT, "Désolé, ce surnom est déjà pris !");
                return Action.INPUT;
            }
        }
        return Action.SUCCESS;
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }
}
