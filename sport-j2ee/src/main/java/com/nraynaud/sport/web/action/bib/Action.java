package com.nraynaud.sport.web.action.bib;

import com.nraynaud.sport.User;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.SportRequest;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;

@Result(name = SUCCESS, value = "/WEB-INF/pages/bib/view.jsp")
@ParentPackage(Constants.STRUTS_PACKAGE)
public class Action extends DefaultAction implements ModelDriven<User> {

    public User getModel() {
        return SportRequest.getSportRequest().getSportSession().getUser();
    }
}
