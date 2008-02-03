package com.nraynaud.sport.web.action.bib;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
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
    static {
        ValidatorFactory.registerValidator("uri", "com.nraynaud.sport.web.URIValidator");
    }

    private String town;
    private String description;
    private String webSite;

    public static final String TOWN_MAX_LENGTH = "25";
    public static final String DESCRIPTION_MAX_LENGTH = "500";
    public static final String WEBSITE_MAX_LENGTH = "200";

    public EditAction(final Application application) {
        super(application);
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

    public String getTown() {
        return town;
    }

    @StringLengthFieldValidator(message = "La ville doit faire moins de ${maxLength} caratères.",
            maxLength = TOWN_MAX_LENGTH)
    public void setTown(final String town) {
        this.town = nullIfEmpty(town);
    }

    private static String nullIfEmpty(final String string) {
        return string.length() > 0 ? string : null;
    }

    public String getDescription() {
        return description;
    }

    @StringLengthFieldValidator(message = "Votre description doit faire moins de ${maxLength} caratères.",
            maxLength = DESCRIPTION_MAX_LENGTH)
    public void setDescription(final String description) {
        this.description = nullIfEmpty(description);
    }

    public String getWebSite() {
        return webSite;
    }

    @StringLengthFieldValidator(message = "L'adresse de votre site web doit faire moins de ${maxLength} caratères.",
            maxLength = WEBSITE_MAX_LENGTH)
    @CustomValidator(message = "Le site web doit être une adresse web (URL)", type = "uri")
    public void setWebSite(final String webSite) {
        this.webSite = nullIfEmpty(webSite);
    }
}
