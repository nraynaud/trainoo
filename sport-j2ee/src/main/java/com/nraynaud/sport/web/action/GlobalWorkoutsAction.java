package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.Public;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

import java.util.List;

@Result(name = Action.SUCCESS, value = "/WEB-INF/pages/globalWorkouts.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class GlobalWorkoutsAction extends DefaultAction {

    private final Application application;

    public GlobalWorkoutsAction(final Application application) {
        this.application = application;
    }

    public List<Workout> getWorkouts() {
        return application.getWorkouts();
    }

    public Double getGlobalDistance() {
        return application.globalDistance();
    }

    public String index() {
        return Action.SUCCESS;
    }
}
