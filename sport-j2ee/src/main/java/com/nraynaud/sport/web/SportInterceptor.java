package com.nraynaud.sport.web;

import static com.nraynaud.sport.web.Constants.LOGIN_RESULT;
import static com.nraynaud.sport.web.Constants.USER_KEY;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

public class SportInterceptor implements Interceptor {
    private static final Class[] NO_PARAMS = new Class[0];

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(final ActionInvocation invocation) throws Exception {
        final Class<?> actionClass = invocation.getAction().getClass();
        final Method actionMethod = getActionMethod(actionClass, invocation.getProxy().getMethod());

        final HttpServletRequest servletRequest = ServletActionContext.getRequest();
        if (actionMethod.isAnnotationPresent(PostOnly.class) && !servletRequest.getMethod().equals("POST"))
            throw new RuntimeException("méthode d'appel interdite");
        if (actionClass.isAnnotationPresent(Public.class))
            return invocation.invoke();
        final HttpSession session = servletRequest.getSession(false);
        if (session == null || session.getAttribute(USER_KEY) == null)
            return LOGIN_RESULT;
        else
            return invocation.invoke();
    }

    // FIXME: This is copied from DefaultActionInvocation but should be exposed through the interface
    protected static Method getActionMethod(final Class<?> actionClass, final String methodName) throws
            NoSuchMethodException {
        Method method;
        try {
            method = actionClass.getMethod(methodName, NO_PARAMS);
        } catch (NoSuchMethodException e) {
            // hmm -- OK, try doXxx instead
            try {
                final String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
                method = actionClass.getMethod(altMethodName, NO_PARAMS);
            } catch (NoSuchMethodException e1) {
                // throw the original one
                throw e;
            }
        }
        return method;
    }
}
