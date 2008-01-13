package com.nraynaud.sport.web.result;

import com.nraynaud.sport.web.view.PageDetail;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.inject.Inject;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.StrutsConstants;
import org.apache.struts2.components.Include;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class LayoutResult extends StrutsResultSupport {
    private String encoding;

    public void doExecute(final String finalLocation, final ActionInvocation invocation) throws Exception {
        final StringWriter stringWriter = new StringWriter();
        final HttpServletResponse response = ServletActionContext.getResponse();
        final HttpServletRequest request = ServletActionContext.getRequest();
        includeWithCharset(finalLocation, stringWriter, response, request);
        PageDetail.detailFor(request).setContent(stringWriter.toString());
        execute("/WEB-INF/application.jsp", request, response);
    }

    //HACk HACK HACK
    // Include.include() uses the response encoding to write to the buffer,
    // but reads it with the strut's default encoding.
    // setting the response to the struts encoding before inclusion ensures that read and write will occur in the same encoding.
    private void includeWithCharset(final String finalLocation, final Writer writer, final HttpServletResponse response,
                                    final HttpServletRequest request) throws ServletException, IOException {
        response.setCharacterEncoding(encoding);
        Include.include(finalLocation, writer, request, response);
    }

    @SuppressWarnings({"StringConcatenation"})
    private static void execute(final String finalLocation,
                                final HttpServletRequest request,
                                final HttpServletResponse response) throws Exception {
        final RequestDispatcher dispatcher = request.getRequestDispatcher(finalLocation);

        // if the view doesn't exist, let's do a 404
        if (dispatcher == null) {
            response.sendError(404, "result '" + finalLocation + "' not found");
            return;
        }

        // If we're included, then include the view
        // Otherwise do forward
        // This allow the page to, for example, set content type
        if (!response.isCommitted() && (request.getAttribute("javax.servlet.include.servlet_path") == null)) {
            request.setAttribute("struts.view_uri", finalLocation);
            request.setAttribute("struts.request_uri", request.getRequestURI());

            dispatcher.forward(request, response);
        } else {
            dispatcher.include(request, response);
        }
    }

    @Inject(StrutsConstants.STRUTS_I18N_ENCODING)
    public void setDefaultEncoding(final String encoding) {
        this.encoding = encoding;
    }
}
