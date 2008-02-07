package com.nraynaud.sport.web.action.groups;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.RedirectBack;
import static com.opensymphony.xwork2.Action.INPUT;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION),
@Result(name = INPUT, type = ChainBack.class, value = "/WEB-INF/pages/group.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class JoinAction extends ChainBackAction {

    public Long groupId;

    public JoinAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        application.joinGroup(getUser(), groupId);
        return SUCCESS;
    }
}
