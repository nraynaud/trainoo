package com.nraynaud.sport.web.action.track;

import com.nraynaud.sport.Application;
import com.nraynaud.sport.web.Constants;
import com.nraynaud.sport.web.PostOnly;
import com.nraynaud.sport.web.actionsupport.DefaultAction;
import com.nraynaud.sport.web.result.Redirect;
import static com.opensymphony.xwork2.Action.SUCCESS;
import jfun.parsec.pattern.Pattern;
import static jfun.parsec.pattern.Patterns.*;
import org.apache.struts2.config.ParentPackage;
import org.apache.struts2.config.Result;
import org.apache.struts2.config.Results;

@Results({
@Result(name = SUCCESS, type = Redirect.class, params = {"namespace", "/track"}, value = "")
        })
@ParentPackage(Constants.STRUTS_PACKAGE)
public class CreateAction extends DefaultAction {
    public String track;
    public double length;
    private static final Pattern POINT_LIST;

    static {
        final Pattern coord = seq(optional(isChar('-')), isDecimal());
        final Pattern point = seq(isChar('['), coord, isChar(','), coord, isChar(']'));
        POINT_LIST = seq(point, many(seq(isChar(','), point)));
    }

    public CreateAction(final Application application) {
        super(application);
    }

    @PostOnly
    public String create() {
        ensurePointFormat(track);
        application.createTrack(getUser(), track, length);
        return SUCCESS;
    }

    public static void ensurePointFormat(final String points) {
        if (!matchPoint(points))
            throw new RuntimeException("error in track : " + points);
    }

    public static boolean matchPoint(final String input) {
        final int result = POINT_LIST.match(input, input.length(), 0);
        return result == input.length();
    }
}
