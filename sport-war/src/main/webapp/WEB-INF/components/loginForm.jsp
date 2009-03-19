<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>


<h2>Connexion</h2>

<form action="<%=createUrl("/", "login", "fromAction", fromActionOrCurrent())%>" method="POST">
    <ul>
        <li>
            <label for="loginSide">Surnom&nbsp;:</label>
            <input name="login" id="loginSide" class="text">
        </li>
        <li>
            <label for="passwordSide">Mot de passe&nbsp;:</label>
            <input name="password" id="passwordSide" type="password" class="text">
        </li>
        <li>
            <input type="submit" class="submit" name="submit" value="Connexion">
        </li>
    </ul>
</form>
<%=link("/", "forgotPassword", "Mot de passe oublié", "")%>