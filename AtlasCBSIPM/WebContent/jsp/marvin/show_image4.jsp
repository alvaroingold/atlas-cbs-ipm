<html>
<body>
<%
String format = java.net.URLEncoder.encode("png:w200,h200,b32,#ffffff");
String molname = "caffeine.smi";
String filename =
java.net.URLEncoder.encode("C:\\Program Files\\Apache Tomcat 4.0\\webapps\\examples\\jsp\\marvin"+molname);
%>
<h1>Image generation example</h1>
<img src="generate_image.jsp?file=<%=filename%>&format=<%= format%>"
width=200 height=200 onclick='setMol("<%=molname%>")'>
<p>Caffeine</p>
<script language="JavaScript"
src="http://www.chemaxon.com/marvin/marvin.js"></script>
<script lanuage="JavaScript">
mview_name="mview";
mview_begin("http://www.chemaxon.com/marvin",200,200);
mview_end();

function setMol(filename) {
    if(document.mview != null) {
	document.mview.setM(0,filename);
    }
}
</script>
<form>
<input type="button" value="load mol" onClick='setMol("<%=molname%>")'>
</form>
</body>
</html>
