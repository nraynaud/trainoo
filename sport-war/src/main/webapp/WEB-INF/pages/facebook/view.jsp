<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.BibPageData" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

Salut <%=stringProperty("name")%> !
<br>
Compte trainoo :
<form action="" method="POST">
    <% final String trainoo_account = stringProperty("trainoo_account");
        if (trainoo_account == null) {%>
    <input type="text" name="trainoo_account"> <input type="submit" value="Envoyer !">
    <% } else { %>
    <%=trainoo_account%> <input type="hidden" name="trainoo_account" value="0"> <input type="submit"
                                                                                       value="Supprimer !">
    <%}%>
</form>
<%
    if (trainoo_account != null) {
        final BibPageData data = property("model", BibPageData.class);
        if (data == null) {
%>
Désolé, ce compte est inconnu sur trainoo.com.
<%
        } else {
            call(pageContext, "facebookTable.jsp", data);
        }
    }
%>