package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.data.GlobalWorkoutsPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Result(name = Action.SUCCESS, value = "/WEB-INF/pages/globalWorkouts.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class GlobalWorkoutsAction extends DefaultAction implements ModelDriven<GlobalWorkoutsPageData> {
    private GlobalWorkoutsPageData statisticsData;
    public int workoutPage;
    public String disciplineFilter;

    public GlobalWorkoutsAction(final Application application) {
        super(application);
    }

    public GlobalWorkoutsPageData getModel() {
        if (statisticsData == null) {
            final Collection<String> disciplines;
            if (disciplineFilter == null)
                disciplines = Collections.emptyList();
            else
                disciplines = Arrays.asList(disciplineFilter.split(","));
            statisticsData = application.fetchFrontPageData(workoutPage, 20, disciplines);
        }
        return statisticsData;
    }
}