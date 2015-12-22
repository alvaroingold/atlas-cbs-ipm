import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class LEIgetLEI extends HttpServlet {


    ResourceBundle rb = ResourceBundle.getBundle("LocalStrings");
    

   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter salida = response.getWriter();

        ServletContext servletContext = getServletContext();
        String user = servletContext.getInitParameter("user").trim();
        String pass = servletContext.getInitParameter("password").trim();
        String dbname = servletContext.getInitParameter("dbname").trim();
        String dbhost = servletContext.getInitParameter("dbhost").trim();
        String dbport = servletContext.getInitParameter("dbport").trim();


        String param = request.getParameter("target");
        String param2 = request.getParameter("organism");
        String param3 = request.getParameter("database");
        String param4 = request.getParameter("molecule");
        String param5 = request.getParameter("type");
        String param6 = request.getParameter("lei");




                try {
			String DRIVER = "org.gjt.mm.mysql.Driver";
			Class.forName(DRIVER).newInstance();
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+user+"&password="+pass;
                        conn = DriverManager.getConnection(conn_URL);

			ResultSet rs2=null;
			PreparedStatement stmt=null;

			// We have it all. Target, organism, molecule, data type and lei definition. We just want a single value.
                        if( param4 != null && param != null && param2 != null && param5 != null && param6 != null){
	       		                stmt=conn.prepareStatement("select LEI.VALUE,MOLECULE.ID_MOLECULE FROM LEI,PHARMACOLOGY,TARGORG,MOLECULE WHERE TARGORG.ID_COMBINATION = PHARMACOLOGY.ID_COMBINATION AND PHARMACOLOGY.ID_PHARMACOLOGY = LEI.ID_PHARMACOLOGY AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_MOLECULE = ? AND PHARMACOLOGY.ID_TYPE = ? AND LEI.ID_TYPE = ? AND MOLECULE.ID_MOLECULE = PHARMACOLOGY.ID_MOLECULE");
					stmt.setString(1, param);
                                        stmt.setString(2, param2);
                                        stmt.setString(3, param4);
                                        stmt.setString(4, param5);
                                        stmt.setString(5, param6);

			// We have target, organism, type and lei. We are looking for data for all molecules of this target and LEI
			}else if ( param != null && param2 != null && param5 != null && param6 != null){
					stmt=conn.prepareStatement("select LEI.VALUE,MOLECULE.ID_MOLECULE FROM LEI,PHARMACOLOGY,TARGORG,MOLECULE WHERE TARGORG.ID_COMBINATION = PHARMACOLOGY.ID_COMBINATION AND PHARMACOLOGY.ID_PHARMACOLOGY = LEI.ID_PHARMACOLOGY AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_TYPE = ? AND LEI.ID_TYPE = ? AND MOLECULE.ID_MOLECULE = PHARMACOLOGY.ID_MOLECULE ");
                                        stmt.setString(1, param);
                                        stmt.setString(2, param2);
                                        stmt.setString(3, param5);
                                        stmt.setString(4, param6);

			// We have target, organism and molecule. We seek all values for one molecule.
			}else if ( param4 != null && param != null && param2 != null && param6 != null){
					stmt=conn.prepareStatement("select LEI.VALUE,MOLECULE.ID_MOLECULE FROM LEI,PHARMACOLOGY,TARGORG,MOLECULE WHERE TARGORG.ID_COMBINATION = PHARMACOLOGY.ID_COMBINATION AND PHARMACOLOGY.ID_PHARMACOLOGY = LEI.ID_PHARMACOLOGY AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_MOLECULE = ? AND LEI.ID_TYPE = ? AND MOLECULE.ID_MOLECULE = PHARMACOLOGY.ID_MOLECULE");
                                        stmt.setString(1, param);
                                        stmt.setString(2, param2);
                                        stmt.setString(3, param4);
                                        stmt.setString(4, param6);


                        }else{
				salida.println("NO case");
				salida.println("Target: " + param);
                                salida.println("Organism: " + param2);
                                salida.println("Database: " + param3);
                                salida.println("Molecule: " + param4);
                                salida.println("Pharma type: " + param5);
                                salida.println("Lei type: " + param6);

                                //stmt=conn.prepareStatement("select LEI.VALUE FROM LEI");
                        }
                        rs2=stmt.executeQuery();



                        while (rs2.next()) {
                                salida.println(rs2.getString(2)+"|"+rs2.getString(1)+";");
                        }
                        conn.close();

                }catch (SQLException E) {
			salida.println("ERROR. 2434632.\n");
                }catch( ClassNotFoundException E) {
                        salida.println("ERROR. NoMySQL.\n");
		}catch( Exception E) {
                        salida.println("ERROR. What?.\n");
		}

        salida.close();
    }


    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        processRequest(request, response);
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException
    {
        doGet(request, response);
    }

}
