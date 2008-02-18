package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.NameClashException;
import com.nraynaud.sport.web.*;
import com.nraynaud.sport.web.actionsupport.PasswordAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionChainResult;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.servlet.http.HttpServletRequest;

@Results({
@Result(type = ActionChainResult.class, params = {"namespace", "/", "method", "create"}, value = "login"),
@Result(name = Action.INPUT, value = "/WEB-INF/pages/signup.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class SignupAction extends PasswordAction implements ServletRequestAware, ChainBackCapable {
    public static final String LOGIN_MAX_LENGTH = "20";
    private static final String LOGIN_MIN_LENGTH = "4";
    public String login;
    private HttpServletRequest request;
    public boolean rememberMe = true;
    public String fromAction;
    public String onErrorAction;

    public SignupAction(final Application application) {
        super(application);
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

    @SuppressWarnings({"DuplicateStringLiteralInspection"})
    @PostOnly
    public String create() {
        if (request.getMethod().equals("POST")) {
            try {
                application.createUser(login, password);
            } catch (NameClashException e) {
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

    public ActionDetail getFromAction() {
        return new ActionDetail(fromAction);
    }

    public ActionDetail getOnErrorAction() {
        return new ActionDetail(onErrorAction);
    }
}
