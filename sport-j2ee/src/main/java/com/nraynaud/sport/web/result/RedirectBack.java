package com.nraynaud.sport.web.result;

import com.nraynaud.sport.web.ActionDetail;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.dispatcher.mapper.ActionMapper;

import java.util.Map;

/**
 * Redirect (with 302) to the previous action.
 */
public class RedirectBack extends BackResult {
    private ActionMapper actionMapper;

    protected Result createRealResult(final ActionInvocation invocation, final ActionDetail detail) {
        final Redirect result = new Redirect(detail.namespace, detail.name, "index");
        result.setActionMapper(actionMapper);
        for (final Map.Entry<String, String[]> entry : detail.parameters.entrySet()) {
            result.addParameter(entry.getKey(), entry.getValue()[0]);
        }
        return result;
    }

    @Inject
    public void setActionMapper(final ActionMapper mapper) {
        actionMapper = mapper;
    }
}
