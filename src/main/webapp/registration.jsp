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
    <title>Registration</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<form action="controller" method="post">
    <input type="hidden" name="option" value="registration">
    <label>Enter your email<input type="email" name="email"></label>
    <label>Enter your name<input type="text" name="name"></label>
    <label>Create login<input type="text" name="login"></label>
    <label>Create password<input type="password" name="password"></label>
    <button type="submit">Sign up</button>
</form>
</body>
</html>
