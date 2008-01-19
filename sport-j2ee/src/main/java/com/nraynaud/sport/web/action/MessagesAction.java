package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DefaultAction;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.SportRequest;
import com.nraynaud.sport.web.result.Redirect;
import com.opensymphony.xwork2.Action;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

import java.util.Date;

@Results({
@Result(type = Redirect.class, value = Constants.WORKOUTS_ACTION),
@Result(name = Action.INPUT, value = "/WEB-INF/pages/personalWorkoutList.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class MessagesAction extends DefaultAction {

    String content;
    private final Application application;
    private SportRequest request;

    public MessagesAction(final Application application) {
        this.application = application;
    }

    public void setContent(final String content) {
        this.content = content;
    }

    @PostOnly
    public String create() {
        application.createMessage(getUser(), getUser().getId(), content, new Date());
        return SUCCESS;
    }

    public void setRequest(final SportRequest request) {
        this.request = request;
    }

    public User getUser() {
        return request.getSportSession().getUser();
    }
}
