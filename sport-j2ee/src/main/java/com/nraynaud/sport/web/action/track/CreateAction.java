package com.nraynaud.sport.web.action.track;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/track"}, value = "")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class CreateAction extends DefaultAction {
    public String track;
    public double length;

    public CreateAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        application.createTrack(getUser(), track, length);
        return SUCCESS;
    }
}
