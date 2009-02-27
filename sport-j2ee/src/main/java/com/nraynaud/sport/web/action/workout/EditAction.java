package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.AccessDeniedException;
import com.nraynaud.sport.Application;
import com.nraynaud.sport.Workout;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.view.WorkoutView;
import static com.opensymphony.xwork2.Action.INPUT;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Conversion
@Results({
    @Result(name = INPUT, value = "/WEB-INF/pages/workout/edit.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class EditAction extends DefaultAction implements ModelDriven<WorkoutView> {
    public Long id;
    private WorkoutView data;

    public EditAction(final Application application) {
        super(application);
    }

    public String index() {
        return INPUT;
    }

    public WorkoutView getModel() {
        if (data == null)
            try {
                final Workout workout = application.fetchWorkoutForEdition(id, getUser(), true);
                data = WorkoutView.createView(id.toString(),
                        workout.getDiscipline().nonEscaped(),
                        workout.getDate(),
                        workout.getDistance(),
                        workout.getDuration(),
                        workout.getEnergy(),
                        workout.getComment() != null ? workout.getComment().toString() : "");
            } catch (WorkoutNotFoundException e) {
                throw new DataInputException(e);
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        return data;
    }
}
