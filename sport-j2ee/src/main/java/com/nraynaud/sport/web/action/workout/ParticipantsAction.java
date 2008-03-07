package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.*;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionChainResult;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

import java.util.ArrayList;
import java.util.Collection;

@Conversion
@Results({
@Result(name = INPUT, value = "/WEB-INF/pages/workout/editParticipants.jsp"),
@Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/workout", "id", "${id}"}, value = ""),
@Result(name = "delete", type = ActionChainResult.class, value = "delete",
        params = {"namespace", "/workout", "method", "create"})
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class ParticipantsAction extends DefaultAction {
    public Long id;
    public String[] participants;
    public Collection<String> allUsers;
    public Workout workout;

    public ParticipantsAction(final Application application) {
        super(application);
    }

    public String index() {
        initData();
        return INPUT;
    }

    @PostOnly
    public String create() {
        initData();
        try {
            application.setWorkoutParticipants(getUser(), id, participants);
            return SUCCESS;
        } catch (AccessDeniedException e) {
            addActionError("Seul le propriétaire de l'entraînement peut en modifer les participants.");
            return INPUT;
        }
    }

    private void initData() {
        allUsers = application.fechLoginBeginningBy("");
        try {
            workout = application.fetchWorkout(id);
            final ArrayList<String> names = new ArrayList<String>(workout.getParticipants().size());
            for (final User participant : workout.getParticipants()) {
                names.add(participant.getName().nonEscaped());
            }
            participants = names.toArray(new String[names.size()]);
        } catch (WorkoutNotFoundException e) {
            addActionError("L'entraînement " + id + " n'existe pas.");
        }
    }
}
