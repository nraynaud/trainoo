<%@ taglib prefix="r" uri="/sport-tags" %>
<%@ page import="com.nraynaud.sport.Workout" %>
<%@ page import="com.nraynaud.sport.web.view.DataHelper" %>
<%@ page import="com.nraynaud.sport.web.view.PageDetail" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="java.util.List" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%
    final Workout workout = top();
    final List<DataHelper.Data> dataList = DataHelper.compute(workout);
    final PageDetail pageDetail = PageDetail.detailFor(request);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Entraînement de <%=workout.getUser().getName()%>
        </title>
        <link rel="icon" href="<%=stat("/static/favicon.ico")%>" type="image/vnd.microsoft.icon">
        <link href="<%=stat("/static/sport.css")%>" rel="stylesheet" type="text/css">
        <link href="<%=stat("/static/reset.css")%>" rel="stylesheet" type="text/css">
        <link href="<%=stat("/static/pimp/sport_pimp.css")%>" rel="stylesheet" type="text/css">
        <!--[if lt IE 7]>
        <link href="<%=stat("/static/sport_ie6.iecss")%>" rel="stylesheet" type="text/css">
    <![endif]-->
        <%
            for (final String header : pageDetail.getHeaders()) {
                out.append(header);
            }
        %>
        <style type="text/css">
            .workoutBlock {
                padding: 0;
            }

            .workoutBlock .content {
                margin: 0;
                padding: 0;
            }

            .nikeGraphContainer {
                height: 50px;
            }

            .nikePlusLink {
                visibility: hidden;
            }
        </style>
    </head>
    <body style="background-color:white;font-size:50%;width:250px">
        <div class="block workoutBlock" id="workoutBlock">
            <div class="content">
                <dl>
                    <%
                        for (final DataHelper.Data row : dataList) {
                    %>
                    <dt <%=row.userProvided ? "class=\"editable\"" : ""%>><%=row.label%>
                    </dt>
                    <dd><span><%=row.value%></span></dd>
                    <%}%>

                </dl>
                <% if (workout.isNikePlus() && workout.getUser().getNikePlusId() != null) { %>
                <div>
                    <%call(pageContext, "nikePlusDetail.jsp", workout);%>
                </div>

                <%}%>
                <div style="text-align:right;"><a
                        href="<%=createUrl("/workout", "", "id", workout.getId().toString())%>" target="_blank"
                        title="Aller à la page de l'entraînement"><img
                        src='<%=stat("/static/external/logo_widget.png")%>' alt=""></a></div>
            </div>
        </div>
        <script type="text/javascript" src="<%=stat("/static/prototype.js")%>"></script>
        <script type="text/javascript" src="<%=stat("/static/effects.js")%>"></script>
        <script type="text/javascript" src="<%=stat("/static/controls.js")%>"></script>
        <script type="text/javascript" src="<%=stat("/static/sport.js")%>"></script>
        <r:writeJavascript/>
        <script type="text/javascript">
            var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
            document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
        </script>
        <script type="text/javascript">
            var pageTracker = _gat._getTracker("UA-3412937-1");
            pageTracker._initData();
            pageTracker._trackPageview();
        </script>
    </body>
</html>