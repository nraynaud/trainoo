package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.AccessDeniedException;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.view.WorkoutEditPageDetails;
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

    public WorkoutEditPageDetails getModel() {
        try {
            final Workout workout = application.fetchWorkout(id, getUser(), true);
            final String trackId = workout.getTrack() != null ? workout.getTrack().getId().toString() : "";
            return new WorkoutEditPageDetails(
                    WorkoutView.createView(id.toString(),
                            workout.getDiscipline().nonEscaped(),
                            workout.getDate(),
                            workout.getDistance(),
                            workout.getDuration(),
                            workout.getEnergy(),
                            workout.getDebriefing() != null ? workout.getDebriefing().toString() : "",
                            trackId),
                    PAGE_TILE, new ActionDetail("/workout", "reallyEdit", "id", id.toString()),
                    cancelAction(id.toString()), application.fetchTracks(getUser()));
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
    }
}
