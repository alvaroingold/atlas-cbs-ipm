<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>

<%

session.getId();
String aut = (String)session.getAttribute("authorized");
String id_origin = "";
int dupliwindow = 0;
String duplicate = request.getParameter("duplicate");
if( duplicate != null)
{

	dupliwindow = 1;
}

if( aut == "yes")
{
	id_origin = (String) session.getAttribute("id_origin");
}

%>


<html>
<head>
<title>AtlasCBS server</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link rel="icon" href="favicon.ico" type="image/x-icon">
<link rel="shortcut icon" href="favicon.ico" type="image/x-icon">
<link href="u.css" media="all" rel="stylesheet" type="text/css" />
<link href="demo_table.css" media="all" rel="stylesheet" type="text/css" />
<link href="demo_validation.css" media="all" rel="stylesheet" type="text/css" />
<link href="jquery-ui.css" media="all" rel="stylesheet" type="text/css" />
<link href="jquery-ui-1.7.2.custom.css" media="all" rel="stylesheet" type="text/css" />
<link href="jquery.jqplot.css" media="all" rel="stylesheet" type="text/css" />

<!--[if lt IE 10]><script language="javascript" type="text/javascript" src="excanvas.js"></script><![endif]-->

   <script type="text/javascript" src="jquery.min.js">  </script>
   <script type="text/javascript" src="jquery.dataTables.min.js">  </script>
   <script type="text/javascript" src="jquery.jqplot.js">  </script>
   <script type="text/javascript" src="jqplot.cursor.min.js">  </script>
   <script type="text/javascript" src="jqplot.highlighter.js">  </script>
   <script type="text/javascript" src="jqplot.pointLabels.min.js"></script>
   <script type="text/javascript" src="jqplot.canvasTextRenderer.min.js"></script>
   <script type="text/javascript" src="jqplot.canvasAxisLabelRenderer.min.js"></script>
   <script type="text/javascript" src="jquery-ui-1.8.16.custom.min.js">  </script>
   <script type="text/javascript" src="jquery.cookie.js">  </script>

   <link href="jquery-ui.css.1" rel="stylesheet" type="text/css"/>

   <script type="text/javascript" src="jquery.colorpicker.js"> </script>
   <script type="text/javascript" src="jquery.ui.colorpicker-en.js"> </script>

   <script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>

   <script type="text/javascript" src="viewer.js">  </script>

<link href="jquery.colorpicker.css" rel="stylesheet" type="text/css"/>

   <script type="text/javascript">


                $( function() {

                $('#ColorSource').colorpicker({
                                        onClose: function(hex, rgba, inst) {
                                                                console.log('onClose: '+hex+', ('+rgba.r+','+rgba.g+','+rgba.b+','+rgba.a+')');
                                                        },
                                        onSelect: function(hex, rgba, inst) {
                                                                console.log('onSelect: '+hex+', ('+rgba.r+','+rgba.g+','+rgba.b+','+rgba.a+')');
                                                        },
                                        showOn: 'both',
                                        showSwatches: true,
                                        showNoneButton: true,
                                        buttonColorize: true,
                                        limit: 'websafe',
                                        parts: 'full',
                                        regional: 'nl',
                                        altProperties: 'background-color,color'
                                });
    
            });



<%
	if( aut == "yes")
	{
%>
       var windowOnload=window.onload||load_stuff(<%= session.getAttribute("id_user")%>,"<%= request.getParameter("PDB")%>");
       window.onload=function(){load_stuff(<%= session.getAttribute("id_user")%>,"<%= request.getParameter("PDB")%>"); };

<%
	}else{
%>
       var windowOnload=window.onload||load_stuff("","<%= request.getParameter("PDB")%>");
       window.onload=function(){load_stuff("","<%= request.getParameter("PDB")%>"); };


<%
}
%>




//       window.onload = load_combos(<%=id_origin%>);

