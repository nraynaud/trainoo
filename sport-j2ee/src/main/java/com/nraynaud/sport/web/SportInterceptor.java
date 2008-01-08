package com.nraynaud.sport.web;

import com.nraynaud.sport.UserStore;
import static com.nraynaud.sport.web.Constants.LOGIN_RESULT;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import org.apache.struts2.ServletActionContext;

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
        final SportRequest request = new SportRequest(userStore, ServletActionContext.getRequest());
        invocation.getInvocationContext().put("sportRequest", request);
        final Object action = invocation.getAction();
        final Class<?> actionClass = action.getClass();
        final Method actionMethod = getActionMethod(actionClass, invocation.getProxy().getMethod());
        filterPostMethod(actionMethod, request);
        final Method requestMethod;
        try {
            requestMethod = actionClass.getMethod("setRequest", SportRequest.class);
            requestMethod.invoke(action, request);
        } catch (NoSuchMethodException e) {
            //ok, no problem
        }
        if (isPublic(actionClass)) return invocation.invoke();
        return request.isLogged() ? LOGIN_RESULT : invocation.invoke();
    }

    private static boolean isPublic(final Class<?> actionClass) throws Exception {
        return actionClass.isAnnotationPresent(Public.class);
    }

    private static void filterPostMethod(final Method actionMethod, final SportRequest servletRequest) {
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
