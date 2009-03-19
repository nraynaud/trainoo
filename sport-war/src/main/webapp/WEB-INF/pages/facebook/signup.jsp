<%@ page import="static com.nraynaud.sport.web.action.SignupAction.*" %>
<%@ page import="static com.nraynaud.sport.web.view.Helpers.*" %>
<%@ page import="static com.nraynaud.sport.web.view.StackUtil.*" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib prefix="p" uri="/sport-tags" %>

<p:layoutParams title="Inscription par facebook"/>
<div id="tinyCenter">
    <div class="block">
        <div class="content textContent">
            Vous allez vous inscrire avec votre compte Facebook. Pour revenir sur le site cliquez simplement sur l'icône
            facebook.
            <form name="signup" method="post" action="<%=createUrl(currentAction())%>">
                <s:actionerror/>
                <s:fielderror/>
                <input type="hidden" name="fromAction" value="<%=fromActionOrCurrent()%>"/>
                <span class="label">
                    <label for="login">Votre surnom</label>
                    <span class="help fullWidth">c'est par ce nom que vous serez visible sur tout le site</span>
                </span>
                <span class="input fullWidth">
                    <input name="login" id="login" class="text" value="<%=propertyEscapedOrNull("login", "")%>">
                </span>

                <p:javascript>$('login').focus();</p:javascript>
                <p:javascript-raw>makeItCount('login', <%= LOGIN_MAX_LENGTH%>, <%= LOGIN_MIN_LENGTH%>);</p:javascript-raw>

                <span class="actions">
                    <input type="submit" value="Connection"/>
                </span>
            </form>
        </div>
    </div>
</div>
