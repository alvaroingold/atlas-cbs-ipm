<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>AtlasCBS server</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="u.css" media="all" rel="stylesheet" type="text/css" />
<link href="jquery-ui.css" media="all" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="jquery.min.js">  </script>
<script type="text/javascript" src="jquery-ui-1.8.16.custom.min.js">  </script>
<script type="text/javascript" src="jquery.cookie.js">  </script>
   <script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>

</head>
<body>

<%
session.getId();
String aut = (String)session.getAttribute("authorized");
if( aut == "yes")
{
%>
<div id="top-menu" align="right">Hi <%=session.getAttribute("name")%> <A HREF="logout.jsp">(Logout)</A></div>
<div id="header">
<CENTER><IMG SRC="title1.png">  </CENTER>
</div>
<p>
<div id="tabs">
<li><A HREF="intro.jsp"><span>Main</span></A></li>
<li><A HREF="java.jsp"><span>Map viewer</span></A></li>
<li><A HREF="manage.jsp"><span>Manage data</span></A></li>
<li><A HREF="help.jsp"><span>Help</span></A></li>
<li><A HREF="about.jsp"><span>About</span></A></li>
</div>
<div id="content">
<TABLE>
<TR><TD>
<CENTER><APPLET id="apl" code="atlas2/Main" archive="viewer.jar"  WIDTH = 1300 HEIGHT = 700><PARAM name="basename" value="http://ub.cbm.uam.es:8080/atlasr9"><PARAM name="id_user" value=<%= session.getAttribute("id_user")%>> 
</APPLET></CENTER>
</TD></TR>
</TABLE>


<% // Here we are if we do no have an username
}else{
%>
<div id="top-menu" align="right">Invited User. <A HREF="register.jsp">Register now!</A></div>
<div id="header">
<CENTER><IMG SRC="title1.png">  </CENTER>
</div>
<p>
<div id="tabs">
<li><A HREF="intro.jsp"><span>Main</span></A></li>
<li><A HREF="java.jsp"><span>Map viewer</span></A></li>
<li><A HREF="login.jsp"><span>Login</span></A></li>
<li><A HREF="help.jsp"><span>Help</span></A></li>
<li><A HREF="about.jsp"><span>About</span></A></li>
</div>
<div id="content">
<TABLE>
<TR><TD>
<CENTER><APPLET id="apl" code="atlas2/Main" archive="viewer.jar"  WIDTH = 1300 HEIGHT = 720><PARAM name="basename" value="http://ub.cbm.uam.es:8080/atlasr9/"><PARAM name="id_user" value="0">
</APPLET></CENTER>
</TD></TR>
</TABLE>

<%
}
%>

<p><center>Have fun!</center>
</div>


<div id="footer">
<p>AtlasCBS - A tool to explore chemico-biological database
<p>
<p>(C) 2011. Unidad de Bioinformatica. Centro de Biologia Molecular "Severo Ochoa". 
<BR>Consejo Superior de Investigaciones Cientificas - Universidad Autonoma de Madrid.
<br>(C) 2011. Laboratorio de Modelado Molecular. Universidad de Alcala de Henares.
<br>(C) 2011. University of Illinois at Chicago (UIC).
<br>(C) 2013. Revised Feb-June 2013 during visit to UAH by CAZ.
<br>(C) 2015. Unidad de Bioinformatica. Institut Pasteur de Montevideo.

</div>

<script type="text/javascript">
try{

var pageTracker = _gat._getTracker("UA-33092043-1");
pageTracker._trackPageview();

} catch(err) {}
</script>


</body>
</html>
