package com.nraynaud.sport.web.action.statistics;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.data.StatisticsPageData;
import com.nraynaud.sport.web.Constants;
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
public class Action extends DefaultAction implements ModelDriven<StatisticsPageData> {
    public Action(final Application application) {
        super(application);
    }

    public StatisticsPageData getModel() {
        final User user = getUser();
        return user == null ? null : application.fetchStatisticsPageData(user);
    }
}
