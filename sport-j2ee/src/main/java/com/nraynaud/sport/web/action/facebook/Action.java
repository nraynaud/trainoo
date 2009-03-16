package com.nraynaud.sport.web.action.facebook;

import com.google.code.facebookapi.FacebookException;
import com.google.code.facebookapi.FacebookWebappHelper;
import com.google.code.facebookapi.IFacebookRestClient;
import com.google.code.facebookapi.ProfileField;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.data.BibPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.LayoutResult;
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
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
    public static final String API_KEY = System.getenv("FACEBOOK_PUBLIC");
    private static final String SECRET_KEY = System.getenv("FACEBOOK_PRIVATE");
    private static final int TRAINOO_ACCOUNT_KEY = 1;

    public Action(final Application application) {
        super(application);
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        try {
            final FacebookWebappHelper<Document> helper = getHelper(request, response);
            final IFacebookRestClient<Document> restClient = helper.get_api_client();
            final long userID = restClient.users_getLoggedInUser();
            if (trainoo_account != null) {
                restClient.data_setUserPreference(TRAINOO_ACCOUNT_KEY,
                        trainoo_account.equals("0") ? null : trainoo_account);
            }
            trainoo_account = restClient.data_getUserPreference(TRAINOO_ACCOUNT_KEY);
            final Collection<Long> users = new ArrayList<Long>();
            users.add(userID);
            final EnumSet<ProfileField> fields = EnumSet.of(ProfileField.NAME);
            final Document d = restClient.users_getInfo(users, fields);
            name = d.getElementsByTagName("name").item(0).getTextContent();
            final StringWriter stringWriter = new StringWriter();
            LayoutResult.renderWithCharset("/WEB-INF/pages/facebook/profileView.jsp", stringWriter, response, request,
                    "UTF-8");
            restClient.profile_setFBML(null, stringWriter.toString(), null, null, null);
        } catch (FacebookException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        return SUCCESS;
    }

    public static FacebookWebappHelper<Document> getHelper(final HttpServletRequest request,
                                                           final HttpServletResponse response) {
        return FacebookWebappHelper.newInstanceXml(request, response, API_KEY, SECRET_KEY);
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
