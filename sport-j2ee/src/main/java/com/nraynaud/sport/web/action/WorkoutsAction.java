package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.data.StatisticsPageData;
import com.nraynaud.sport.data.UserPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
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
@Result(name = INPUT, value = "/WEB-INF/pages/personalWorkoutList.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class WorkoutsAction extends DefaultAction implements ModelDriven<StatisticsPageData> {
    private UserPageData data;
    public int workoutPage;

    public WorkoutsAction(final Application application) {
        super(application);
    }

    @SkipValidation
    public String index() {
        return SUCCESS;
    }

    public StatisticsPageData getModel() {
        if (data == null)
            data = application.fetchUserPageData(getUser(), workoutPage);
        return data;
    }
}
