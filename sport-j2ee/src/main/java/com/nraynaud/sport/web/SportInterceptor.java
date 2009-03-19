package com.nraynaud.sport.web;

import com.google.code.facebookapi.FacebookException;
import com.nraynaud.sport.FacebookUtil;
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
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import static java.net.URLDecoder.decode;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SportInterceptor extends AbstractInterceptor {
    private static final Class<?>[] NO_PARAMS = new Class[0];
    private final UserStore userStore;

    public SportInterceptor(final UserStore userStore) {
        this.userStore = userStore;
    }

    public String intercept(final ActionInvocation invocation) throws Exception {
        final HttpServletRequest servletRequest = ServletActionContext.getRequest();
        final SportRequest request = new SportRequest(userStore, servletRequest);
        if (!request.isLogged())
            handleRememberMe(servletRequest, ServletActionContext.getResponse());
        final ActionContext invocationContext = invocation.getInvocationContext();
        invocationContext.put(SportRequest.SPORT_REQUEST, request);
        invocationContext.getValueStack().push(request);
        final Object action = invocation.getAction();
        final Class<?> actionClass = action.getClass();
        final ActionProxy actionProxy = invocation.getProxy();
        final Method actionMethod = getActionMethod(actionClass, actionProxy.getMethod());
        filterPostMethod(actionMethod, request);
        setMetadata(action, actionProxy, servletRequest);
        try {
            final Method requestMethod = actionClass.getMethod("setRequest", SportRequest.class);
            requestMethod.invoke(action, request);
        } catch (NoSuchMethodException e) {
            //ok, no problem, sorry for the inconvenience
        }
        if (isPublic(actionClass)) return invocation.invoke();
        return request.isLogged() ? invocation.invoke() : LOGIN_RESULT;
    }

    private void handleRememberMe(final HttpServletRequest request, final HttpServletResponse response) {
        if (handleFacebook(request, response)) return;
        final Cookie[] cookies = request.getCookies();
        if (cookies == null)
            return;
        for (final Cookie cookie : cookies) {
            if (cookie.getName().equals(Constants.REMEMBER_COOKIE_NAME)) {
                try {
                    final User user = userStore.fetchRememberedUser(cookie.getValue());
                    SportSession.openSession(user, request, true);
                } catch (UserNotFoundException e) {
                    //no problem, forget it
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    private boolean handleFacebook(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final Long facebookId = FacebookUtil.getFacebookUserId(request, response);
            if (facebookId != null) {
                final User user = userStore.facebookLogin(facebookId);
                if (user != null) {
                    SportSession.openSession(user, request, false);
                    return true;
                } else {
                }
            }
        } catch (FacebookException e) {
            //let's not insist
            e.printStackTrace();
            return false;
        }
        return false;
    }

    private static void setMetadata(final Object action, final ActionProxy actionProxy,
                                    final HttpServletRequest invocationContext) {
        if (action instanceof DefaultAction) {
            final Map<String, String[]> getParams = queryParams(invocationContext);
            ((DefaultAction) action).setActionDescription(new ActionDetail(actionProxy.getNamespace(),
                    actionProxy.getActionName(), getParams));
        }
    }

    private static Map<String, String[]> queryParams(final HttpServletRequest invocationContext) {
        final Map<String, String[]> getParams = new HashMap<String, String[]>();
        final String queryString = invocationContext.getQueryString();
        final StringTokenizer st1 = new StringTokenizer(queryString == null ? "" : queryString, "&");
        while (st1.hasMoreTokens()) {
            final StringTokenizer st2 = new StringTokenizer(st1.nextToken(), "=");
            try {
                final String key = decode(st2.nextToken(), "UTF-8");
                final String[] value;
                if (st2.hasMoreTokens())
                    value = new String[]{decode(st2.nextToken(), "UTF-8")};
                else
                    value = new String[]{""};
                getParams.put(key, value);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }
        }
        return getParams;
    }

    private static boolean isPublic(final Class<?> actionClass) {
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
