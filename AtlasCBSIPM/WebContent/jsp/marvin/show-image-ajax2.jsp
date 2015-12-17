<html>
<head>
<%@page import="java.io.*"%>
<title>MarvinSketch Ajax example 2</title>
<script language="javascript" style="text/javascript">
/* Create a new XMLHttpRequest object to talk to the Web server */
var xmlHttp = false;
/*@cc_on @*/
/*@if (@_jscript_version >= 5)
try {
      xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
} catch (e) {
      try {
              xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (e2) {
                        xmlHttp = false;
                          }
}
@end @*/

if (!xmlHttp && typeof XMLHttpRequest != 'undefined') {
      xmlHttp = new XMLHttpRequest();
}

var selected = -1;
var moltype = "smiles";
var mols = new Array();
var imagetype = "png";

function callServer() {
    var parameters = "format="+imagetype;
    parameters = parameters+"&index="+(selected+1);
    parameters = parameters+"&moltype="+moltype;
    parameters = parameters+"&mol="+getSelectedMol();
    var url = "createsaveimagemol.jsp";

    // Open a connection to the server
    xmlHttp.open("POST", url, true);
    xmlHttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    xmlHttp.setRequestHeader("Content-length", parameters.length);
    xmlHttp.setRequestHeader("Connection", "close");

    // Setup a function for the server to run when it's done
    xmlHttp.onreadystatechange = updatePage;
    // Send the request
    xmlHttp.send(parameters);
}

function updatePage() {
    if (xmlHttp.readyState == 4) {
        var response = xmlHttp.responseText;
        // parse index an
        var x = response.indexOf("compound");
        var s = response.substring(x+"compound".length);
        x = s.indexOf("."+imagetype);
        var i = s.substring(0,x);
       // response = '<img src="'+response+'" alt="'+response+'" width=200 height=200 />';
       var elemid = "img"+i;
       var imgelem = document.getElementById(elemid);
       imgelem.src = response;
    }
}

function getSelectedMol() {
    if(selected != -1) {
        return mols[selected];
    }
    return "";
}

function setSelectedMol(s) {
    if(selected != -1) {
        mols[selected] = s;
    }
}
</script>
</head>
<body>
<h1>MarvinSketch Ajax example 2</h1>
<p>The displaying molecules are stored on the server in smiles and png format.
Click on a molecule image on the left side to edit the linking
molecule in MarvinSketch. By pressing  the <strong>Save edited structure</strong>
button, the image will be refreshed and the molecule will be updated also on 
server side.<br>
On the server side,
 <a href="createsaveimagemol.jsp.txt">createsaveimagemol.jsp</a> creates image
 from the posted molecule source and overwrites linking molecule and image
 files.  After then, sends back the URL of the updated image file to the browser to refresh the displayed image.</p>
<script language="javascript">
<%
// load molecules
String workdir = "/var/www/public/test/image-generation/workdir";
String moltype = "smiles";
File parent = new File(workdir);
ByteArrayInputStream bis;
for(int i=0;i<2;i++) {
    File f = new File(parent,"compound"+String.valueOf(i+1)+"."+moltype);
    String molstr = "";
    if(f.exists()) {
        try {        
            FileInputStream fin = new FileInputStream(f);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.flush();
            int d = -1;
            while((d = fin.read()) != -1) {
                bos.write((byte)d);
            }
            fin.close();
            bos.close();
            molstr = bos.toString();
        }catch(IOException ioex) {molstr=ioex.getMessage();}
    }
    %>
    mols[<%=i %>]="<%=molstr %>";
    <%
}
%>
</script>
<table>
<tr>
<td>
    <table bgcolor="#efefef">
<%
for(int i=1;i<=2;i++) { %>
    <tr><td><img name="img<%=i %>" id="img<%=i %>" src="workdir/compound<%=i %>.png" width=200 height=200 onclick="editMol(<%=i %>)"/></td></tr>
<%
}
%>
    </table>
</td>
<td width=30>&nbsp;</td>
<td valign=top align="center">
    <script language="javascript" src="/marvin/marvin.js"></script>
    <script language="javascript" style="text/javascript">
msketch_name="JMSketch";
msketch_begin("/marvin",400,380);
msketch_end();

function editMol(index) {
    selected = index-1;
    document.getElementById("molindex").innerHTML = index;
    if(document.JMSketch != null) {
        document.JMSketch.setMol(mols[selected]);
    }
}

function saveSelectedMol() {
    if(selected == -1) {
        return;
    }
    if(document.JMSketch != null) {
        var s = document.JMSketch.getMol(moltype);
        if(getSelectedMol() != s) {
            setSelectedMol(s);
            callServer();
        }
    }
}

    </script>
    <p><strong>Selected compound: <span id="molindex">--</span> </strong>
    <form><input type="button" value="Save edited structure" onclick="saveSelectedMol()"/></form></p>
</td>
</tr>
</table>
<p>(<a href="show-image-ajax2.jsp.txt">source of this page</a>)</p>
</body>
</html>
