package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.data.ConversationData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import static com.opensymphony.xwork2.Action.INPUT;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
    @Result(value = "/WEB-INF/pages/messages.jsp"),
    @Result(name = INPUT, value = "/WEB-INF/pages/messages.jsp")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class Action extends DefaultAction {
    public String receiver;
    private ConversationData conversationData;
    public int pageIndex;

    public Action(final Application application) {
        super(application);
    }

    public ConversationData getConversationData() {
        if (conversationData == null && receiver != null) {
            conversationData = application.fetchConvertationData(getUser(), receiver, null, pageIndex);
        }
        return conversationData;
    }
}
