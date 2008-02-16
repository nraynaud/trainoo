package com.nraynaud.sport.web.action.groups;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = INPUT, value = "/WEB-INF/pages/groups/edit.jsp"),
@Result(name = SUCCESS, type = Redirect.class, value = "", params = {"namespace", "/groups", "id", "${id}"})
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class CreateAction extends DefaultAction {

    public static final String MAX_NAME_LENGTH = "20";
    public static final String MIN_NAME_LENGTH = "3";
    public String name;
    public Long id;

    public CreateAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        id = application.createGroup(getUser(), name, null).getId();
        return SUCCESS;
    }

    @StringLengthFieldValidator(message = "Le nom doit faire entre ${minLength} et ${maxLength} caratères.",
            maxLength = MAX_NAME_LENGTH, minLength = MIN_NAME_LENGTH)
    public void setName(final String name) {
        this.name = name;
    }
}
