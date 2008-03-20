package com.nraynaud.sport.web.actionsupport;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.User;
import com.nraynaud.sport.data.NewMessageData;
import com.nraynaud.sport.web.*;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import org.apache.struts2.config.Namespace;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.interceptor.validation.SkipValidation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

@ParentPackage(Constants.STRUTS_PACKAGE)
@Namespace("/")
@Public
public class DefaultAction extends ActionSupport {
    public ActionDetail actionDescription;
    protected SportRequest request;
    protected final Application application;
    private List<NewMessageData> newMessages;

    public DefaultAction(final Application application) {
        this.application = (Application) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{Application.class}, new InvocationHandler() {
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                final long time = System.currentTimeMillis();
                try {
                    try {
                        return method.invoke(application, args);
                    } catch (InvocationTargetException e) {
                        throw e.getTargetException();
                    }
                } finally {
                    System.out
                            .println("invocation: "
                                    + method.getName()
                                    + " time: "
                                    + (System.currentTimeMillis() - time)
                                    + "ms");
                }
            }
        });
    }

    @SuppressWarnings({"MethodMayBeStatic"})
    @SkipValidation
    public String index() {
        return Action.SUCCESS;
    }

    @PostOnly
    public String create() {
        throw new RuntimeException("méthode d'appel interdite");
    }

    protected static void pushValue(final Object data) {
        ActionContext.getContext().getValueStack().push(data);
    }

    public void setRequest(final SportRequest request) {
        this.request = request;
    }

    public User getUser() {
        return request.isLogged() ? request.getSportSession().getUser() : null;
    }

    public void setActionDescription(final ActionDetail actionDescription) {
        this.actionDescription = actionDescription;
    }

    public List<NewMessageData> getNewMessages() {
        if (newMessages == null)
            newMessages = application.fetchNewMessagesCount(getUser());
        return newMessages;
    }
}
