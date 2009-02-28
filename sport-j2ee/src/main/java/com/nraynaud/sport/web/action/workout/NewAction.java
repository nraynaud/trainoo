package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.view.WorkoutPageDetails;
import com.nraynaud.sport.web.view.WorkoutView;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
    @Result(name = SUCCESS, value = "/WEB-INF/pages/workout/edit.jsp")
        })
public class NewAction extends DefaultAction implements ModelDriven<WorkoutPageDetails> {
    public static final String PAGE_TILE = "Nouvel Entraîment";
    public static final ActionDetail CANCEL_ACTION = new ActionDetail("/workouts", "");

    public NewAction(final Application application) {
        super(application);
    }

    public WorkoutPageDetails getModel() {
        return new WorkoutPageDetails(
                new WorkoutView(null, "", "Aujourd'hui", "", "", "", ""), PAGE_TILE,
                new ActionDetail("/workout", "create"), CANCEL_ACTION);
    }
}
