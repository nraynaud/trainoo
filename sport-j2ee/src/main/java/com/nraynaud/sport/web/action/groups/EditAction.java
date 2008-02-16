package com.nraynaud.sport.web.action.groups;

import com.nraynaud.sport.*;
import static com.nraynaud.sport.Helper.nonEscaped;
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
import org.apache.struts2.interceptor.validation.SkipValidation;

@Results({
@Result(name = "precondError", type = Redirect.class, value = "", params = {"namespace", "/groups", "id", "${id}"}),
@Result(name = INPUT, value = "/WEB-INF/pages/groups/edit.jsp"),
@Result(name = SUCCESS, type = Redirect.class, value = "", params = {"namespace", "/groups", "id", "${id}"})
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class EditAction extends DefaultAction {

    public static final String MAX_NAME_LENGTH = "20";
    public static final String MIN_NAME_LENGTH = "3";
    public String name;
    public Long id;
    public static final String MAX_DESCRIPTION_LENGTH = "4000";

    public String description;

    public EditAction(final Application application) {
        super(application);
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        try {
            final Group group = application.fetchGroupForUpdate(getUser(), id);
            description = nonEscaped(group.getDescription());
            name = nonEscaped(group.getName());
            id = group.getId();
        } catch (GroupNotFoundException e) {
            return "precondError";
        } catch (AccessDeniedException e) {
            return "precondError";
        }
        return INPUT;
    }

    @PostOnly
    public String create() {
        try {
            try {
                application.updateGroup(getUser(), id, name,
                        description != null && description.length() > 0 ? description : null);
            } catch (NameClashException e) {
                addActionError("Ce nom de groupe existe déjà.");
                return INPUT;
            }
        } catch (GroupNotFoundException e) {
            addActionError("Le groupe n'existe pas.");
            return INPUT;
        } catch (AccessDeniedException e) {
            addActionError("Vous n'avez pas le droit d'effectuer cette modification.");
            return INPUT;
        }
        return SUCCESS;
    }

    @StringLengthFieldValidator(message = "La description doit faire moins de ${maxLength} caratères.",
            maxLength = MAX_DESCRIPTION_LENGTH)
    public void setDescription(final String description) {
        this.description = description;
    }

    @StringLengthFieldValidator(message = "Le nom doit faire entre ${minLength} et ${maxLength} caratères.",
            maxLength = MAX_NAME_LENGTH, minLength = MIN_NAME_LENGTH)
    public void setName(final String name) {
        this.name = name;
    }
}
