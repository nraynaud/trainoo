package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.Redirect;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Result(type = Redirect.class, params = {"namespace", "/"}, value = "")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class LogoutAction extends DefaultAction implements ServletRequestAware, ServletResponseAware {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private final Application application;

    public LogoutAction(final Application application) {
        this.application = application;
    }

    @PostOnly
    public String create() {
        if ("POST".equals(request.getMethod())) {
            final HttpSession session = request.getSession(false);
            if (session != null) {
                if (Boolean.TRUE.equals(session.getAttribute(Constants.REMEMBER_COOKIE_NAME)))
                    application.forgetMe(getUser());
                session.invalidate();
            }
            for (final Cookie cookie : request.getCookies()) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
        return Action.SUCCESS;
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }
}
