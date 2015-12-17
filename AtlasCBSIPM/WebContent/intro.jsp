<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>


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

<% // Here we are if we do not have an username
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
<div id="content-intro" align="center">

<CENTER><h1>Welcome to the AtlasCBS server!</h1></CENTER><BR>

<div id="contentbox">

	<div id="contenbox_title">
		Introduction
	</div>

	<div id="contentbox_stuff">
<IMG SRC="intro.png" align="left">

<p>AtlasCBS is a tool that allows you to explore chemico-biological space using Ligand Efficiency Indices (LEIs) as variables. This unique set of variables permits a graphical representation of the content of databases that contain affinity data (SAR databases such as PDBBind, BindingDB) in an atlas-like fashion. The server allows you to see the content of the database(s) of choice as pages in a map-like environment with different variables and scales. The content of the databases is displayed in two-dimensional pages where the angular coordinate is related to the chemical composition of the ligand(s) and the radial coordinate is related to affinity of the ligand(s) towards the specific target(s). Details can be found in the specific <A HREF="http://www.uic.edu/labs/caz/discovery/index.html">publications</A>.</p>
<p>
The purpose of this server is to help you navigate in Chemico-Biological Space (CBS) so that you can examine the affinity and polarity of the chemical matter available for certain target(s) and possibly compare against what your own laboratory can produce, or what your competitors are designing. This comparison can be extremely useful and can allow you to design strategies to optimize your drug-discovery projects in your academic or private research.</p>
<p>Currently, available databases include BindingDB (19/05/2012), PDBBind v2011, ChEMBL (v16) and private, user uploaded databases. In order to use your own private databases you have to register <A HREF="register.jsp">here</A></p>
<p>To start using the site click on <A HREF="viewer.jsp"><span>Map viewer</span></A> in the menu. The JAVA viewer is available <A HREF="applet.jsp">here</A>.</p>
<p>Some additional references can be found below.</A></p>
<ol>
<li><A HREF="http://dx.doi.org/10.1002/minf.201000161">Abad-Zapatero, C.; Blasi, D.<i>Ligand Efficiency Indices (LEI): More than a Simple Efficiency Yardstick</i> (2011). <i>Molecular Informatics</i>. 30 (2-3), 122-132.</A></li>
<li><A HREF="http://dx.doi.org/10.1002/minf.201000158">Christmann-Franck, S.; Cravo, D.; Abad-Zapatero, C.;<i>Time-Trajectories in Efficiency Maps as Effective Guides for Drug Discovery Efforts</i> (2011). <i>Molecular Informatics</i>. 30 (2-3), 137-144.</li>
<li><A HREF="http://dx.doi.org/10.1002/minf.201000157">Blasi, D.; Arsequell, G.; Valencia, G.; Nieto, J.; Planas, A.; Pinto, M.; Centero, N. B.; Abad-Zapatero, C.; Quintana, J.;<i>Retrospective Mapping of SAR Data for TTR Protein in Chemico-Biological Space Using Ligand Efficiency Indices as a Guide to Drug Discovery Strategies</i> (2011). <i>Molecular Informatics</i>. 30 (2-3), 161-167.</A></li>
</ol>
<br>
<b>Update:</b> Version.2.0. Several things have changed in this version compared to the earlier one posted at this site.
<ol>
           <li>The original LE defined by Hopkins (DDT, 2004) is now
              included in the variable set for x and y, and is named
              LEH (for 'Heavy Atom' and 'Hopkins').</li>
           <li>An additional LE related to polarity has been added:
              LEP = DeltaG/NPOL(N+O).
              This permits the representation of efficiency planes 
              LEH vs. LEP, which are equivalent to NBEI vs. NSEI.
              Any combination with the other LEIs is possible.</li>
           <li>The format of the 'Download session data as CSV' output file has been changed to include a title line containing:</li>
<pre>
               Name,SMILES,Type,Value,pKi,x-variable,y-variable.

              This output can be read by several programs
             (i.e. StarDrop) using the first line to label the column 
              headings.
</pre>
</ol>
===
             Further details can be found in the legend below the map
             and on the annotations within the viewer tabs.
<!--  This version installed at EBI. May 22, 2013.     -->
              
	</div>

</div>

<div id="contentbox">
        <div id="contenbox_title">
	Troubleshooting
        </div>
        <div id="contentbox_stuff">
<p>If you encounter problems in the use of the server please consult the <A HREF="help.jsp">help</A> section or these specific topics: <p>
	<ul>
	<li><A HREF="help.jsp#Browser">Browser support</A></li>
	<li><A HREF="help.jsp#Installing">Installing Java Virtual Machine</li>
        <li><A HREF="help.jsp#Display">Displaying maps</A></li>
	<li><A HREF="help.jsp#Register">Register and Login</A></li>
        <li><A HREF="help.jsp#Uploading">Uploading data</A></li>
        <li><A HREF="help.jsp#Visor">Visualizing personal data</A></li>
        <li><A HREF="help.jsp#Filter">Selecting compounds with filters</A></li>
	</ul>
        </div>

</div>

<div id="contentbox">
        <div id="contenbox_title">
        Suggestions, Feedback and Bugs
        </div>
        <div id="contentbox_stuff">
<IMG SRC="bug.png" HEIGHT=50 ALIGN="LEFT">We really appreciate your suggestions, comments, ideas or whatever insight you might have. Possible ways to improve the current server and new ideas that could be useful for the scientific community at large are welcome. Please send e-mails to <A HREF="mailto:acortes@cbm.uam.es">acortes at cbm.uam.es </A> or <A HREF="mailto:caz@uic.edu">caz at uic.edu</A>. We will be delighted to hear from you.
<br>
<br>
<br>
        </div>

</div>



<div id="contentbox">
        <div id="contenbox_title">
	Disclaimer

        </div>
        <div id="contentbox_stuff">
<IMG SRC="caveat.png" HEIGHT=100 ALIGN="LEFT">The developers of the platform disclaim all warranties with regard to this platform, web and software, included all implied warranties of merchantability and fitness. In no event shall the developers be liable for any special, indirect of consequential damages or any damages whatsoever resulting from loss of user, data or profits, whether in an action of contract, negligence or other tortuous action, arising out of or in connection with the use or performance of this platform, software and web. 
<br>
<br>
<br>
<br>
        </div>

</div>



</div>
</div>

<div id="footer">
<p>AtlasCBS - A tool to explore and navigate chemico-biological space
<p>
<p>(C) 2011. Unidad de Bioinformatica. Centro de Biologia Molecular "Severo Ochoa". 
<BR>Consejo Superior de Investigaciones Cientificas - Universidad Autonoma de Madrid.
<br>(C) 2011. Laboratorio de Modelado Molecular. Universidad de Alcala de Henares.
<br>(C) 2011. Univeristy of Illinois at Chicago (UIC).

<br> <p>(C) 2013. Revised version produced by C.A-Z during his stay at the UAH, Feb-June, 2013
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
