package com.nraynaud.sport.web;

import com.nraynaud.sport.UserStore;
import static com.nraynaud.sport.web.Constants.LOGIN_RESULT;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

public class SportInterceptor implements Interceptor {
    private static final Class<?>[] NO_PARAMS = new Class[0];

    private final UserStore userStore;

    public SportInterceptor(final UserStore userStore) {
        this.userStore = userStore;
    }

    public void destroy() {
    }

    public void init() {
    }

    public String intercept(final ActionInvocation invocation) throws Exception {
        final Class<?> actionClass = invocation.getAction().getClass();
        final Method actionMethod = getActionMethod(actionClass, invocation.getProxy().getMethod());
        final HttpServletRequest servletRequest = ServletActionContext.getRequest();
        filterPostMethod(actionMethod, servletRequest);
        if (isPublic(actionClass)) return invocation.invoke();
        return invokeIfUserLogged(invocation, servletRequest);
    }

    private String invokeIfUserLogged(final ActionInvocation invocation, final HttpServletRequest servletRequest) throws
            Exception {
        return isLogged(servletRequest) ? LOGIN_RESULT : invocation.invoke();
    }

    private boolean isLogged(final HttpServletRequest servletRequest) {
        return SportSession.storeIntoRequest(userStore, servletRequest) == null;
    }

    private static boolean isPublic(final Class<?> actionClass) throws Exception {
        return actionClass.isAnnotationPresent(Public.class);
    }

    private static void filterPostMethod(final Method actionMethod, final HttpServletRequest servletRequest) {
        if (actionMethod.isAnnotationPresent(PostOnly.class) && !servletRequest.getMethod().equals("POST"))
            throw new RuntimeException("méthode d'appel interdite");
    }

    @SuppressWarnings({"CaughtExceptionImmediatelyRethrown"})
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
