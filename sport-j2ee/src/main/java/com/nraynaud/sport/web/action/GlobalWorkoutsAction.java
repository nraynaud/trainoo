package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.FrontPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.Public;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

@Result(name = Action.SUCCESS, value = "/WEB-INF/pages/globalWorkouts.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class GlobalWorkoutsAction extends DefaultAction implements ModelDriven<FrontPageData> {
    private FrontPageData frontPageData;

    public GlobalWorkoutsAction(final Application application) {
        frontPageData = application.fetchFrontPageData();
    }

    public FrontPageData getModel() {
        return frontPageData;
    }
}