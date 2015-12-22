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

<CENTER><h1>How to use Atlas CBS server</h1></CENTER><BR>

<div id="contentbox">

        <div id="contenbox_title">
	<A NAME="top">Topic Index</A>
        </div>
        <div id="contentbox_stuff">
        <ul>
        <li><A HREF="#Browser">Browser support</A></li>
        <li><A HREF="#Installing">Installing Java Virtual Machine</li>
	<li><A HREF="#Display">Displaying maps</A></li>
        <li><A HREF="#Register">Register and Login</A></li>
        <li><A HREF="#Uploading">Uploading data</A></li>
        <li><A HREF="#Visor">Visualizing personal data</A></li>
        <li><A HREF="#Filter">Selecting compounds with filters</A></li>
        </ul>
	</div>
</div>
<hr>

<div id="contentbox">

        <div id="contenbox_title">
        <A NAME="Browser">Browser support</A>
        </div>
        <div id="contentbox_stuff">
<p>Due to the use of JavaScript based technologies and some CSS features, the list of supported browsers by AtlasCBS server is:</p>
<ul>
<li>Firefox 3.5 or newer</li>
<li>Internet Explorer 8.0 or newer</li>
<li>Chrome 14 or newer</li>
</ul>
<CENTER>	<A HREF="#top">Return index</A></CENTER>
	</div>
</div>

<div id="contentbox">

        <div id="contenbox_title">
        <A NAME="Display">Displaying maps</A>
        </div>
        <div id="contentbox_stuff">
<p>In order to display maps you need to select first a <b>Data source</b> within the <b>Map Viewer</b> tab in the main menu.</p>
<p>If you already registered and are logged in with your user account, there will be four data sources available:</p>
<ol>
<li>BindingDB</li>
<li>PDBBind</li>
<li>ChEMBL</li>
<li>UserSet</li>
</ol>
<p>The UserSet will be saved when you log out for your next visit. You can have several user sets with different names (see the section on Uploading your data)</p>
<p>To map the content of a data base for a specific target and organism, select the corresponding items from the roll down tabs. Make sure to select the corresponding data for the values of the affinity constant that you wish (Ki, IC50 or KD). When you open the database, the window would tell you in parenthesis how many items are in the database for each affinity constant. All the other constants are not recommended and the server will have problems handling them,  because the strict definition of LEIs does not include them. Using  them to navigate the AtlasCBS is not recommended.</p>
<p>Once the data source and organism are selected you have to say <b>add source</b>. Depending upon the number of items and the speed of your internet connection and browser,  the table in the upper right-hand box will populate with data at different times.  Starting entries will be: Name, Smiles, X, Y. The two first columns should be obvious and the ones labeled X and Y, correspond to the X and Y variables of the plots. These variables will change depending upon the choice of LEIs selected to plot the efficiency maps. By default, the server will plot the nBEI vs. NSEI plot.  Different variables (i.e., LEIs) can be used by modifying the axes in the <b>Graph Controls</b> tab.  It is recommended that after the BEI vs. SEI (y,x) plot you try the nBEI vs. NSEI, where the target:ligand complexes available from the database for the selected target will map along lines defined by NPOL. From the definitions, this number is the number of N,O atoms in the ligand and can be used to follow the polarity of the ligands counterclockwise in the angular coordinate (slope of the lines).</p>
<p>With the map in your window, now is the time for exploration. 
<br>
 What are your compounds interest?</p>

<p>You can select a compound with the mouse click and it will appear on the screen/map with the name given on the table and it will be included in the <b>selection</b> list. If you click there, the structure of the compound will appear on the window below. </p>
<p>The scales of the axes can be changed with the graph controls, and also you can select an area of the map for further, more detailed exploration. You can also ‘duplicate’ your current map and then change the variables to explore other Efficiency maps simultaneously: BEI vs. SEI compared with nBEI vs. NSEI. At this point you should be ready to navigate within the content of the database or your own data (see below) to begin asking questions regarding the polarity, efficiency of your compounds and the options you might have to improve their physico-chemical properties and efficiencies within the same or different series. </p>

