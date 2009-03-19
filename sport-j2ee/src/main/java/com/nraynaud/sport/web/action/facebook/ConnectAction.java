package com.nraynaud.sport.web.action.facebook;

import com.google.code.facebookapi.IFacebookRestClient;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.FacebookUtil;
import com.nraynaud.sport.NameClashException;
import com.nraynaud.sport.User;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.SportSession;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.nraynaud.sport.web.result.Redirect;
import com.nraynaud.sport.web.result.RedirectBack;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Results({
    @Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION),
    @Result(name = "signup", type = Redirect.class, value = "signup", params = {"namespace", "/facebook"})
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class ConnectAction extends ChainBackAction implements ServletRequestAware, ServletResponseAware {
    private HttpServletRequest request;
    private HttpServletResponse response;

    public ConnectAction(final Application application) {
        super(application);
    }

    public String index() {
        final IFacebookRestClient<Document> client = FacebookUtil.getClient(request, response);
        final Long facebookId = client.getCacheUserId();
        if (facebookId != null)
            if (getUser() != null) {
                try {
                    application.facebookConnect(getUser(), facebookId);
                } catch (NameClashException e) {
                    closeCurrentSessionAndOpenNewOne(facebookId);
                }
            } else {
                if (!openSession(facebookId)) return "signup";
            }
        return SUCCESS;
    }

    private void closeCurrentSessionAndOpenNewOne(final Long facebookId) {
        final HttpSession session = request.getSession(false);
        session.invalidate();
        openSession(facebookId);
    }

    private boolean openSession(final Long facebookId) {
        final User user = application.facebookLogin(facebookId);
        if (user != null) {
            SportSession.openSession(user, request, false);
            return true;
        } else
            return false;
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }
}
