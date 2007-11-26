package com.nraynaud.sport.web.action;

import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

@SuppressWarnings({"MethodMayBeStatic"})
@Result(value = "/WEB-INF/pages/test.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class TestAction extends ActionSupport {
    public String index() {
        return SUCCESS;
    }
}
