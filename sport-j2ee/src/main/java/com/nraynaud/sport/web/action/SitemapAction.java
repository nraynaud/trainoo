package com.nraynaud.sport.web.action;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.data.SitemapData;
import com.nraynaud.sport.web.Public;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.opensymphony.xwork2.ModelDriven;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;
import org.apache.struts2.dispatcher.ServletDispatcherResult;

@Results({
        //the type avoids having the page decorated by application.jsp
    @Result(type = ServletDispatcherResult.class, value = "/WEB-INF/pages/sitemap.jsp")
        })
@Public
public class SitemapAction extends DefaultAction implements ModelDriven<SitemapData> {
    public SitemapAction(final Application application) {
        super(application);
    }

    public SitemapData getModel() {
        return application.fetchSitemapData();
    }
}