<p>We welcome your suggestions as to future improvements of this beta version.</p>
<CENTER>        <A HREF="#top">Return index</A></CENTER>
        </div>
</div>



<div id="contentbox">

	<div id="contenbox_title">
	<A NAME="Register">Register and Login</A>
	</div>

	<div id="contentbox_stuff">
<p>In order to upload your own data to the server, you have to register and login. This is the only way to keep the data private and to avoid mixing data from different users.</p>

<p>To register click Register now in the upper-right corner or in the link you can see in the main page introduction.</p>

<p>You will need an e-mail address, a simple password and to accept the terms of the service presented in the page.</p>

<p>After registration, you will be able to login in the system to upload your own datasets.</p>
<CENTER><IMG SRC="imgs/login_help.png" align="center" HEIGHT="300" WIDTH="80%"><BR>
<I>Example of login screen</I>
</CENTER>
<br>
<CENTER>        <A HREF="#top">Return index</A></CENTER>
<br>
<br>
<br>
<br>
<br>

	</div>

</div>
<div id="contentbox">
        <div id="contenbox_title">
	<A NAME="Uploading">Uploading data</A>

        </div>
        <div id="contentbox_stuff">
<p>Once you have login in the server, a tab with the title Manage data will appear instead of the tab login.
This section is the private database manager.</p>

<CENTER><IMG SRC="imgs/uploading_help1.png" HEIGHT="300" WIDTH="80%"><BR>
<I>Private database manager</I>
</CENTER>


<p>Before uploading anything we should create a new target and associate it to an organism to generate a data source. To do so, enter  the name of the target in the text box following the Target combobox and press Add Target. If no error happens you should see an 'OK' label. Then select the corresponding organism from the combobox. This data source will be available at the Map visor under private user database. 
However, you also can use the default target name 'Default' with the default organism 'Default'.<br>
Once you have created the target and relate it to an organism, you can proceed to upload the data.</p>

<CENTER><IMG SRC="imgs/uploading_help2.png" HEIGHT="200" WIDTH="80%"><BR>
<I>Private database manager</I>
</CENTER>


<p>Make sure your data file meets the following requisites:</p>
<ul>
<li>CSV format separated with semicolon</li>
<li>No titled accepted and no “or“ to separate text. An example dataset is provided for convenience.</li>
<li>Only four columns are allowed in this order:  Name;SMILES;Constant type;Valuea</li>
<li>Constant type should be one of the following: Kd, Ki or IC50 only.</li>
<li>Affinity values (Kd, Ki or IC50) values are expected in nM. Currently, these are the only affinity values are accepted. The reason for this restriction is that the variables used for the abscissa and ordinate of the plots (x,y), the various Ligand Efficiency Indices (LEIs) are defined only in terms of the those three variables. <b>Using other affinities (or even inhibitions values, i.e., Percent inhibitions) is not consistent with the current definitions of the LEIs.</b> </li>
</ul>
<p>The  file can be prepared from Excel spreadsheets using the <i><b>Save As</b></i>; function or from Openoffice.org/Libroffice.org using the same function.</p>

<p>Excel exports can be directly inserted. OpenOffice.org adds quotes to text and have to be removed or avoided in the Save As dialog.</p>

<p>Now Press <b><i>Upload a file</i></b> and select your CSV file from your PC.</p>
<p>The system will try to process the file and add the contents to the database. If an error happens, a label with the word <b><i>Failed</i></b> will appear. This failure can be partial, if some SMILES strings are not well formatted this does not imply that the rest of the data have failed to be inserted. Examine the resulting table of values with corresponding four basic columns: Name,SMILES,Constant type, Value.</p>

<p>By default the system calculates the initial set of LEIs: SEI, BEI, NSEI, NBEI, nBEI and mBEI, as defined in the publications.</p>

