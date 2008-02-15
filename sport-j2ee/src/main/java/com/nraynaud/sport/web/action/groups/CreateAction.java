package com.nraynaud.sport.web.action.groups;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = INPUT, value = "/WEB-INF/pages/groups/edit.jsp"),
@Result(name = SUCCESS, type = Redirect.class, value = "", params = {"namespace", "/groups", "id", "${id}"})
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class CreateAction extends GroupAction {

    public CreateAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        id = application.createGroup(getUser(), name, null).getId();
        return SUCCESS;
    }
}
