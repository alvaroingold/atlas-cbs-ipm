<html>
<head>
<title>AtlasCBS server</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="u.css" media="all" rel="stylesheet" type="text/css" />
   <script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>

</head>
<body bgcolor="#ddd">

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
<li><A HREF="viewer.jsp"><span>Map viewer</span></A></li>
<li><A HREF="manage.jsp"><span>Manage data</span></A></li>
<li><A HREF="help.jsp"><span>Help</span></A></li>
<li><A HREF="about.jsp"><span>About</span></A></li>
</div>

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
<li><A HREF="viewer.jsp"><span>Map viewer</span></A></li>
<li><A HREF="login.jsp"><span>Login</span></A></li>
<li><A HREF="help.jsp"><span>Help</span></A></li>
<li><A HREF="about.jsp"><span>About</span></A></li>
</div>
<%
}
%>
<div id="content">
<div id="content-intro" align="CENTER">


<CENTER><h1>About</h1></CENTER><BR>


<div id="bigcontentbox">
        <div id="contenbox_title">
        Development
        </div>
        <div id="contentbox_stuff">
Implementation and development of this AtlasCBS platform has been carried out by Alvaro Cortes Cabrera (acortes at cbm.uam.es), Cele Abad-Zapatero, Federico Gago and Antonio Morreale. Initial development started at the Bioinformatics Unit of the Centro Biologia Molecular Severo Ochoa (CBMSO) of the Universidad Autonoma de Madrid (UAM), sponsored by a grant to C.A-Z from the Spanish Ministry of Science and Technology. Further development was sponsored by the Foundation Giner
de los Rios, while C.A-Z was visiting professor at the University of
Alcala de Henares (UAH)(February-June, 2013).
        </div>

</div>

<div id="bigcontentbox">

	<div id="contenbox_title">
	<A HREF="http://ub.cbm.uam.es">Bioinformatics Unit</A>
	</div>

	<div id="contentbox_stuff">
<A HREF="http://www.cbm.uam.es"><IMG SRC="cbmso.png" ALIGN="LEFT"></A>
The Bioinformatics Unit of the CBMSO was founded in 2002 by Dr. Angel Ramirez Ortiz. After his untimely and premature loss in May 2008, we were recognized as an emerging group in April 2009 under the leadership of Dr. Ugo Bastolla.  
Our group is active in the field of the computational structural biology of proteins. Our main research lines are the computational study of protein structures, stability and evolution, drug design by docking and virtual screening, and theoretical ecology, in particular applied to bacterial communities. We also provide Bioinformatics facility to the CBMSO.
The drug design unit was directed first by Dr. Antonio Morreale and this is where the initial design and development of the AtlasCBS took place. The development continued at the University of Alcala de Henares (UAH) in the department of Pharmacology under Prof. Federico Gago Badenas.
<p>
Our close collaborators in developing software and applications for drug discovery are:
<p>

	</div>


</div>
<div id="bigcontentbox">
        <div id="contenbox_title">
	<A HREF="http://www2.uah.es/farmamol/">Molecular Modeling Lab</A>
        </div>
        <div id="contentbox_stuff">
<A HREF="http://www.uah.es"><IMG SRC="uah.png" ALIGN="LEFT"></A>
<br>
        Located in the School of Medicine of University of Alcala in Madrid. Part of the Pharmacology Department and directed by Federico Gago.
<br>
<br>
<br>
<br>
        </div>

</div>

<div id="bigcontentbox">
        <div id="contenbox_title">
        <A HREF="http://www.uic.edu/depts/cphb/CPB/Welcome.html">Center for Pharmaceutical Biotechnology. University of Illinois at Chicago</A>
        </div>
        <div id="contentbox_stuff">
<A HREF="http://www.uic.edu/"><IMG SRC="logo_cele.jpg" WIDTH=400 HEIGHT=80 ALIGN="LEFT"></A>
<br>
Center for Pharmaceutical Biotechnology is part of the University of Illinois at Chicago. You can find the original papers explaining the background on the Ligand Efficiency Index (LEI) concepts and applications in the web page of <A HREF="http://www.uic.edu/labs/caz/index.html">Dr. Cele Abad-Zapatero</A> 
<br>
<br>
<br>
<br>

        </div>
</div>


</div>
</div>

</div>
</div>
<div id="footer">
<p>AtlasCBS - A tool to explore chemico-biological database
<p>
<p>(C) 2011. Unidad de Bioinformatica. Centro de Biologia Molecular "Severo Ochoa". 
<BR>Consejo Superior de Investigaciones Cientificas - Universidad Autonoma de Madrid.
<br>(C) 2011. Laboratorio de Modelado Molecular. Universidad de Alcala de Henares.
<br>(C) 2011. Univeristy of Illinois at Chicago (UIC).
</div>

<script type="text/javascript">
try{

var pageTracker = _gat._getTracker("UA-33092043-1");
pageTracker._trackPageview();

} catch(err) {}
</script>


</body>
</html>