$(document).ready(function(){

   $(".tabLink").each(function(){
      $(this).click(function(){
        tabeId = $(this).attr('id');
        $(".tabLink").removeClass("activeLink");
        $(this).addClass("activeLink");
        $(".tabcontent").addClass("hide");
        $("#"+tabeId+"-1").removeClass("hide")   
        return false;	  
      });
    });  

var pageTracker = _gat._getTracker("UA-33092043-1");
pageTracker._trackPageview();


$.jqplot.eventListenerHooks.push(['jqplotClick', myClickHandler]);
$.jqplot.eventListenerHooks.push(['jqplotMouseMove', myOverHandler]);


<%
        if( dupliwindow == 1)
        {%>
                load_view_state();

<%      }else{ %>

	setInit();
<%	}

%>


});


   </script>


</head>
<body>

<% 

if ( aut == "yes")
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
<%
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
<CENTER><H2>Map viewer</H2></CENTER>
<div id="left_div" style="height:520px;width:520px">

<div id="chart" style="height:520px;width:520px;"></div>
<input type="button" value="Clear selection" name="clearannotations" id="clearannotations" onClick="clear_selection();">
<HR>
<table width="350" align= "center" border="1" RULES="ROWS" FRAME="HSIDES" cellspacing="0" cellpadding="2" style="font-size: 0.7em;"> 
  <tr> 
    <td><p><strong>Name</strong></p></td> 
      <td><p><strong>Definition</strong></p></td> 
  </tr>
 <tr> 
    <td><p>LEH</p></td> 
    <td><p>= -RTln(Kd)[or (Ki)]/NHA; Hopkins' LE; LE=0 if no
    Ki,Kd are available. </p></td> 
  </tr> 
  <tr> 
    <td><p>BEI</p></td> 
    <td><p>p(Ki), p(Kd), or p(IC<sub>50</sub>)/MW(kiloDa) </p></td> 
  </tr> 
  <tr> 
    <td><p>SEI</p></td> 
    <td><p>p(K<sub>i</sub>), p(K<sub>d</sub>), or p(IC<sub>50</sub>)/(PSA/100 &Aring;<sup>2</sup>)</p></td> 
    <tr> 
    <td><p>NSEI</p></td> 
    <td><p>NSEI = -log<sub>10</sub> Ki/(NPOL) = pKi/NPOL(N,O)</p></td> 
    <tr> 
    <td><p>NBEI</p></td> 
    <td><p>NBEI= -log<sub>10</sub> Ki/(NHA)= pKi/(NHA)</p></td> 
    </tr> 
    <tr> 
    <td><p>nBEI</p></td> 
    <td><p>nBEI= -log<sub>10</sub>[(Ki/NHA)]</p></td> 
    </tr> 
    <tr> 
    <td><p>mBEI</p></td> 
    <td><p>mBEI=-log<sub>10</sub>[(Ki/MW)]</p></td> 
   </tr> 
   <tr> 
    <td><p>LEP</p></td> 
    <td><p> = -RTln(Kd)[or(Ki)]/NPOL; Hopkins' LE extension for NPOL by 	CAZ (unpublished). LEP,LEH =0 if no Ki,Kd are available </p></td> 
	</tr>
</table> 
<BR>
When the plane represents nBEI vs. NSEI (y vs. x), the slope of the lines corresponds to the number of polar atoms (N+O) and the intersect is log<sub>10</sub>(Number of non-hydrogen atoms). See Viewer tab for more details.

</div>
<div id="right_div">

<div class="tab-box" id="innertabs"> 

<%
	if( request.getParameter("PDB") != null)
	{
%>
    <a href="javascript:;" class="tabLink activeLink" id="cont-0">Selection</a>
    <a href="javascript:;" class="tabLink" id="cont-1">Viewer</a> 
    <a href="javascript:;" class="tabLink" id="cont-2">Data</a> 
    <a href="javascript:;" class="tabLink " id="cont-3">Filters</a> 
    <a href="javascript:;" class="tabLink " id="cont-5">Exports</a>                 
<%
	}else{
%>
    <a href="javascript:;" class="tabLink" id="cont-0">Selection</a>
    <a href="javascript:;" class="tabLink" id="cont-1">Viewer</a>
    <a href="javascript:;" class="tabLink activeLink" id="cont-2">Data</a>
    <a href="javascript:;" class="tabLink " id="cont-3">Filters</a>
    <a href="javascript:;" class="tabLink " id="cont-5">Exports</a>
<%
	}
