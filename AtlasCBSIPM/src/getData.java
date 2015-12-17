import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class getData extends HttpServlet {


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

        String param = request.getParameter("target");
        String param2 = request.getParameter("organism");

                try {
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+"/"+dbname+"?user="+user+"&password="+pass;
                        conn = DriverManager.getConnection(conn_URL);

                        Statement stmt = conn.createStatement();
                        ResultSet rs2;
                        if( param2 != null){
                        rs2 = stmt.executeQuery("select MOLECULE.NAME,PHARMACOLOGY.SEI,PHARMACOLOGY.BEI,PHARMACOLOGY.NSEI,PHARMACOLOGY.NBEI,PHARMACOLOGY.NBEI2 from PHARMACOLOGY,MOLECULE WHERE PHARMACOLOGY.ID_TARGET=" + param + " AND MOLECULE.ID_MOLECULE=PHARMACOLOGY.ID_MOLECULE AND PHARMACOLOGY.ID_ORGANISM="+param2);
                        }else{
                        rs2 = stmt.executeQuery("select MOLECULE.NAME,PHARMACOLOGY.NSEI,PHARMACOLOGY.NBEI2 from PHARMACOLOGY,MOLECULE WHERE PHARMACOLOGY.ID_TARGET=" + param + " AND MOLECULE.ID_MOLECULE=PHARMACOLOGY.ID_MOLECULE");
                        }
                                salida.println("NAME#SEI#BEI#NSEI#NBEI#nBEI;");
                        while (rs2.next()) {
                                salida.println(rs2.getString(1)+"#"+rs2.getString(2)+"#"+rs2.getString(3)+";");
                        }
                        conn.close();

                }catch (SQLException E) {
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
