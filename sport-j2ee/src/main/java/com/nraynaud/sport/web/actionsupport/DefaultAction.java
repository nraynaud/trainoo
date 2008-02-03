package com.nraynaud.sport.web.actionsupport;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.data.NewMessageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.SportRequest;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.config.Namespace;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.List;

@ParentPackage(Constants.STRUTS_PACKAGE)
@Namespace("/")
@Public
public class DefaultAction extends ActionSupport {
    public String actionDescription;
    protected SportRequest request;
    protected final Application application;
    private List<NewMessageData> newMessages;

    public DefaultAction(final Application application) {
        this.application = application;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        return Action.SUCCESS;
    }

    protected static void pushValue(final Object data) {
        ActionContext.getContext().getValueStack().push(data);
    }

    public void setRequest(final SportRequest request) {
        this.request = request;
    }

    public User getUser() {
        return request.isLogged() ? request.getSportSession().getUser() : null;
    }

    public void setActionDescription(final String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public List<NewMessageData> getNewMessages() {
        if (newMessages == null)
            newMessages = application.fetchNewMessagesCount(getUser());
        return newMessages;
    }
}
