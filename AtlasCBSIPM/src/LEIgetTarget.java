import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class LEIgetTarget extends HttpServlet {


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

        String param = request.getParameter("organism");
	String iddatabase = request.getParameter("database");

                try {
			String DRIVER = "org.gjt.mm.mysql.Driver";
			Class.forName(DRIVER).newInstance();
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+user+"&password="+pass;
                        conn = DriverManager.getConnection(conn_URL);

			ResultSet rs2=null;
			PreparedStatement stmt=null;


                        if( param != null){
       		                stmt=conn.prepareStatement("select TARGETS.ID_TARGET, TARGETS.NAME from TARGETS,TARGORG WHERE TARGETS.ID_TARGET = TARGORG.ID_TARGET AND TARGORG.ID_ORGANISM = ? ORDER BY TARGETS.NAME");
				stmt.setString(1, param);
			}else if ( iddatabase != null){
				stmt=conn.prepareStatement("select TARGETS.ID_TARGET, TARGETS.NAME from TARGETS WHERE TARGETS.ID_ORIGINDB = ? ORDER BY TARGETS.NAME");
                                stmt.setString(1, iddatabase);
                        }else{
	                        stmt=conn.prepareStatement("select ID_TARGET, NAME from TARGETS ORDER BY NAME");
                        }
                        rs2=stmt.executeQuery();



                        while (rs2.next()) {
                                salida.println(rs2.getString(1)+"#"+rs2.getString(2)+";");
                        }
                        conn.close();

                }catch (SQLException E) {
			salida.println("ERROR. 2434632.\n");
                        salida.println(E);
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
