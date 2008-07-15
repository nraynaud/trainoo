<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.web.view.TableContent" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<div class="block sheetBlock">
    <div class="content">
        <dl class="sheet">
            <% final TableContent content = top(TableContent.class);
                for (final TableContent.TableSheet sheet : content.sheets) {
                    call(pageContext, "workoutSheet.jsp", sheet);
                }
            %>
        </dl>
    </div>
</div>