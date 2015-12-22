<html>
<body>
<%
String format = java.net.URLEncoder.encode("png:w200,h200,b32,#ffffff");
String filename = java.net.URLEncoder.encode("caffeine.smi");
%>
<img src="http://bioinf6:8080/examples/jsp/marvin/generate_image.jsp?file=<%=filename%>&format=<%= format%>"
width=200 height=200>
</body>
</html>
