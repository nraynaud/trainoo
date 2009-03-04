package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.AccessDeniedException;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.view.WorkoutPageDetails;
import com.nraynaud.sport.web.view.WorkoutView;
import static com.opensymphony.xwork2.Action.INPUT;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
    @Result(name = INPUT, value = "/WEB-INF/pages/workout/edit.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class EditAction extends DefaultAction implements ModelDriven<WorkoutPageDetails> {
    public Long id;
    public static final String PAGE_TILE = "Modification de mon entraînement";

    public static ActionDetail cancelAction(final String id) {
        return new ActionDetail("/workout", "", "id", id);
    }

    public EditAction(final Application application) {
        super(application);
    }

    public String index() {
        return INPUT;
    }

    public WorkoutPageDetails getModel() {
        try {
            final Workout workout = application.fetchWorkoutForEdition(id, getUser(), true);
            return new WorkoutPageDetails(
                    WorkoutView.createView(id.toString(),
                            workout.getDiscipline().nonEscaped(),
                            workout.getDate(),
                            workout.getDistance(),
                            workout.getDuration(),
                            workout.getEnergy(),
                            workout.getDebriefing() != null ? workout.getDebriefing().toString() : ""),
                    PAGE_TILE, new ActionDetail("/workout", "reallyEdit", "id", id.toString()),
                    cancelAction(id.toString()));
        } catch (WorkoutNotFoundException e) {
            throw new DataInputException(e);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }
}
