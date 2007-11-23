<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Les derniers entraînements</title>
    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="center">
    <h1>Les derniers Entraînements</h1>
    <span style="display:inline-block;">
        <table>
            <s:iterator value="workouts">
                <tr>
                    <td><s:date name="date" format="E dd/M/yy"/></td>
                    <td><s:property value="user.name"/></td>
                </tr>
            </s:iterator>
        </table>
    </span>
    <hr/>

<a href="<s:url action='login'/>">S'identifier</a> | <a href="<s:url action='signup'/>">S'inscrire</a>
</div>
</body>
</html>