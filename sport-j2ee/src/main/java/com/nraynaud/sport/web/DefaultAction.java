package com.nraynaud.sport.web;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.config.Namespace;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;


@ParentPackage(Constants.STRUTS_PACKAGE)
@Namespace("/")
public class DefaultAction extends ActionSupport {

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        return Action.INPUT;
    }
}
