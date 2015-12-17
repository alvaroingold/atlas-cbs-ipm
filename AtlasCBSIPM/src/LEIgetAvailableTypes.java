import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class LEIgetAvailableTypes extends HttpServlet {


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
	int i = 0;


                try {
			String DRIVER = "org.gjt.mm.mysql.Driver";
			Class.forName(DRIVER).newInstance();
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+user+"&password="+pass;
                        conn = DriverManager.getConnection(conn_URL);

			ResultSet rs2=null;
			PreparedStatement stmt=null;

			
                        if( param != null && param2 != null){
			salida.print("0;");
			        for( i = 1;  i < 4; ++i)
				{
/*                                stmt=conn.prepareStatement("select LEI.VALUE FROM LEI,PHARMACOLOGY,TARGORG WHERE TARGORG.ID_COMBINATION = PHARMACOLOGY.ID_COMBINATION AND PHARMACOLOGY.ID_PHARMACOLOGY = LEI.ID_PHARMACOLOGY AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_TYPE = ? AND LEI.ID_TYPE=2");*/
                                stmt=conn.prepareStatement("select ID_PHARMACOLOGY FROM PHARMACOLOGY,TARGORG WHERE TARGORG.ID_COMBINATION = PHARMACOLOGY.ID_COMBINATION AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_TYPE = ?");
                                        stmt.setString(1, param);
                                        stmt.setString(2, param2);
                                        stmt.setInt(3,i);
		                        rs2=stmt.executeQuery();
					rs2.last();
                                        salida.print(rs2.getRow()+";");
				}
                        }else{
                        }

			salida.print("0;0;0;");

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