%>
</div>

  <div class="tabcontent paddingAll hide" id="cont-1-1">
<br>
Session management
<hr>
<INPUT TYPE="button" id="save_view" onClick="save_view_state();" value="Save session">
<INPUT TYPE="button" id="clear_view" onClick="clear_view_state();" value="Clear session">
<INPUT TYPE="button" id="load_view" onClick="load_view_state();" value="Load session">
<INPUT TYPE="button" id="duplicate_view" onClick="duplicate_window();" value="Duplicate window">
<br>
<br>
Space Coordinates
<hr>
<table>
<tr>
<td>X:</td><td><select id="x_type" name="x_type" onChange="update_x_coord();">
<option value="1">LEH</option>
<option value="2">BEI</option>
<option value="3">SEI</option>
<option value="4" selected>NSEI</option>
<option value="5">NBEI</option>
<option value="6">nBEI</option>
<option value="7">mBEI</option>
<option value="8">LEP</option>

<INPUT TYPE="button" id="view_lei_desc" onClick="LEIsHelp();" value="See descriptions"></td> 
</tr>
<tr>
<td>Y:</td><td><select id="y_type" name="y_type" onChange="update_y_coord();">
<option value="1">LEH</option>
<option value="2">BEI</option>
<option value="3">SEI</option>
<option value="4">NSEI</option>
<option value="5">NBEI</option>
<option value="6" selected>nBEI</option>
<option value="7">mBEI</option>
<option value="8">LEP</option>
</select>
</td>
</tr>
</table>
<br>
<hr>
<INPUT TYPE="button" id="change_java" onClick="load_java_applet();" value="Use JAVA viewer">
<p>
</div>

<%
        if( request.getParameter("PDB") != null)
        {
%>
	  <div class="tabcontent paddingAll" id="cont-0-1">
<%
	}else{
%>

  <div class="tabcontent paddingAll hide" id="cont-0-1">
<%
	}
%>

<center>Current Selection</center>
<div id="selection_space" style="height: 150px">
<hr>
<TABLE ID="selectionTable" cellpadding="0" cellspacing="0" border="0" class="display">
        <thead>
                <tr>
                        <th>Name</th>
                        <th>SMILES</th>
                        <th>X</th>
                        <th>Y</th>
                </tr>
        </thead>
<tbody><tr><td></td><td>No data selected</td></tr></tbody>


</TABLE>
</div>

<br> <center>Selected molecule</center> <hr> <div id="image_block" align="center" styele="text-align: center;"> <span id="mol_info" style="text-align: center;"> </span>
 <span id="mol_props" style="text-align: center;"></span> </div> 

</div>


<%
        if( request.getParameter("PDB") != null)
        {
%>
  <div class="tabcontent paddingAll hide" id="cont-2-1">
<%
	}else{
%>
  <div class="tabcontent paddingAll " id="cont-2-1">
<%
	}
%>

<table border=0>
<tr>
  <td><span id="descripcion">Database: </span></td>
  <td><select id="database" name="database" onChange="return databaseSelected();" width="400" style="width: 400px; height=0.8em" >
  </select></td>
</tr>
<tr>
  <td><span id="descripcion">Target: </span></td>
  <td><select id="target" name="target" onChange="return targetSelected();" width="400" style="width: 400px; height=0.8em" >
  </select></td>
</tr>
<tr>
<td>
<span id="descripcion">Organism: </span></td>
<td>  <select id="organism" name="organism" onChange="return organismSelected();" width="400" style="width: 400px">
  </select></td>
</tr>
<tr>
<td>
<span id="descripcion">Type: </span></td>
<td>  <select id="phs" name="phs" onChange="return phsSelected();" width="400" style="width: 400px">
  </select></td>
</tr>
<tr>
<td></td>
<td> <input type="button" id="addSource" name="addSource" value="Add Source" onClick="return addSource();"> <input type="button" id="removeSource" name="removeSource" value="Remove Source" onClick="return removeSource();"> <input type="button" id="hideSource" name="hideSource" value="Show/Hide Source" onClick="return hideSource();">
 </td>
