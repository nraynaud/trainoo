package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.data.WorkoutPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.Public;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.Namespace;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

@Result(name = SUCCESS, value = "/WEB-INF/pages/workout/view.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Namespace("/workout")
@Public
public class Action extends DefaultAction implements ModelDriven<WorkoutPageData> {
    private Long id;
    private WorkoutPageData data;
    private final Application application;

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
                data = application.fetchWorkoutPageData(id);
            } catch (WorkoutNotFoundException e) {
                throw new DataInputException(e);
            }
        return data;
    }
}
