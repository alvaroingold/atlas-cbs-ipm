import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class LEIgetProps extends HttpServlet {


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


        String param2 = request.getParameter("molecule");
        String param3 = request.getParameter("type");


                try {
			String DRIVER = "org.gjt.mm.mysql.Driver";
			Class.forName(DRIVER).newInstance();
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+user+"&password="+pass;
                        conn = DriverManager.getConnection(conn_URL);

			ResultSet rs2=null;
			PreparedStatement stmt=null;

			// We have it all. we need data for a single point
                        if( param2 != null && param3 != null){
	       		                stmt=conn.prepareStatement("select PROPS.ID_MOLECULE, PROPS.ID_PROP_TYPE, PROPS.VALUE FROM PROPS WHERE PROPS.ID_MOLECULE = ? AND PROPS.ID_PROP_TYPE = ?");
					stmt.setString(1, param2);
                                        stmt.setString(2, param3);
                        }else{
                                        stmt=conn.prepareStatement("select PROPS.ID_MOLECULE, PROPS.ID_PROP_TYPE, PROPS.VALUE FROM PROPS");
                        }
                        rs2=stmt.executeQuery();



                        while (rs2.next()) {
                                salida.println(rs2.getString(1)+"#"+rs2.getString(2)+"#"+rs2.getString(3)+";");
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
