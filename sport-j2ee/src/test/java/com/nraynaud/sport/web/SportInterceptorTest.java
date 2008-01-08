package com.nraynaud.sport.web;

import com.opensymphony.xwork2.mock.MockActionInvocation;
import com.opensymphony.xwork2.mock.MockActionProxy;
import org.junit.Test;

public class SportInterceptorTest {

    @Test
    public void testNothing() throws Exception {
        final SportInterceptor interceptor = new SportInterceptor(null);
        final MockActionInvocation invocation = new MockActionInvocation();
        final Object action = new Object() {
            public String go() {
                return "";
            }
        };
        invocation.setAction(action);
        final MockActionProxy actionProxy = new MockActionProxy();
        actionProxy.setMethod("go");
        invocation.setProxy(actionProxy);
//        interceptor.intercept(invocation);
    }
}
