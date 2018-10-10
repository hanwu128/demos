<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>出错啦</title>
</head>
<body>
	<div>遇到了一些问题V_V</div>
	<%
		Exception exception = (Exception)request.getAttribute("exception");
		exception.printStackTrace();
		out.println(exception.getMessage());
	%>
</body>
</html>