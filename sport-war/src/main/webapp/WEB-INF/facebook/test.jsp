<%@ page import="com.nraynaud.sport.web.action.facebook.Action" %>
<%@ page session="false" contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:fb="http://www.facebook.com/2008/fbml">
    <head>
        <title>LOL</title>
    </head>
    <body>
        <h1>LOL le titre</h1>
        <script src="http://static.ak.connect.facebook.com/js/api_lib/v0.4/FeatureLoader.js.php"
                type="text/javascript"></script>
        <fb:login-button onlogin="update_user_box()"></fb:login-button>

        <div id="user"></div>
        <script type="text/javascript">
            function update_user_box() {
                var user_box = document.getElementById("user");
                user_box.innerHTML = "<span>" + "<fb:profile-pic uid=loggedinuser facebook-logo=true></fb:profile-pic>"
                        + "Welcome, <fb:name uid=loggedinuser useyou=false></fb:name>. You are signed in with your Facebook account."
                        + "</span>";
                FB.XFBML.Host.parseDomTree();
            }
            FB.init("<%=Action.getHelper(request, response).getApiKey()%>", "/facebook/xd_receiver.htm", {"ifUserConnected" : update_user_box});
        </script>
    </body>
</html>