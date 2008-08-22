package com.nraynaud.sport.web.action.facebook;

import com.facebook.api.FacebookException;
import com.facebook.api.FacebookRestClient;
import com.facebook.api.ProfileField;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.w3c.dom.Document;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

@Results({
    @Result(name = SUCCESS, type = ServletDispatcherResult.class, value = "/WEB-INF/pages/facebook/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class Action extends DefaultAction implements ServletRequestAware, ServletResponseAware {
    private HttpServletRequest request;
    private HttpServletResponse response;
    public String name;
    public String auth_token;

    public Action(final Application application) {
        super(application);
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        final FacebookRestClient restClient = new FacebookRestClient("4d7b60f54176c2752cc66138c01105a7",
                "cb3408a206dc084cec8107298a5a9faf", "");
        try {
            restClient.auth_getSession(auth_token);
            final long userID = restClient.users_getLoggedInUser();
            final Collection<Long> users = new ArrayList<Long>();
            users.add(userID);
            final EnumSet<ProfileField> fields = EnumSet.of(
                    com.facebook.api.ProfileField.NAME,
                    com.facebook.api.ProfileField.PIC);
            final Document d = restClient.users_getInfo(users, fields);
            name = d.getElementsByTagName("name").item(0).getTextContent();
        } catch (FacebookException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return SUCCESS;
    }

    @PostOnly
    public String create() {
        return index();
    }

    public void setServletRequest(final HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(final HttpServletResponse response) {
        this.response = response;
    }
}
