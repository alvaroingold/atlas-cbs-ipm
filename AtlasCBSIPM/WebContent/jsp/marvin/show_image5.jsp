<html>
<head>
<%@page import="java.net.URLEncoder"%>

<%!
/**
 * Replaces the occurences of a string with another 
 * one in a third string.
 * For example, useful for inserting molfiles into script
 * generated HTML pages.
 * @param input original string
 * @param query string to be replaced
 * @param replacement string that will replace all occurences 
 * of the query string
 * @return the modified string.
 */
static String replaceString(String input, String query, String replacement) {
	StringBuffer sb = new StringBuffer();
	int from=0;
	int pos;
	while((pos = input.indexOf(query, from)) >= 0) {
	    if(pos > from) {
			sb.append(input.substring(from,pos));
	    }
	    sb.append(replacement);
	    from = pos+query.length();
	}
	if(input.length() > from)
	    sb.append(input.substring(from,input.length()));
	return sb.toString();
}

/**
 * Converts a string to a format that can be used as a
 * value of JavaScript variable in an HTML page.
 * Converts line separators to UNIX style and replaces
 * new line characters with a backslash and an "n"
 * character.
 * @param input original string containing line terminators
 * @return the modified string.
 */
public static String convertForJavaScript(String input) {
    String value = input;
    //Converting Windows style string to UNIX style
    value = replaceString(value, "\r\n", "\n");
    //Converting Macintosh style string to UNIX style
    value = replaceString(value, "\r", "\n");
    //Converting special characters
    value = replaceString(value, "\\", "\\\\");
    value = replaceString(value, "\n", "\\n");
    value = replaceString(value, "\"", "\\\"");
    value = replaceString(value, "'", "\\'");
    return value;
}
%>
</head>
<body>
<%
String format = java.net.URLEncoder.encode("png:w200,h200,b32,#ffffff");
String[] mols = new String[] {"\n  Marvin  05250517512D\n\n  6  6  0  0  0  0            999 V2000\n"+
"    0.3437    2.0125    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"   -0.3707    1.6000    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"   -0.3707    0.7750    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"    0.3437    0.3625    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"    1.0582    0.7750    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"    1.0582    1.6000    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"  1  2  1  0  0  0  0\n  1  6  2  0  0  0  0\n  2  3  2  0  0  0  0\n"+
"  3  4  1  0  0  0  0\n  4  5  2  0  0  0  0\n  5  6  1  0  0  0  0\n"+
"M  END\n", 
"\n  Marvin  05250517532D\n\n  5  5  0  0  0  0            999 V2000\n"+
"   -0.1562    0.8713    0.0000 N   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"   -0.8707    1.2838    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"   -0.8707    2.1088    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"    0.5582    2.1088    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"    0.5582    1.2838    0.0000 C   0  0  0  0  0  0  0  0  0  0  0  0\n"+
"  1  2  1  0  0  0  0\n  1  5  1  0  0  0  0\n  2  3  2  0  0  0  0\n"+
"  3  4  1  0  0  0  0\n  4  5  2  0  0  0  0\nM  END\n"};
%>
<h1>Image generation example</h1>
<p>Click on a molecule image to load the molecule into the viewer.</p>
<table width="100%">
<tr><td>
<table>
<tr><th>Structure</th><th>Name</th></tr>
<tr><td><img src="generate_image.jsp?mol=<%=java.net.URLEncoder.encode(mols[0])%>&format=<%=format%>"
width=100 height=100 onclick='loadmol("<%=convertForJavaScript(mols[0])%>")'></td>
<td>Benzene</td>
</tr>
<tr><td><img src="generate_image.jsp?mol=<%=java.net.URLEncoder.encode(mols[1])%>&format=<%=format%>"
width=100 height=100 onclick='loadmol("<%=convertForJavaScript(mols[1])%>")'></td>
<td>Pyrrole</td>
</tr>
</table>
</td>
<td>
<script language="JavaScript" src="http://www.chemaxon.com/marvin/marvin.js">
</script>
<script language="JavaScript"><!--

function loadmol(mol) {
    if(document.mview != null) {
        document.mview.setM(0,mol);
    }
}

function resetmol() {
    mol = "\n  Marvin  05250518262D\n\n  0  0  0  0  0  0            999 V2000\n"+
"M  END\n";
    loadmol(mol);
}

mview_name="mview";
mview_begin("http://www.chemaxon.com/marvin",200,200);
mview_end();
//-->
</script>
<form>
<p><input type="button" value="Reset canvas" onclick="resetmol()"></p>
</form>
</td></tr>
</table>
<p>(<a href="show_image5.jsp.txt">JSP source</a>)</p>
</body>
</html>
