package com.nraynaud.sport.web.action.statistics;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;

@Results({
    @Result(name = SUCCESS, value = "/WEB-INF/pages/statistics/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class Action extends DefaultAction {
    public Long id;
    public double totalDistance;

    public Action(final Application application) {
        super(application);
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        final User user = getUser();
        totalDistance = user == null ? 0.0 : application.fetchTotalDistance(user);
        return SUCCESS;
    }
}
