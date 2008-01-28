package com.nraynaud.sport.web;

import com.nraynaud.sport.UserStore;
import static com.nraynaud.sport.web.Constants.LOGIN_RESULT;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;

import java.lang.reflect.Method;

public class SportInterceptor extends AbstractInterceptor {
    private static final Class<?>[] NO_PARAMS = new Class[0];
    private final UserStore userStore;

    public SportInterceptor(final UserStore userStore) {
        this.userStore = userStore;
    }

    public String intercept(final ActionInvocation invocation) throws Exception {
        final SportRequest request = new SportRequest(userStore, ServletActionContext.getRequest());
        final ActionContext invocationContext = invocation.getInvocationContext();
        invocationContext.put(SportRequest.SPORT_REQUEST, request);
        invocationContext.getValueStack().push(request);
        final Object action = invocation.getAction();
        final Class<?> actionClass = action.getClass();
        final ActionProxy actionProxy = invocation.getProxy();
        final Method actionMethod = getActionMethod(actionClass, actionProxy.getMethod());
        filterPostMethod(actionMethod, request);
        setMetadata(action, actionProxy, invocationContext);
        final Method requestMethod;
        try {
            requestMethod = actionClass.getMethod("setRequest", SportRequest.class);
            requestMethod.invoke(action, request);
        } catch (NoSuchMethodException e) {
            //ok, no problem
        }
        if (isPublic(actionClass)) return invocation.invoke();
        return request.isLogged() ? invocation.invoke() : LOGIN_RESULT;
    }

    private static void setMetadata(final Object action, final ActionProxy actionProxy,
                                    final ActionContext invocationContext) {
        if (action instanceof DefaultAction) {
            final String encodedAction = new ActionDetail(actionProxy.getNamespace(), actionProxy.getActionName(),
                    invocationContext.getParameters()).encodedAction;
            ((DefaultAction) action).setActionDescription(encodedAction);
        }
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
