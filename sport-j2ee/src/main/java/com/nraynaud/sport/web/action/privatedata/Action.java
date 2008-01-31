package com.nraynaud.sport.web.action.privatedata;

import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/privateData/view.jsp"),
@Result(name = INPUT, value = "/WEB-INF/pages/privateData/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class Action extends DefaultAction {
}
