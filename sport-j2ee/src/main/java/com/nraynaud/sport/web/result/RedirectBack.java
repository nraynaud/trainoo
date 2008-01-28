package com.nraynaud.sport.web.result;

import com.nraynaud.sport.web.ActionDetail;
import com.nraynaud.sport.web.action.MessagesAction;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.Result;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.dispatcher.mapper.ActionMapper;

import java.util.Map;

public class RedirectBack implements Result {
    private ActionMapper actionMapper;

    public void execute(final ActionInvocation invocation) throws Exception {
        final Object action = invocation.getAction();
        if (action instanceof MessagesAction) {
            final MessagesAction messageAction = (MessagesAction) action;
            final String fromAction = messageAction.getFromAction();
            final ActionDetail detail = new ActionDetail(fromAction);
            final Redirect result = new Redirect(detail.namespace, detail.name, "index");
            result.setActionMapper(actionMapper);
            for (final Object o : detail.parameters.entrySet()) {
                final Map.Entry entry = (Map.Entry) o;
                result.addParameter((String) entry.getKey(), ((String[]) entry.getValue())[0]);
            }
            result.execute(invocation);
        }
    }

    @Inject
    public void setActionMapper(final ActionMapper mapper) {
        actionMapper = mapper;
    }
}
