package com.nraynaud.sport.web.action.privatedata;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.importer.Importer;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.INPUT;
import static com.opensymphony.xwork2.Action.SUCCESS;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/privateData/edit.jsp"),
@Result(name = INPUT, value = "/WEB-INF/pages/privateData/edit.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class ChangeNikePlusAction extends DefaultAction {
    public String nikePlusEmail;
    public String nikePlusPassword;
    private final Importer importer;

    public ChangeNikePlusAction(final Application application, final Importer importer) {
        super(application);
        this.importer = importer;
    }

    @PostOnly
    public String create() {
        application.updateNikePlusData(getUser(), nikePlusEmail, nikePlusPassword);
        final String result = RefreshNikePlusAction.refresh(this, application, importer, nikePlusEmail,
                nikePlusPassword);
        if (SUCCESS.equals(result))
            addActionMessage("vos paramètres Nike+ ont bien été mis à jour et vos entraînements importés.");
        return result;
    }
}
