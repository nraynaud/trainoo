package com.nraynaud.sport.web.action.groups;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.NameClashException;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.ChainBackAction;
import com.nraynaud.sport.web.result.ChainBack;
import com.nraynaud.sport.web.result.RedirectBack;
import static com.opensymphony.xwork2.Action.INPUT;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(type = RedirectBack.class, value = Constants.WORKOUTS_ACTION),
@Result(name = INPUT, type = ChainBack.class, value = "/WEB-INF/pages/groups/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class CreateAction extends ChainBackAction {

    public static final String MAX_NAME_LENGTH = "20";
    public static final String MIN_NAME_LENGTH = "3";
    public String name;
    public Long id;

    public CreateAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        try {
            id = application.createGroup(getUser(), name, null).getId();
        } catch (NameClashException e) {
            addActionError("Désolé un groupe avec le même nom existe déjà");
            return INPUT;
        }
        return SUCCESS;
    }

    @StringLengthFieldValidator(message = "Le nom doit faire entre ${minLength} et ${maxLength} caratères.",
            maxLength = MAX_NAME_LENGTH, minLength = MIN_NAME_LENGTH)
    public void setName(final String name) {
        this.name = name;
    }
}
