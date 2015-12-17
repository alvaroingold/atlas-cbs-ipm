<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>

<%
session.getId();
String aut = (String)session.getAttribute("authorized");
String id_origin = "";
if( aut == "yes")
{
	id_origin = (String) session.getAttribute("id_origin");
}

%>

<html>
<head>
<title>AtlasCBS server</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="u.css" media="all" rel="stylesheet" type="text/css" />
<link href="demo_table.css" media="all" rel="stylesheet" type="text/css" />
<link href="demo_validation.css" media="all" rel="stylesheet" type="text/css" />
<link href="jquery-ui.css" media="all" rel="stylesheet" type="text/css" />
<link href="jquery-ui-1.7.2.custom.css" media="all" rel="stylesheet" type="text/css" />


   <script type="text/javascript" src="st.js">  </script>
   <script type="text/javascript" src="jquery-1.6.2.js">  </script>
   <script type="text/javascript" src="jquery.dataTables.js">  </script>
   <script src="jquery.jeditable.js" type="text/javascript"></script>
   <script src="jquery-ui.js" type="text/javascript"></script>
   <script src="jquery.validate.js" type="text/javascript"></script>
   <script type="text/javascript" src="jquery.dataTables.editable.js">  </script>

   <script type="text/javascript" src="http://www.google-analytics.com/ga.js"></script>

   <script type="text/javascript">

       var windowOnload=window.onload||load_combos(<%=id_origin%>);
       window.onload=function(){load_combos(<%=id_origin%>);};

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
    });


   </script>


</head>
<body>

<%
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
<div id="content">

<CENTER><H2>Private database manager</H2></CENTER>




<table border=0>
<tr>
  <td><span id="descripcion">Target: </span></td>
  <td><select id="target" name="target" onChange="return targetSelected();" width="400" style="width: 400px; height=0.8em" >
  </select></td>
  <td><input type="text" id="NewTarget" name="NewTarget" value="">
  <input type="button" id="addTarget" name="addTarget" value="Add Target" onClick="return addTarget();"> <span id="addTarget_result"></span></td>
</tr>
<tr>
<td>
<span id="descripcion">Organism: </span></td>
<td>  <select id="organism" name="organism" onChange="return organismSelected();" width="400" style="width: 400px">
  </select></td>
</tr>
</table>

<p>

<hr>
<center>Upload User sets</center>
<hr>
<div class="tab-box" id="innertabs">
    <a href="javascript:;" class="tabLink activeLink" id="cont-1">CSV</a>
    <a href="javascript:;" class="tabLink " id="cont-2">SMILES</a>

</div>

  <div class="tabcontent paddingAll" id="cont-1-1">
<p>Upload CSV file</p>

<div id="file-uploader">
    <noscript>
        <p>Please enable JavaScript to use file uploader.</p>
    </noscript>
</div>
<p>Files should be in CSV (semicolon separated values) format in the following order:  Name; SMILES; constant type; value. Values should be in nM to be comparable with the other databases. No title accepted. </p>
<P>The file can be obtained from Excel spreadsheets using the Save As function or from Openoffice.org/Libroffice.org using the same function.</p>
</div>
  <div class="tabcontent paddingAll hide" id="cont-2-1">
<p>Upload SMILES file</p>
<div id="file-uploader_SMI">
    <noscript>
        <p>Please enable JavaScript to use file uploader.</p>
    </noscript>
</div>
<p>Files should contain one SMILES string per line with no name or descriptions.</p>
<p>Binding information will be generated randomly using Ki from uM to pM range.</p>
</div>

<div class="tabcontent paddingAll hide" id="cont-3-1">
<p>Upload SDF file</p>
<div id="file-uploader_SDF">
    <noscript>
        <p>Please enable JavaScript to use file uploader.</p>
    </noscript>
</div>
<p>SDF files should be in standard MDLv2000 version separating molecule using $$$$.</p>
<p>Binding information will be generated randomly using Ki from uM to pM range.</p>
</div>

<link href="fileuploader.css" rel="stylesheet" type="text/css" />
<script src="fileuploader.js" type="text/javascript"></script>
<hr>
<center>Data</center>
<hr>
<p>

 <form id="formAddNewRow" action="#" title="Add new record">
        <input type="hidden" name="id" id="id" rel="0" value="DATAROWID" />
        <input type="hidden" name="idtarget" id="idtarget" rel="1" value="-1" />
        <input type="hidden" name="idorganism" id="idorganism" rel="2" value="-1" />
        <label for="compound">Compound name</label><br />
        <input type="text" name="compound" id="compound" rel="3" />
        <br />
        <label for="smiles">SMILES</label><br />
        <textarea name="smiles" id="smiles" rel="4"></textarea>
        <br />
        <label for="type">Type</label><br />
        <select name="type" id="type" rel="5">
                <option>Kd</option>
                <option>Ki</option>
                <option>IC50</option>
                <option>Kd/Ki ratio</option>
                <option>&delta G<sub>0</sub></option>
        </select>
        <br />
        <label for="kvalue">Value</label><br />
        <textarea name="kvalue" id="kvalue" rel="6"></textarea>
        <br />

</form>

<div class="add_delete_toolbar" />


<table cellpadding="0" cellspacing="0" border="0" class="display" id="lei_data">
        <thead>
                <tr>
                        <th>ID</th>
			<th>IDTarget</th>
		        <th>IDOrganism</th>
                        <th>Name</th>
                        <th>SMILES</th>
                        <th>Type</th>
                        <th>Value</th>
                </tr>
        </thead>
        <tfoot>
                <tr>
                        <th>ID</th>
                        <th>IDTarget</th>
                        <th>IDOrganism</th>
                        <th>Name</th>
                        <th>SMILES</th>
                        <th>Type</th>
                        <th>Value</th>

                </tr>

        </tfoot>
        <tbody>

        </tbody>
</table>


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
<div id="content">
<p>
Before uploading any data you should register <A HREF="register.jsp">here</A> first in order to keep your data private.
<p>
If you already have an username please login <A HREF="login.jsp">here</A>
<%
}
%>

</div>


<div id="footer">
<p>AtlasCBS - A tool to explore chemico-biological database
<p>
<p>(C) 2011. Unidad de Bioinformatica. Centro de Biologia Molecular "Severo Ochoa". 
<BR>Consejo Superior de Investigaciones Cientificas - Universidad Autonoma de Madrid.
<br>(C) 2011. Laboratorio de Modelado Molecular. Universidad de Alcala de Henares.
<br>(C) 2011. University of Illinois at Chicago (UIC).
<br> <p>(C) 2013. Revised version produced by C.A-Z during his stay at the UAH, Feb-June, 2013
<br>(C) 2015. Unidad de Bioinformatica. Institut Pasteur de Montevideo.

<br>


</div>

<script type="text/javascript">
try{

var pageTracker = _gat._getTracker("UA-33092043-1");
pageTracker._trackPageview();

} catch(err) {}
</script>


</body>
</html>
