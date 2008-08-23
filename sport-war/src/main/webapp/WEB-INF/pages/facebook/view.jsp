<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

Salut <%=stringProperty("name")%> !
<br>
Compte trainoo :
<form method="POST">
    <% final String trianoo_account = stringProperty("trainoo_account");
        if (trianoo_account == null) {%>
    <input type="text" name="trainoo_account"> <input type="submit" value="Envoyer !">
    <% } else { %>
    <%=trianoo_account%> <input type="hidden" name="trainoo_account" value="0"> <input type="submit"
                                                                                       value="Supprimer !">
    <%}%>
</form>