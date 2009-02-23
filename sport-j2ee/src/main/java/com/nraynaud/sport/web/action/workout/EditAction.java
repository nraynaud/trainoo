package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.*;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.AbstractWorkoutAction;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionChainResult;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import javax.servlet.http.HttpServletRequest;

@Conversion
@Results({
    @Result(name = INPUT, value = "/WEB-INF/pages/workout/edit.jsp"),
    @Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/workout", "id", "${id}"}, value = ""),
    @Result(name = "delete", type = ActionChainResult.class, value = "delete",
            params = {"namespace", "/workout", "method", "create"})
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class EditAction extends AbstractWorkoutAction implements ServletRequestAware, ModelDriven<Workout> {
    public Long id;
    private boolean delete;
    private Workout data;
    public int workoutPage;
    public int similarPage;
    public int publicMessagesPageIndex;
    public int privateMessagesPageIndex;

    public EditAction(final Application application) {
        super(application);
    }

    @SkipValidation
    public String index() {
        if (id != null) {
            final Workout workout;
            try {
                workout = data;
                application.checkEditionGrant(workout, getUser());
                setDate(workout.getDate());
                setDistance(workout.getDistance());
                setDuration(workout.getDuration());
                setDiscipline(workout.getDiscipline().nonEscaped());
                setEnergy(workout.getEnergy());
                final UserString comment = workout.getComment();
                setComment(comment != null ? comment.nonEscaped() : null);
                return INPUT;
            } catch (AccessDeniedException e) {
                addActionError("Vous n'avez pas le droit de modifier cet entraînement.");
                return INPUT;
            }
        } else
            return SUCCESS;
    }

    @PostOnly
    public String create() {
        if (id != null) {
            try {
                if (delete) return "delete";
                else
                    application.updateWorkout(id, getUser(), getDate(), getDuration(), getDistance(), getEnergy(),
                            getDiscipline(),
                            getComment());
                return SUCCESS;
            } catch (WorkoutNotFoundException e) {
                addActionError("l'entraînement désigné n'existe pas pour cet utilisateur");
                return INPUT;
            } catch (AccessDeniedException e) {
                addActionError("Vous n'avez pas le droit de modifier cet entraînement");
                return INPUT;
            }
        } else
            return SUCCESS;
    }

    public void setServletRequest(final HttpServletRequest request) {
        delete = request.getParameter("delete") != null;
    }

    public Workout getModel() {
        if (data == null)
            try {
                data = application.fetchWorkoutForEdition(id, getUser(), true);
            } catch (WorkoutNotFoundException e) {
                throw new DataInputException(e);
            } catch (AccessDeniedException e) {
                throw new RuntimeException(e);
            }
        return data;
    }
}
