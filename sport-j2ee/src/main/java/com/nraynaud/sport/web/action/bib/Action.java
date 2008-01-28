package com.nraynaud.sport.web.action.bib;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.data.BibPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.DefaultAction;
import static com.nraynaud.sport.web.action.bib.Action.INPUT;
import com.nraynaud.sport.web.view.Helpers;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = SUCCESS, value = "/WEB-INF/pages/bib/view.jsp"),
@Result(name = INPUT, value = "/WEB-INF/pages/bib/view.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class Action extends DefaultAction implements ModelDriven<BibPageData> {
    private Long id;
    final private Application application;
    private BibPageData data;

    public Action(final Application application) {
        this.application = application;
    }

    public BibPageData getModel() {
        if (data == null) {
            try {
                final User currentUser = Helpers.currentUser();
                final Long myId = id == null ? currentUser.getId() : id;
                data = application.fetchBibPageData(currentUser, myId);
            } catch (UserNotFoundException e) {
                throw new DataInputException(e);
            }
        }
        return data;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