<p>Different variables and different efficiency planes can be produced using the Graph controls tab.</p>
<p><b>Among them, the easiest way to check that everything went well is to plot nBEI vs. NSEI by selecting these variables for y and x respectively. 
These choice of variables results in very easy to interpret maps where the various data points (each point a target: ligand complex) line up along lines of slope equal to NPOL. This corresponds to the number of polar atoms (N+O, so that in can be related to one of the points in the Ro5). More hydrophobic compounds (smaller number of NPOL) line along lines of lines of slope 1,2,3 etc. More polar compounds will plot along lines of steeper slopes, clustering towards the vertical axis.  By changing the scale of the axis you can explore different parts of the maps, as you would do in an Atlas by selecting different scales, or regions of the map. </b></p>

<p>Automatically a list of the inserted molecules will be displayed under the button.<br>
These molecules can be modified and deleted just clicking in the table. Also an individual data point can be added using the button <b><i>Add</i></b></p>
</p>
<CENTER><IMG SRC="imgs/uploading_help3.png" HEIGHT="400" WIDTH="80%"><BR>
<I>Data uploaded to the server</I>
</CENTER>
<br>
<CENTER>        <A HREF="#top">Return index</A></CENTER>
<br>
<br>
<br>
<br>
        </div>

</div>


<div id="contentbox">
        <div id="contenbox_title">
        <A NAME="Visor">Visualizing your own data</A>

        </div>
        <div id="contentbox_stuff">
<p>In the Map Visor, if you are logged in the server, under the Data source tab, you can change the Database to the <i><b>UserSet</b></i>. Under this database, all sets uploaded will appear categorized by target and organism, and can be added as the other sources from BindingDB and PDBBind.</p>
<CENTER><IMG SRC="imgs/uploading_help4.png" HEIGHT="400" WIDTH="80%"><BR>
<I>Data source tab in the visor</I>
</CENTER>
<p>You also can mix compounds from different sources and compare data from BindingDB with your sets.</p>
<CENTER><IMG SRC="imgs/uploading_help5.png" HEIGHT="400" WIDTH="80%"><BR>
<I>Mixing data</I>
</CENTER>
<br>
<CENTER>        <A HREF="#top">Return index</A></CENTER>
<br>
<br>
        </div>

</div>


<div id="contentbox">
        <div id="contenbox_title">
        <A NAME="Filter">Selecting compounds with filters</A>

        </div>
        <div id="contentbox_stuff">
<p>In the Map Visor, under the <b>Filters</b></i> tab you should see the different options:</p>
<ol>
<li><b>Name</b>: Search for compounds with the given word in the name and add them to the selection.</li>
<li><b>SMILES</b>: Depending on the viewer this function has different behaviour. In the Java applet viewer this option accepts SMARTS parsing throught the CDK module, and complex queries are possible. In the new viewer, this options reduces to a simple string matching.</li>
<li><b>X-Y ranges:</b>: These options select compounds within a range of values.</li>
<li><b>Similarity</b>: This option is only available in the Java applet viewer. This module allows the selection of compounds with a similarity with a query is above a cut-off. The similarity measure is done with the Tanimoto coefficient and the CDK fingerprints.</li>
</ol>
All the filters append the compounds which match the requirements to the selection list.
<br>
<CENTER>        <A HREF="#top">Return index</A></CENTER>
<br>
<br>
        </div>

</div>



<div id="contentbox">
        <div id="contenbox_title">
        <A NAME="Installing">Installing JAVA plugin</A>
        </div>
        <div id="contentbox_stuff">
<p>At this moment, to visualize the data in interactive manner, you need to run locally a Java Applet. This Java Applet requires an installed Java Virtual Machine (JVM) 1.6 in your PC.</p>
<p>Please ensure that you have installed the JVM 1.6 or newer and the your browser is configured to use it properly.</p>
<p>You can download the lastest java package <A HREF="http://java.oracle.com">here</A></p>
<CENTER>        <A HREF="#top">Return index</A></CENTER>

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
<br>(C) 2011. University of Illinois at Chicago (UIC).
<br>(C) 2013. Lastest revision Feb-May-2013. by ACC and C.A-Z during his stay at UAH.
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
