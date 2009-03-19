package com.nraynaud.sport.web.action.facebook;

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
import org.apache.struts2.interceptor.validation.SkipValidation;

@Results({
    @Result(name = SUCCESS, type = ServletDispatcherResult.class, value = "/WEB-INF/pages/facebook/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class Action extends DefaultAction {

    public Action(final Application application) {
        super(application);
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        return SUCCESS;
    }

    @PostOnly
    public String create() {
        return index();
    }
}
