package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

@Result(value = "/WEB-INF/pages/about.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class AboutAction extends DefaultAction {
    public AboutAction(final Application application) {
        super(application);
    }
}
