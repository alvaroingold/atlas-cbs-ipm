import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class LEIgetLowData extends HttpServlet {


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



                try {
			String DRIVER = "org.gjt.mm.mysql.Driver";
			Class.forName(DRIVER).newInstance();
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+user+"&password="+pass;
                        conn = DriverManager.getConnection(conn_URL);

			ResultSet rs2=null;
			PreparedStatement stmt=null;

			// We have it all. Target, organism, molecule and type. We just want a single value.
                        if( param4 != null && param != null && param2 != null && param5 != null){
	       		                stmt=conn.prepareStatement("select PHARMACOLOGY.ID_PHARMACOLOGY, PHARMACOLOGY.ID_MOLECULE, PHARMACOLOGY.VALUE, PHARMACOLOGY.PH, PHARMACOLOGY.TEMP FROM PHARMACOLOGY, TARORG WHERE TARORG.ID_COMBINATION = PHARMACOLOGY.ID_COMBINATION AND TARORG.ID_TARGET = ? AND TARORG.ID_ORGANISM = ? AND PHARMACOLOGY.MOLECULE = ? AND PHARMACOLOGY.ID_TYPE = ?");
					stmt.setString(1, param);
                                        stmt.setString(2, param2);
                                        stmt.setString(3, param4);
                                        stmt.setString(4, param5);
			// We have target, organism and type. We are looking for data for all molecules of this target
			}else if ( param != null && param2 != null && param5 != null){
				stmt=conn.prepareStatement("select PHARMACOLOGY.ID_PHARMACOLOGY, PHARMACOLOGY.ID_MOLECULE, PHARMACOLOGY.VALUE, PHARMACOLOGY.PH, PHARMACOLOGY.TEMP FROM PHARMACOLOGY, TARORG WHERE TARORG.ID_COMBINATION = PHARMACOLOGY.ID_COMBINATION AND TARORG.ID_TARGET = ? AND TARORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_TYPE = ?");
                                        stmt.setString(1, param);
                                        stmt.setString(2, param2);
                                        stmt.setString(3, param5);
			// We have target, organism and molecule. We seek all values for one molecule.
			}else if ( param4 != null && param != null && param2 != null){
                                        stmt=conn.prepareStatement("select PHARMACOLOGY.ID_PHARMACOLOGY, PHARMACOLOGY.ID_MOLECULE, PHARMACOLOGY.VALUE, PHARMACOLOGY.PH, PHARMACOLOGY.TEMP FROM PHARMACOLOGY, TARORG WHERE TARORG.ID_COMBINATION = PHARMACOLOGY.ID_COMBINATION AND TARORG.ID_TARGET = ? AND TARORG.ID_ORGANISM = ? AND PHARMACOLOGY.MOLECULE = ?");
                                        stmt.setString(1, param);
                                        stmt.setString(2, param2);
                                        stmt.setString(3, param4);

                        }else{
                                stmt=conn.prepareStatement("select PHARMACOLOGY.ID_PHARMACOLOGY, PHARMACOLOGY.ID_MOLECULE, PHARMACOLOGY.VALUE, PHARMACOLOGY.PH, PHARMACOLOGY.TEMP FROM PHARMACOLOGY");
                        }
                        rs2=stmt.executeQuery();



                        while (rs2.next()) {
                                salida.println(rs2.getString(1)+"#"+rs2.getString(2)+"#"+rs2.getString(3)+"#"+rs2.getString(4)+"#"+rs2.getString(5)+";");
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
