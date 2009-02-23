package com.nraynaud.sport.web.action.workout;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.conversion.annotations.Conversion;
import com.opensymphony.xwork2.validator.annotations.Validation;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Conversion
@Results({
    @Result(name = SUCCESS, value = "/WEB-INF/pages/workout/new.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
@Validation
public class NewAction extends DefaultAction {

    public NewAction(final Application application) {
        super(application);
    }
}
