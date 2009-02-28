package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.AccessDeniedException;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Conversion
@Results({
    @Result(name = INPUT, value = "/WEB-INF/pages/workout/edit.jsp"),
    @Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/workout", "id", "${id}"}, value = "")
        })
@Validation
public class ReallyEditAction extends AbstractWorkoutAction {

    public ReallyEditAction(final Application application) {
        super(application, EditAction.PAGE_TILE);
    }

    @PostOnly
    public String create() {
        try {
            application.updateWorkout(Long.valueOf(id), getUser(), getDate(), getDuration(), getDistance(),
                    getEnergy(),
                    getDiscipline(), getComment());
            return SUCCESS;
        } catch (WorkoutNotFoundException e) {
            addActionError("l'entraînement désigné n'existe pas pour cet utilisateur");
            return INPUT;
        } catch (AccessDeniedException e) {
            addActionError("Vous n'avez pas le droit de modifier cet entraînement");
            return INPUT;
        }
    }

    protected ActionDetail cancelAction() {
        return EditAction.cancelAction(id);
    }
}
