package com.nraynaud.sport.web;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * excatly the same as  {@link com.opensymphony.xwork2.interceptor.ModelDrivenInterceptor} but
 * doesn't fetch the model twice !
 */
public class SportModelDrivenInterceptor extends AbstractInterceptor {

    public String intercept(final ActionInvocation invocation) throws Exception {
        final Object action = invocation.getAction();
        if (action instanceof ModelDriven) {
            final ModelDriven<?> modelDriven = (ModelDriven<?>) action;
            //avoid fetching the model twice.
            final Object model = modelDriven.getModel();
            if (model != null)
                invocation.getStack().push(model);
        }
        return invocation.invoke();
    }
}
