package com.nraynaud.sport.web.action.groups;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/groups", "id", "${id}"}, value = ""),
@Result(name = INPUT, type = ChainBack.class, value = "/WEB-INF/pages/groups/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class CreateAction extends ChainBackAction {
    public String name;
    public Long id;

    public CreateAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        id = application.createGroup(getUser(), name, null).getId();
        return SUCCESS;
    }
}
