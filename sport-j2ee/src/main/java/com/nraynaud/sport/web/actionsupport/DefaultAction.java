package com.nraynaud.sport.web.actionsupport;

import com.nraynaud.sport.User;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.SportRequest;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.config.Namespace;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;

@ParentPackage(Constants.STRUTS_PACKAGE)
@Namespace("/")
@Public
public class DefaultAction extends ActionSupport {
    private String actionDescription;

    protected SportRequest request;

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        return Action.SUCCESS;
    }

    protected static void pushValue(final Object data) {
        ActionContext.getContext().getValueStack().push(data);
    }

    public String getActionDescription() {
        return actionDescription;
    }

    public void setActionDescription(final String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public void setRequest(final SportRequest request) {
        this.request = request;
    }

    public User getUser() {
        return request.isLogged() ? request.getSportSession().getUser() : null;
    }
}