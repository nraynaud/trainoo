package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.AccessDeniedException;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
    @Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/"}, value = "workouts"),
    @Result(name = INPUT, type = Redirect.class, params = {"namespace", "/"}, value = "workouts")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class DeleteAction extends DefaultAction {
    public Long id;

    public DeleteAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        if (id != null) {
            try {
                application.deleteWorkout(id, getUser());
            } catch (AccessDeniedException e) {
                addActionError("Vous n'avez pas le droit de détruire cet entraînement");
                return INPUT;
            }
        }
        return SUCCESS;
    }
}
