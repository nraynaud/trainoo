package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.interceptor.ServletResponseAware;

import javax.servlet.http.HttpServletResponse;

@Result(value = "/WEB-INF/pages/workoutError.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
@Public
public class ErrorAction extends DefaultAction implements ServletResponseAware {

    public ErrorAction(final Application application) {
        super(application);
    }

    public void setServletResponse(final HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }
}
