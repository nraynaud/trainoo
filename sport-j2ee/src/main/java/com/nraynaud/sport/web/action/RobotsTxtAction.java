package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

/**
 * BE CAREFUL THIS ACTION IS ACTUALLY CONFIGURED IN struts.xml !!!
 */
@Results({
        //the type avoids having the page decorated by application.jsp
    @Result(type = ServletDispatcherResult.class, value = "/WEB-INF/pages/robots.jsp")
        })
@Public
public class RobotsTxtAction extends DefaultAction {
    public RobotsTxtAction(final Application application) {
        super(application);
    }
}