</tr>
<tr>
<td><table>
<tr><td>Source color</td></tr>
<tr><td><input type="color" id="ColorSource" value="fe9810"/></td></tr>
<tr><td><input type="button" id="setColor" name="setColor" value="Set color" onClick="return changeColor();"></td></tr>
</table>
</td><td>
<select id="datasources" name="datasources" size="10" style="width: 400px">
</select>

</td>
</tr>
<tr>
<td><span id="descripcion">From PDB: </span></td>
<td><input type="text" id="pdbcode" name="pdbcode" value="2WI2"> <input type="button" id="addPDB" name="addPDB" value="Add PDB source" onClick="return addPDBsource();"></td>
</tr>
</table>
<br>
<center>Data</center>
<hr>
<div id="data_space">
<table cellpadding="0" cellspacing="0" border="0" class="display" id="lei_data">
        <thead>
                <tr>
                        <th>Name</th>
                        <th>SMILES</th>
                        <th>X</th>
                        <th>Y</th>
                </tr>
        </thead>
        <tbody>

        </tbody>
</table>
</div>
  </div>

 <div class="tabcontent paddingAll hide" id="cont-3-1">
<br>
<table>
<tr><td>Name</td><td><input type="text" name="Namefiltering" id="Namefiltering"></td></tr>
<tr><td>SMILES</td><td><input type="text" name="SMILESfiltering" id="SMILESfiltering"></td></tr>
<tr><td>From X <input type="text" name="FromXfiltering" id="FromXfiltering"></td><td>To X <input type="text" name="ToXfiltering" id="ToXfiltering"></td></tr>
<tr><td>From Y <input type="text" name="FromYfiltering" id="FromYfiltering"></td><td>To Y <input type="text" name="ToYfiltering" id="ToYfiltering"></td></tr>
<tr><td>Slope (NPOL in NSEI/nBEI graphs)</td><td><input type="text" name="NPOLfiltering" id="NPOLfiltering"></td></tr> 
</table>
<input type="button" value="Set" name="btnfilters" id="btnfilters" onClick="set_filters();">



  </div>

 <div class="tabcontent paddingAll hide" id="cont-4-1" >
<br>
<div class="subset_container">
<table id="subset_table">
<tr>
<td>User defined subsets</td>
</tr>
<tr><td><select id="subsetsources" name="subsetsources" size="10" style="width: 400px">
</select></td></tr>
<tr><td> <input type="button" value="Remove subset"> <input type="button" value="Hide/Show subset">  </td></tr>
</table>
</div>
</div>
  
 <div class="tabcontent paddingAll hide" id="cont-5-1">
<br>
Here you can find exporting options for your data in the AtlasCBS server.
<hr>
<table>
<tr><td><input type="button" id="exportIMG" name="exportIMG" value="Export map as image" onClick="return exportIMG();"></td><td>Generate a static version of the current map</td></tr>
<!-- tr><td><input type="button" id="exportIMG" name="exportIMG" value="Generate session report" onClick="return exportIMG();"></td><td>Generate a PDF 'session report' to for storage</td></tr -->
<tr><td><input type="button" id="exportIMG" name="exportIMG" value="Download session data as CSV" onClick="return downloadCSV();"></td><td>Download all the data loaded in the session as a CSV file importable in any spreadsheet software.</td></tr>
</table>

</div>

  



</div>


<div id="footer">
<td><p><strong>AtlasCBS - A tool to explore and navigate Chemico-Biological Space</strong></p></td>
<p>
<p>(C) 2011. Unidad de Bioinformatica. Centro de Biologia Molecular "Severo Ochoa". 
<BR> Consejo Superior de Investigaciones Cientificas - Universidad Autonoma de Madrid.
<br>(C) 2011. Laboratorio de Modelado Molecular. Universidad de Alcala de Henares.
<br>(C) 2013. Revised version produced by A.C-C and C.A-Z during his stay at the UAH, Feb-June, 2013.
<br>(C) 2015. Unidad de Bioinformatica. Institut Pasteur de Montevideo.

</div>


</body>
</html>
