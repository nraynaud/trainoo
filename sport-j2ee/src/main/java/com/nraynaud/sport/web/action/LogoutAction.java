package com.nraynaud.sport.web.action;

import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.Redirect;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.interceptor.ServletRequestAware;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Result(type = Redirect.class, value = "/")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class LogoutAction extends DefaultAction implements ServletRequestAware {
    private HttpServletRequest request;

    @PostOnly
    public String create() {
        if ("POST".equals(request.getMethod())) {
            final HttpSession session = request.getSession(false);
            if (session != null)
                session.invalidate();
        }
        return Action.SUCCESS;
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }
}
