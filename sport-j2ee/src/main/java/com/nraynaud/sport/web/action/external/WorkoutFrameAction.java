package com.nraynaud.sport.web.action.external;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

@Public
@Result(type = ServletDispatcherResult.class, value = "/WEB-INF/pages/external/workoutFrame.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
public class WorkoutFrameAction extends DefaultAction implements ModelDriven<Workout> {
    public Long id;

    public WorkoutFrameAction(final Application application) {
        super(application);
    }

    public Workout getModel() {
        return application.fetchWorkout(id);
    }
}
