package com.nraynaud.sport.web.action.bib;

import com.nraynaud.sport.User;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.UserStore;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.view.Helpers;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

@Result(name = SUCCESS, value = "/WEB-INF/pages/bib/view.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
public class Action extends DefaultAction implements ModelDriven<User> {
    private Long id;
    final private UserStore application;
    private User user;

    public Action(final UserStore application) {
        this.application = application;
    }

    public User getModel() {
        if (user == null) {
            final User currentUser = Helpers.currentUser();
            if (id == null || id.equals(currentUser.getId())) {
                user = currentUser;
            } else
                try {
                    user = application.fetchUser(id);
                } catch (UserNotFoundException e) {
                    throw new RuntimeException(e);
                }
        }
        return user;
    }


    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }
}
