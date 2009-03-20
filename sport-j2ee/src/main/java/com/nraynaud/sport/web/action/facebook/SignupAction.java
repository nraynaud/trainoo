package com.nraynaud.sport.web.action.facebook;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.IFacebookRestClient;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.FacebookUtil;
import com.nraynaud.sport.NameClashException;
import com.nraynaud.sport.User;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.SportSession;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.nraynaud.sport.web.result.RedirectBack;
import com.opensymphony.xwork2.Action;
import static com.opensymphony.xwork2.Action.INPUT;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Results({
    @Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION),
    @Result(name = INPUT, value = "/WEB-INF/pages/facebook/signup.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class SignupAction extends ChainBackAction implements ServletRequestAware, ServletResponseAware {
    public static final String LOGIN_MAX_LENGTH = "20";
    public static final String LOGIN_MIN_LENGTH = "4";
    public String login;
    private HttpServletRequest request;
    private HttpServletResponse response;

    public SignupAction(final Application application) {
        super(application);
    }

    @SuppressWarnings({"DuplicateStringLiteralInspection"})
    @PostOnly
    public String create() {
        if (request.getMethod().equals("POST")) {
            try {
                final Long facebookId = getFacebookId();
                final User user = application.createFacebookUser(login, facebookId);
                SportSession.openSession(user, request, false);
            } catch (NameClashException e) {
                addFieldError(Constants.LOGIN_RESULT, "Désolé, ce surnom est déjà pris !");
                return com.opensymphony.xwork2.Action.INPUT;
            }
        }
        return Action.SUCCESS;
    }

    private Long getFacebookId() {
        try {
            return getFacebook().users_getLoggedInUser();
        } catch (FacebookException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        final IFacebookRestClient<Document> facebook = getFacebook();
        final Long facebookId = getFacebookId();
        if (facebookId != null) {
            login = FacebookUtil.getInfo(facebook, "name");
        }
        return INPUT;
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }

    private IFacebookRestClient<Document> getFacebook() {
        return FacebookUtil.getClient(request, response);
    }
}
