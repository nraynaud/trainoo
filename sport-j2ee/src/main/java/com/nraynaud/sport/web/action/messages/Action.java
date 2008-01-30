package com.nraynaud.sport.web.action.messages;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.WorkoutNotFoundException;
import com.nraynaud.sport.data.ConversationData;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.DataInputException;
import com.nraynaud.sport.web.DefaultAction;
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
    private String receiver;
    private ConversationData conversationData;
    private final Application application;

    public Action(final Application application) {
        this.application = application;
    }

    public ConversationData getConversationData() {
        if (conversationData == null && receiver != null) {
            try {
                conversationData = application.fetchConvertationData(getUser(), receiver, null);
            } catch (WorkoutNotFoundException e) {
                throw new DataInputException(e);
            }
        }
        return conversationData;
    }

    public void setReceiver(final String receiver) {
        this.receiver = receiver;
    }

    public String getReceiver() {
        return receiver;
    }
}
