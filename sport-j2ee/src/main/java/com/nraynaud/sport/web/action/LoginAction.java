package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.web.*;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.RedirectBack;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Results({
@Result(name = Action.INPUT, value = "/WEB-INF/pages/login.jsp"),
@Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION)
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class LoginAction extends DefaultAction implements ServletRequestAware, ServletResponseAware, ChainBackCapable {

    private String login;
    private String password;
    private HttpServletRequest request;
    public boolean rememberMe = true;
    private HttpServletResponse response;
    public String fromAction;

    public LoginAction(final Application application) {
        super(application);
    }

    @RequiredFieldValidator(message = "Le surnom n'est pas renseigné.")
    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    @RequiredFieldValidator(message = "Le mot de passe n'est pas renseigné.")
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    @PostOnly
    public String create() {
        if (getActionErrors().isEmpty()) {
            final User user = application.authenticate(login, password, rememberMe);
            if (user == null) {
                addActionError("Votre surnom ou votre mot de passe sont invalides. Probablement une erreur de frappe.");
                return Action.INPUT;
            } else {
                if (rememberMe) {
                    final Cookie cookie = new Cookie(Constants.REMEMBER_COOKIE_NAME, user.getRememberToken());
                    cookie.setMaxAge(3600 * 24 * 365);
                    response.addCookie(cookie);
                }
                SportSession.openSession(user, request, rememberMe);
                return Action.SUCCESS;
            }
        } else
            return Action.INPUT;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        return Action.INPUT;
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }

    public String getFromAction() {
        return fromAction;
    }
}
