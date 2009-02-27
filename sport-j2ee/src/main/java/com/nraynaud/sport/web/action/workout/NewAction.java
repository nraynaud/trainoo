package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.view.WorkoutView;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
    @Result(name = SUCCESS, value = "/WEB-INF/pages/workout/new.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class NewAction extends DefaultAction implements ModelDriven<WorkoutView> {

    public NewAction(final Application application) {
        super(application);
    }

    public WorkoutView getModel() {
        return new WorkoutView(null, "", "Aujourd'hui", "", "", "", "");
    }
}
