package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.data.StatisticsPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.SportRequest;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;

@Conversion
@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/personalWorkoutList.jsp"),
@Result(name = INPUT, value = "/WEB-INF/pages/personalWorkoutList.jsp"),
@Result(name = "workouts-redirect", type = Redirect.class, value = "workouts")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class WorkoutsAction extends DefaultAction implements ModelDriven<StatisticsPageData> {
    private final Application application;
    private StatisticsPageData data;
    private SportRequest request;

    public WorkoutsAction(final Application application) {
        this.application = application;
    }

    @SkipValidation
    public String index() {
        return SUCCESS;
    }

    public StatisticsPageData getModel() {
        if (data == null)
            data = application.fetchUserPageData(getUser());
        return data;
    }

    public void setRequest(final SportRequest request) {
        this.request = request;
    }

    public User getUser() {
        return request.getSportSession().getUser();
    }
}
