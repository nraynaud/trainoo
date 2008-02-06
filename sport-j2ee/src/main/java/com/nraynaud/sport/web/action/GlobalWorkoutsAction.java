package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.data.StatisticsPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

@Result(name = Action.SUCCESS, value = "/WEB-INF/pages/globalWorkouts.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class GlobalWorkoutsAction extends DefaultAction implements ModelDriven<StatisticsPageData> {
    private StatisticsPageData statisticsPageData;
    public int workoutPage;
    public String discipline;

    public GlobalWorkoutsAction(final Application application) {
        super(application);
    }

    public StatisticsPageData getModel() {
        if (statisticsPageData == null)
            statisticsPageData = application.fetchFrontPageData(workoutPage, discipline);
        return statisticsPageData;
    }
}