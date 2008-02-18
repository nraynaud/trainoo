package com.nraynaud.sport.web.result;

import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.ChainBackCapable;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;

import java.util.Collections;

public abstract class BackResult implements Result {

    public void execute(final ActionInvocation invocation) throws Exception {
        final Object action = invocation.getAction();
        if (action instanceof ChainBackCapable) {
            final ChainBackCapable chainBackAction = (ChainBackCapable) action;
            final ActionDetail fromActionDetail;
            if (!invocation.getResultCode().equals(Action.SUCCESS) && chainBackAction.getOnErrorAction() != null)
                fromActionDetail = chainBackAction.getOnErrorAction();
            else
                fromActionDetail = chainBackAction.getFromAction();
            //remove our tracks
            chainBackAction.setActionDescription(chainBackAction.getFromAction());
            //resets conversion error to avoid double reporting
            invocation.getInvocationContext().setConversionErrors(Collections.emptyMap());
            final Result result = createRealResult(invocation, fromActionDetail);
            result.execute(invocation);
        } else
            throw new RuntimeException("I'm lost, Action doesn't supports ChainBackCapable");
    }

    protected abstract Result createRealResult(ActionInvocation invocation, ActionDetail detail);
}
