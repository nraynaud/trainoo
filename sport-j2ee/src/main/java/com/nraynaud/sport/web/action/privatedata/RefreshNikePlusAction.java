package com.nraynaud.sport.web.action.privatedata;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.importer.FailureException;
import com.nraynaud.sport.importer.Importer;
import com.nraynaud.sport.importer.WorkoutCollector;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.RedirectBack;
import static com.opensymphony.xwork2.Action.INPUT;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Results({
@Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION),
@Result(name = INPUT, type = ChainBack.class, value = "/WEB-INF/pages/groups/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class RefreshNikePlusAction extends ChainBackAction {
    private final Importer importer;

    public RefreshNikePlusAction(final Application application, final Importer importer) {
        super(application);
        this.importer = importer;
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        final String nikePlusEmail = getUser().getNikePluEmail().nonEscaped();
        final String nikePlusPassword = getUser().getNikePlusPassword().nonEscaped();
        return refresh(this, application, importer, nikePlusEmail, nikePlusPassword);
    }

    public static String refresh(final DefaultAction action, final Application application,
                                 final Importer importer,
                                 final String nikePlusEmail,
                                 final String nikePlusPassword) {
        if (nikePlusEmail != null && nikePlusEmail.length() > 0)
            try {
                importer.importWorkouts(nikePlusEmail, nikePlusPassword, new WorkoutCollector() {
                    List<Runnable> insertions = new ArrayList<Runnable>();

                    public void collectWorkout(final String nikePlusId, final String discipline, final Date date,
                                               final Double distance, final Long duration) {
                        insertions.add(new Runnable() {
                            public void run() {
                                application.createWorkout(date, action.getUser(), duration, distance, discipline,
                                        nikePlusId);
                            }
                        });
                    }

                    public void endCollection() {
                        application.execute(new Runnable() {
                            public void run() {
                                for (final Runnable insertion : insertions) {
                                    insertion.run();
                                }
                            }
                        });
                    }
                });
            } catch (FailureException e) {
                e.printStackTrace();
                action.addActionError("Une erreur s'est produite lors de l'accès au site nike+");
                return INPUT;
            }
        return SUCCESS;
    }
}
