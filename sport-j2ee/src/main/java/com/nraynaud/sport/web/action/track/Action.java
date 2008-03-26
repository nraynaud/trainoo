package com.nraynaud.sport.web.action.track;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

import java.util.List;

@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/track/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class Action extends DefaultAction {
    public Action(final Application application) {
        super(application);
    }

    public List<String> getTracks() {
        return application.fetchTracks(getUser());
    }
}
