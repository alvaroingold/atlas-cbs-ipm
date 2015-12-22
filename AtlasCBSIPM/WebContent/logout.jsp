<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="" %>


<html>
<head>
<title>Logout</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<body>
<%
session.invalidate();

response.sendRedirect("viewer.jsp");
%>


</body>
</html>
