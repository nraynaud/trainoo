<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="com.nraynaud.sport.data.NewMessageData" %>
<%@ page import="com.nraynaud.sport.User" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>

<div id="sidebar">
     <div id="searchBox">
        <form action="http://trainoo.com/search/" id="cse-search-box">
            <div>
                <input type="hidden" name="cx" value="partner-pub-1788371406648361:36ti6n6fck5"/>
                <input type="hidden" name="cof" value="FORID:10"/>
                <input type="hidden" name="ie" value="UTF-8"/>
                <input type="text" class="text" name="q" size="31"/><!--
                --><input type="submit" class="submit" name="sa" value="Rechercher"/>
            </div>
        </form>
        <script type="text/javascript"
                src="http://www.google.com/coop/cse/brand?form=cse-search-box&amp;lang=fr"></script>
    </div>
    <div class="content textContent">
        <% if (isLogged()) { %>
        <h2><%=currentUser().getName()%></h2>
        <ul>
            <li><a href="<%=createUrl("/", "workouts")%>" title="Mon vestiaire">Mon vestiaire</a></li>
            <li><a href="<%=createUrl("/bib", "", "id", String.valueOf(currentUser().getId()))%>" title="Mon dossard">Mon dossard</a></li>
        </ul>
        <ul class="secondary">
            <li><a href="<%=createUrl("/privatedata", "")%>" title="Mon compte">Mon compte</a></li>
            <li><a href="<%=createUrl("/", "logout")%>" title="Déconnexion">Déconnexion</a></li>
        </ul>
        <% } else { %>
        <h2>Connexion</h2>
        <form action="<%=createUrl("/", "login", "fromAction", findFromAction())%>" method="POST">
            <ul>
                <li>
                    <label for="loginSide">Surnom&nbsp;:</label>
                    <input name="login" id="loginSide" class="text"/>
                </li>
                <li>
                    <label for="passwordSide">Mot de passe&nbsp;:</label>
                    <input name="password" id="passwordSide" type="password" class="text"/>
                </li>
                <li>
                    <input type="submit" class="submit" name="submit" value="Connexion"/>
                </li>
            </ul>
        </form>
        <% } %>
    </div>
    <div id="adPlaceHolder">publicité google</div>
</div>
