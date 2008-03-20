package com.nraynaud.sport.web;

import com.nraynaud.sport.User;
import com.nraynaud.sport.UserNotFoundException;
import com.nraynaud.sport.UserStore;
import static com.nraynaud.sport.web.Constants.LOGIN_RESULT;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxy;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class SportInterceptor extends AbstractInterceptor {
    private static final Class<?>[] NO_PARAMS = new Class[0];
    private final UserStore userStore;

    public SportInterceptor(final UserStore userStore) {
        this.userStore = userStore;
    }

    public String intercept(final ActionInvocation invocation) throws Exception {
        final HttpServletRequest servletRequest = ServletActionContext.getRequest();
        handleRememberMe(servletRequest, ServletActionContext.getResponse());
        final SportRequest request = new SportRequest(userStore, servletRequest);
        final ActionContext invocationContext = invocation.getInvocationContext();
        invocationContext.put(SportRequest.SPORT_REQUEST, request);
        invocationContext.getValueStack().push(request);
        final Object action = invocation.getAction();
        final Class<?> actionClass = action.getClass();
        final ActionProxy actionProxy = invocation.getProxy();
        final Method actionMethod = getActionMethod(actionClass, actionProxy.getMethod());
        filterPostMethod(actionMethod, request);
        setMetadata(action, actionProxy, invocationContext);
        try {
            final Method requestMethod = actionClass.getMethod("setRequest", SportRequest.class);
            requestMethod.invoke(action, request);
        } catch (NoSuchMethodException e) {
            //ok, no problem, sorry for the inconvenience
        }
        if (isPublic(actionClass)) return invocation.invoke();
        return request.isLogged() ? invocation.invoke() : LOGIN_RESULT;
    }

    private void handleRememberMe(final HttpServletRequest servletRequest, final HttpServletResponse response) {
        final Cookie[] cookies = servletRequest.getCookies();
        if (cookies == null)
            return;
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals(Constants.REMEMBER_COOKIE_NAME)) {
                try {
                    final User user = userStore.fetchRememberedUser(cookie.getValue());
                    SportSession.openSession(user, servletRequest, true);
                } catch (UserNotFoundException e) {
                    //no problem, forget it
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    private static void setMetadata(final Object action, final ActionProxy actionProxy,
                                    final ActionContext invocationContext) {
        if (action instanceof DefaultAction) {
            ((DefaultAction) action).setActionDescription(new ActionDetail(actionProxy.getNamespace(),
                    actionProxy.getActionName(), invocationContext.getParameters()));
        }
    }

    private static boolean isPublic(final Class<?> actionClass) throws Exception {
        return actionClass.isAnnotationPresent(Public.class);
    }

    private static void filterPostMethod(final Method actionMethod, final SportRequest servletRequest) {
        if (actionMethod.isAnnotationPresent(PostOnly.class) && !servletRequest.getMethod().equals("POST"))
            throw new RuntimeException("méthode d'appel interdite");
    }

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
