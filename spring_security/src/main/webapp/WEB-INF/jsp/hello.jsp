<%@ taglib prefix="th" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2018/7/26
  Time: 15:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h1>Hello ${remoteUser}!</h1>
<form action="/logout" method="post">
    <input type="submit" value="Sign Out"/>
</form>
</body>
</html>
