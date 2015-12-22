import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class ShowDetails extends HttpServlet {
   
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        response.setContentType("text/html");

        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<body>");
        out.println("<head>");

        String title = "Molecula";
        out.println("<title>" + title + "</title>");
	out.println("<script src=\"../../jmol/Jmol.js\"></script>");
        out.println("</head>");
        out.println("<body bgcolor=\"white\">");

        String id = request.getParameter("id");

        out.println("<br>");
        if (id != null) {

        out.println("<h3>" + title + " " + id + "</h3>");

           
                try {
                        Class.forName("org.gjt.mm.mysql.Driver").newInstance();
                }catch (Exception E) {
			out.println("<H1>ERROR</H1><P>No se puede encontrar el connector MySQL.");
                }

                try {
                        java.sql.Connection conn;

                        conn = DriverManager.getConnection("jdbc:mysql://localhost/BDB?user=root&password=alvaro");

                        Statement stmt = conn.createStatement();
                        Statement stmt2 = conn.createStatement();
			ResultSet rs = stmt2.executeQuery("SELECT * FROM MOLECULE WHERE ID_MOLECULE = " + id + " LIMIT 1"); 

			out.println("<HR>");
			String results;
			out.println("<TABLE BORDER=1>");
			out.println("<TR><TD>");
			out.println("<TABLE BORDER=5>");

			int rc = 0;
			String[] titulos = {"SMILES","Last update molec","Origen datos","URL origin","Last update origin"};

                        rs.next() ;
				int i;
				for(i=1; i<= 2; ++i)
				{
					out.println("<TR>");
					out.println("<TD>" +  titulos[i-1] + "</TD><TD>" + rs.getString(i) + "</TD>");
					out.println("</TR>");
				}


			out.println("</TABLE>");
			out.println("</TD><TD>");

out.println("<script>\n jmolInitialize(\"../../jmol\");\njmolCheckBrowser(\"popup\", \"../../browsercheck\", \"onClick\");\n jmolSetAppletColor(\"black\");\n");
out.println("var molecule = ");
                        String ops = rs.getString(3);

                        StringTokenizer ju = new StringTokenizer(ops,"\n");

                       int i2 = 0;
                                      out.println("\"MolNAME\\n\"+");

                       while (ju.hasMoreTokens()) {
                        String line = ju.nextToken();
                        line.replaceAll("\n","");
                                if( i2 < 2){
                                      out.println("\"blablabla\\n\"+");
                                }else{
                                   out.println("\"" + line + "\\n\"+");}
                               i2++;
                        }

			out.println("\"\"");

	out.println("jmolAppletInline(300, molecule);");
	out.println("jmolBr();");
        out.println("</script>");

		out.println("</TD></TR></TABLE");
		
			rs.close();
                        stmt.close();
			stmt2.close();
                       conn.close();
                }catch (SQLException E) {
                        out.println("<h3>SQLException: " + E.getMessage() + "<br>");
                        out.println("SQLState:     " + E.getSQLState() + "<br>");
                        out.println("VendorError:  " + E.getErrorCode() + "<br></h3>");
			out.println("<H1>Error de base de datos</H1>");
                }


	out.println("<HR>");
        } else {
            out.println("");
        }
	out.println("<HR>");
        out.println("<P><H3>Nueva búsqueda</H3>");
        out.print("<form action=\"");
        out.print("ShowDetails\" ");
        out.println("method=POST>");
        out.println("ID");
        out.println("<input type=text size=11 name=id>");
        out.println("<br>");
        out.println("<input type=submit>");
        out.println("</form>");

        out.println("</body>");
        out.println("</html>");
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

}
