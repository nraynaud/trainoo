package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.result.Redirect;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.validator.annotations.RequiredFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Results({
@Result(name = Action.INPUT, value = "/WEB-INF/pages/login.jsp"),
@Result(type = Redirect.class, value = Constants.WORKOUTS_ACTION)
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class LoginAction extends DefaultAction implements ServletRequestAware {
    private final Application application;

    private String login;
    private String password;
    private HttpServletRequest request;

    public LoginAction(final Application application) {
        this.application = application;
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
            final User user = application.authenticate(login, password);
            if (user == null) {
                addActionError("Votre surnom ou votre mot de passe sont invalides. Probablement une erreur de frappe.");
                return Action.INPUT;
            } else {
                openSession(user, request);
                return Action.SUCCESS;
            }
        } else
            return Action.INPUT;
    }

    public static void openSession(final User user, final HttpServletRequest request) {
        final HttpSession session = request.getSession(true);
        session.setMaxInactiveInterval(30 * 24 * 3600);
        session.setAttribute(Constants.USER_KEY, user);
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }
}
