<html>
<title>ATLAS CBS server</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<link href="u.css" media="all" rel="stylesheet" type="text/css" />

<script>
function validate_accept(){
    if( !document.regForm.license.checked) 
    {
	    alert("You must accept the license before registering");
	    return false;
    }else if(document.regForm.username.value=="" || document.regForm.password.value==""){
            alert("Please provide a valid username and password");
            return false;
    }else{
	return true;
    }
}
</script> 

</head>

<body>

<div id="top-menu" align="right">Hi anonymous user</A></div>
<div id="header">
<CENTER><IMG SRC="title1.png">  </CENTER>
</div>
<p>
<div id="tabs">
<li><A HREF="viewer.jsp"><span>Map viewer</span></A></li>
<li><A HREF="register.jsp"><span>Register</span></A></li>
<li><A HREF="about.html"><span>About</span></A></li>
</div>
<div id="content">
<p>The registration of this site is required to use upload data services within the ATLAS CBS framework.
<p>Please consider that registration is the only way to keep users data private from each other. All the data are kept private and it is only accesible by the registered user.
<form action="register" method="post" name="regForm" onSubmit="return validate_accept()">

<table>
<tr>
    <td align="right"><label for="username">E-mail address</label></td>
    <td align="left"><input id="username" name="username" tabindex="1" type="text" />  <b>*The username already exists in the database**</b></td>
</tr>
<tr>
    <td align="right"><label for="password">Password:</label></td>
    <td align="left"><input id="password" name="password" tabindex="2" type="password" /></td>
</tr>
<tr>
   <td></td>
   <td><textarea rows="8" cols="60">
AtlasCBS service is a free server for exploration of chemico-biological space and existing databases, using ligand efficiency indexes (LEIs) as variables.
Data are kept in secured form but no responsibility can be accepted for possible bugs or system errors that will make public parts or full users datasets. The AtlasCBS team is not responsible for any illegal
or unethical breach of data access. 

The developers of the platform disclaim all warranties with regard to this platform, web and software, included all implied warranties of merchantability and fitness. In no event shall the developers be liable for any special, indirect of consequential damages or any damages whatsover resulting from loss of user, data or profits, whether in an action of contract, negligence or other tortuous action, arising out of or in connection with the use or performance of this platform, software and web. 
Registering in the system you accept those conditions.
</textarea>
<input type="checkbox" name="license">I accept the terms of the service</td>
</tr>
<tr>
<td></td>
    <td align="left">
        <input type="submit" name="register" value="Register" tabindex="5" />
    </td>
</tr>
</table></center>
</form>
</div>

</body>
</html>
