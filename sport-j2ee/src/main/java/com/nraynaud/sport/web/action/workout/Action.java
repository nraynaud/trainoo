package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.data.WorkoutPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/workout/view.jsp"),
@Result(name = INPUT, value = "/WEB-INF/pages/workout/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class Action extends DefaultAction implements ModelDriven<WorkoutPageData> {
    private Long id;
    private WorkoutPageData data;
    private final Application application;
    public int workoutPage;

    public Action(final Application application) {
        this.application = application;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public WorkoutPageData getModel() {
        if (data == null)
            try {
                data = application.fetchWorkoutPageData(getUser(), id, workoutPage);
            } catch (WorkoutNotFoundException e) {
                throw new DataInputException(e);
            }
        return data;
    }
}