package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

@Result(name = Action.SUCCESS, value = "/WEB-INF/pages/about.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class AboutAction extends DefaultAction {
    public AboutAction(final Application application) {
        super(application);
    }
}
