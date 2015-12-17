import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class LEIaddTarget extends HttpServlet {


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

        String aut = "";
	String iduser_session = "";
	String username_session ="";
	HttpSession session;


	try{
            session = request.getSession(true);
            aut = (String) session.getAttribute("authorized");
            iduser_session = (String) session.getAttribute("id_user");
            username_session = (String) session.getAttribute("name");

	}catch( Exception e){
                        salida.println("ERROR. What?.\n");
	}

	try{

	    if( aut != null && Integer.parseInt(iduser_session) >= 0 && param != null && param != "")	
	    {
                        String DRIVER = "org.gjt.mm.mysql.Driver";
                        Class.forName(DRIVER).newInstance();
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+user+"&password="+pass;

                        conn = DriverManager.getConnection(conn_URL);

                        ResultSet rs2=null;
                        PreparedStatement stmt=null;

                        stmt=conn.prepareStatement("select ORIGINDB.ID_ORIGINDB FROM ORIGINDB WHERE ORIGINDB.ID_USER = ? LIMIT 1");
                        stmt.setInt(1, Integer.parseInt(iduser_session));
                        rs2=stmt.executeQuery();
			if( !rs2.next() )
			{
				throw new Exception();
			}
                        int id_origindb = rs2.getInt(1);

                        PreparedStatement st = conn.prepareStatement("INSERT INTO TARGETS (NAME,DESCRIPTION,ID_ORIGINDB) VALUES(?,'Private user target',?);", Statement.RETURN_GENERATED_KEYS);
                        st.setString(1, param);
                        st.setInt(2,id_origindb);
                        st.executeUpdate();
	
			ResultSet rs_m = null;
			int id_target = -1;
            		rs_m = st.getGeneratedKeys();
		        if (rs_m.next()) {
	                      id_target = rs_m.getInt(1);
	                }
            		rs_m.close();
			st.close();

	                PreparedStatement st2 = conn.prepareStatement("select ID_ORGANISM from ORGANISM WHERE NAME = 'Default';");
	                ResultSet rst = st2.executeQuery();
		        int id_default_org = -1;

                        if( rst.next() )
                        {
                             id_default_org = rst.getInt(1);
                        }
			
                        st2 = conn.prepareStatement("INSERT INTO TARGORG (ID_TARGET, ID_ORGANISM) VALUES (?,?);");
                        st2.setInt(1, id_target);
                        st2.setInt(2, id_default_org);
                        st2.executeUpdate();

                        conn.close();

		salida.println("OK");
            }else{
		salida.println("ERROR: Auth required. Please login <A HREF=\"/examples/jsp/LEI/login.jsp\">here</A> first");
	    }

	}catch (SQLException E) {
                salida.println("ERROR. 2434632.\n");
                salida.println(E);
        }catch( ClassNotFoundException E) {
                salida.println("ERROR. NoMySQL.\n");
	}catch(Exception e){
                salida.println("Epic Fail");
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
