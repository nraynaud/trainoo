package com.nraynaud.sport.web.result;

import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.ChainBackCapable;
import com.opensymphony.xwork2.ActionChainResult;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ActionProxyFactory;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.inject.Inject;

/**
 * When an error arise in an action, we go to the previous user action by chaining, so that we render the
 * same page to the user, but with the error fields shawn.
 */
public class ChainBack implements Result {
    private ActionProxyFactory actionProxyFactory;

    public void execute(final ActionInvocation invocation) throws Exception {
        final Object action = invocation.getAction();
        if (action instanceof ChainBackCapable) {
            final ChainBackCapable messageAction = (ChainBackCapable) action;
            final String fromAction = messageAction.getFromAction();
            final ActionDetail detail = new ActionDetail(fromAction);
            final ActionChainResult chainResult = new ActionChainResult(detail.namespace, detail.name, "index");
            invocation.getInvocationContext().setParameters(detail.parameters);
            chainResult.setActionProxyFactory(actionProxyFactory);
            chainResult.execute(invocation);
        }
    }

    @Inject
    public void setActionProxyFactory(final ActionProxyFactory actionProxyFactory) {
        this.actionProxyFactory = actionProxyFactory;
    }
}
