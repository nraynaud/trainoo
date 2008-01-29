package com.nraynaud.sport.web.result;

import com.nraynaud.sport.web.ActionDetail;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.dispatcher.mapper.ActionMapper;

import java.util.Map;

public class RedirectBack extends BackResult implements Result {
    private ActionMapper actionMapper;

    protected Result createRealResult(final ActionInvocation invocation, final ActionDetail detail) {
        final Redirect result = new Redirect(detail.namespace, detail.name, "index");
        result.setActionMapper(actionMapper);
        for (final Object o : detail.parameters.entrySet()) {
            final Map.Entry entry = (Map.Entry) o;
            result.addParameter((String) entry.getKey(), ((String[]) entry.getValue())[0]);
        }
        return result;
    }

    @Inject
    public void setActionMapper(final ActionMapper mapper) {
        actionMapper = mapper;
    }
}
