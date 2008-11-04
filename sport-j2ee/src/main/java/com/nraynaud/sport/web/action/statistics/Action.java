package com.nraynaud.sport.web.action.statistics;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.data.StatisticsPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
    @Result(name = SUCCESS, value = "/WEB-INF/pages/statistics/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class Action extends DefaultAction implements ModelDriven<StatisticsPageData> {

    public Long id;
    public String discipline;

    public Action(final Application application) {
        super(application);
    }

    public StatisticsPageData getModel() {
        try {
            return application.fetchStatisticsPageData(id == null ? getUser().getId() : id, discipline);
        } catch (UserNotFoundException e) {
            addActionError("Le numéro d'utilisateur demandé n'existe pas.");
            throw new RuntimeException(e);
        }
    }
}
