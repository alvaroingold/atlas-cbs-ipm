<html>
<body>
<%@page import="java.net.URLEncoder"%>
<%
String format = java.net.URLEncoder.encode("png:w200,h200,b32,#ffffff");
String mol = java.net.URLEncoder.encode("CN1C=NC2=C1C(=O)N(C)C(=O)N2C");
%>
<img src="http://localhost:8080/examples/jsp/marvin/generate_image.jsp?mol=<%=mol%>&format=<%=format%>"
width=200 height=200>
</body>
</html>
