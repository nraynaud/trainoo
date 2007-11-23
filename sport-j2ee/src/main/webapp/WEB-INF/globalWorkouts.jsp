<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<head>
    <title>Les derniers entraînements</title>
    <link href="<s:url value="/static/sport.css"/>" rel="stylesheet" type="text/css"/>
</head>
<body>
<div id="center">
    <a href="<s:url action='login'/>">S'identifier</a> | <a href="<s:url action='signup'/>">S'inscrire</a>
    <hr>
    <h1>Les derniers Entraînements</h1>
    <span style="display:inline-block;">
        <s:set name="lol" value="%{'workoutTable.jsp'}"/>
        <s:component template="%{lol}" templateDir="WEB-INF">
            <s:param name="displayName" value="true"/>
        </s:component>
    </span>
    <hr/>

</div>
</body>
</html>