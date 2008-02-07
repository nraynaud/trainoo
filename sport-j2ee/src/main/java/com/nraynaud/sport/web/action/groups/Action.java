package com.nraynaud.sport.web.action.groups;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.data.GroupData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

import java.util.Collection;

@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/groups/view.jsp"),
@Result(name = INPUT, value = "/WEB-INF/pages/groups/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class Action extends DefaultAction implements ModelDriven<Collection<GroupData>> {
    public Long id;

    public Action(final Application application) {
        super(application);
    }

    public Collection<GroupData> getModel() {
        return application.fetchGroups(getUser());
    }
}
