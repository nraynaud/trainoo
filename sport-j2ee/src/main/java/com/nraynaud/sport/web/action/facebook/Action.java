package com.nraynaud.sport.web.action.facebook;

import com.facebook.api.Facebook;
import com.facebook.api.FacebookException;
import com.facebook.api.FacebookRestClient;
import com.facebook.api.ProfileField;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.data.BibPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
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
public class Action extends DefaultAction implements ServletRequestAware, ServletResponseAware, ModelDriven<BibPageData> {
    private HttpServletRequest request;
    private HttpServletResponse response;
    public String name;
    public String auth_token;
    public String trainoo_account;
    private static final String API_KEY = "4d7b60f54176c2752cc66138c01105a7";
    private static final String SECRET_KEY = "cb3408a206dc084cec8107298a5a9faf";
    private static final int TRAINOO_ACCOUNT_KEY = 1;

    public Action(final Application application) {
        super(application);
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        try {
            final Facebook facebook = new Facebook(request, response, API_KEY, SECRET_KEY);
            facebook.requireLogin("");
            final FacebookRestClient restClient = facebook.get_api_client();
            final long userID = restClient.users_getLoggedInUser();
            if (trainoo_account != null) {
                restClient.data_setUserPreference(TRAINOO_ACCOUNT_KEY,
                        trainoo_account.equals("0") ? null : trainoo_account);
            }
            trainoo_account = restClient.data_getUserPreference(TRAINOO_ACCOUNT_KEY);
            final Collection<Long> users = new ArrayList<Long>();
            users.add(userID);
            final EnumSet<ProfileField> fields = EnumSet.of(com.facebook.api.ProfileField.NAME);
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

    public BibPageData getModel() {
        if (trainoo_account != null) {
            try {
                final User user = application.fetchUser(trainoo_account);
                return application.fetchBibPageData(null, user.getId(), 0, 0);
            } catch (UserNotFoundException e) {
                return null;
            }
        } else
            return null;
    }
}
