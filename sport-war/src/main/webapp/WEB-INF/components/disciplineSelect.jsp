<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:select id="discipline"
          list="{'course', 'vélo', 'VTT', 'marche', 'natation', 'roller', 'vélo ellipitique', 'vélo d\\'appartement'}"
          name="discipline" required="true" value="discipline"/>