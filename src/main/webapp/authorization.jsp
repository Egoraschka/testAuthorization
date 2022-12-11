<%--
  Created by IntelliJ IDEA.
  User: Егор
  Date: 10.12.2022
  Time: 16:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Authorization</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<form action="controller" method="post">
    <input type="hidden" name="option" value="authorization">
    <label>Login<input type="text" name="login"></label>
    <label>Password<input type="password" name="password"></label>
    <button type="submit">Sign in</button>
</form>
</body>
</html>
