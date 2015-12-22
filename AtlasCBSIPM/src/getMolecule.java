import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class getMolecule extends HttpServlet {
   
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
                }

                catch (Exception E) {
			out.println("<H1>ERROR</H1><P>No se puede encontrar el connector MySQL.");

                }

                try {
                        java.sql.Connection conn;

                        conn = DriverManager.getConnection("jdbc:mysql://localhost/DUD?user=root&password=alvaro");

                        Statement stmt = conn.createStatement();
                        Statement stmt2 = conn.createStatement();
			ResultSet rs = stmt2.executeQuery("SELECT * FROM CONFIGURATION WHERE ID_MOLECULE = " + id + " LIMIT 1"); /* Solo la primera configuracion */
  	                ResultSet rs2 = stmt.executeQuery("SELECT MOLECULE.SMILES,MOLECULE.LAST_UPDATE,ORIGINDB.NAME,ORIGINDB.URL,ORIGINDB.LAST_UPDATE FROM MOLECULE,ORIGINDB WHERE MOLECULE.ID_MOLECULE = '" + id + "' AND MOLECULE.ID_ORIGINDB = ORIGINDB.ID_ORIGINDB");

			out.println("<HR>");
			String results;
			out.println("<TABLE BORDER=1>");
			out.println("<TR><TD>");
			out.println("<TABLE BORDER=5>");

			int rc = 0;
			String[] titulos = {"SMILES","Last update molec","Origen datos","URL origin","Last update origin"};

                        while (rs2.next()) {
				int i;
				for(i=1; i<= 5; ++i)
				{
					out.println("<TR>");
					out.println("<TD>" +  titulos[i-1] + "</TD><TD>" + rs2.getString(i) + "</TD>");
					out.println("</TR>");
				}
                        }
			out.println("</TABLE>");
			out.println("</TD><TD>");

out.println("<script>\n jmolInitialize(\"../../jmol\");\njmolCheckBrowser(\"popup\", \"../../browsercheck\", \"onClick\");\n jmolSetAppletColor(\"black\");\n");
out.println("var molecule = ");
        		while (rs.next()) {
			StringTokenizer coordenadas = new StringTokenizer(rs.getString(3),";");
			StringTokenizer mol2types = new StringTokenizer(rs.getString(5),";");
			StringTokenizer topology = new StringTokenizer(rs.getString(7),";");
			out.println("\"@<TRIPOS>MOLECULE\\n\"+");
			out.println("\"Molecula" +id + "\\n\"+");
			int natom = mol2types.countTokens();
			int nbonds = topology.countTokens();
			out.println("\"" + natom + " " + nbonds+ "\\n\"+");
			out.println("\"SMALL\\n\"+");
			out.println("\"NO_CHARGES\\n\"+");
			out.println("\"\\n\"+");
			out.println("\"\\n\"+");
			out.println("\"@<TRIPOS>ATOM\\n\"+");
			int i2 = 1;
			while (coordenadas.hasMoreTokens()) {
			String mol2type = mol2types.nextToken();
			StringTokenizer mol2atom = new StringTokenizer(mol2type,".");
			
				   out.println("\"" + i2 + " " + mol2atom.nextToken() + i2 + " " + coordenadas.nextToken() + " " + mol2type + "1 LIG " + " 0.0" + "\\n\"+");
				++i2;
     			}
 				
			out.println("\"@<TRIPOS>BOND\\n\"+");
			int numBonds = topology.countTokens();
		for (int i = 0; i < numBonds; i++) {
			int bondId = i + 1;
			StringTokenizer stBond = new StringTokenizer(topology.nextToken(), "-");
			out.println("\"   " + bondId + " " + stBond.nextToken() + " " + stBond.nextToken() + " " + stBond.nextToken()+ "\\n\"+"); 
		}
			out.println("\"\"");

	out.println("jmolAppletInline(300, molecule);");
	out.println("jmolBr();");
        out.println("</script>");


                        }
		out.println("</TD></TR></TABLE");
		
                        rs2.close();
			rs.close();
                        stmt.close();
			stmt2.close();
                       conn.close();
                }

                catch (SQLException E) {
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
        out.print("getMolecule\" ");
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
