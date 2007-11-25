package com.nraynaud.sport.web.action;

import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.LayoutResult;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

@Result(type = LayoutResult.class, value = "/WEB-INF/pages/test.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class TestAction extends ActionSupport {
    public String index() {
        return SUCCESS;
    }
}
