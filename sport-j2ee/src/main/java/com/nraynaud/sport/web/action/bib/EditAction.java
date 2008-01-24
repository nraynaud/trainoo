package com.nraynaud.sport.web.action.bib;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.SportRequest;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.validator.ValidatorFactory;
import com.opensymphony.xwork2.validator.annotations.CustomValidator;
import com.opensymphony.xwork2.validator.annotations.StringLengthFieldValidator;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.interceptor.validation.SkipValidation;

@Results({
@Result(name = INPUT, value = "/WEB-INF/pages/bib/edit.jsp"),
@Result(name = SUCCESS, type = Redirect.class, value = "")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class EditAction extends DefaultAction {
    private String town;
    private String description;
    private String webSite;
    private final Application application;

    public EditAction(final Application application) {
        this.application = application;
        ValidatorFactory.registerValidator("uri", "com.nraynaud.sport.web.URIValidator");
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        final User user = getUser();
        town = user.getTown();
        description = user.getDescription();
        webSite = user.getWebSite();
        return INPUT;
    }

    public String create() {
        application.updateBib(getUser(), town, description, webSite);
        return SUCCESS;
    }

    private static User getUser() {
        return SportRequest.getSportRequest().getSportSession().getUser();
    }

    public String getTown() {
        return town;
    }

    @StringLengthFieldValidator(message = "La ville doit faire moins de ${maxLength} caratères.", maxLength = "25")
    public void setTown(final String town) {
        this.town = town;
    }

    public String getDescription() {
        return description;
    }

    @StringLengthFieldValidator(message = "Votre description doit faire moins de ${maxLength} caratères.",
            maxLength = "200")
    public void setDescription(final String description) {
        this.description = description;
    }

    public String getWebSite() {
        return webSite;
    }

    @StringLengthFieldValidator(message = "L'adresse de votre site web doit faire moins de ${maxLength} caratères.",
            maxLength = "200")
    @CustomValidator(message = "Le site web doit être une adresse web (URL)", type = "uri")
    public void setWebSite(final String webSite) {
        this.webSite = webSite;
    }
}
