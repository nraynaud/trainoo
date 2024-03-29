package com.nraynaud.sport.web.action.bib;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.data.BibPageData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.SportRequest;
import static com.nraynaud.sport.web.action.bib.Action.INPUT;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
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
@Public
public class Action extends DefaultAction implements ModelDriven<BibPageData> {
    private Long id;
    public int workoutPage = 0;
    public int messagePageIndex;

    public Action(final Application application) {
        super(application);
    }

    public BibPageData getModel() {
        try {
            final User currentUser = currentUser();
            final Long myId = id == null ? currentUser.getId() : id;
            return application.fetchBibPageData(currentUser, myId, workoutPage, messagePageIndex);
        } catch (UserNotFoundException e) {
            throw new DataInputException(e);
        }
    }

    private static User currentUser() {
        final SportRequest request = SportRequest.getSportRequest();
        return request.isLogged() ? request.getSportSession().getUser() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
