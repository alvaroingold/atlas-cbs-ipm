import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;



public class LEIdeleteCompound extends HttpServlet {


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

        String param_id = request.getParameter("id");

        String aut;
        String iduser_session;
        String username_session;
        HttpSession session;
	String id_origin_param;
	int id_origin = -1;



	// session validation 
        try{
            session = request.getSession(true);
            aut = (String) session.getAttribute("authorized");
            iduser_session = (String) session.getAttribute("id_user");
            username_session = (String) session.getAttribute("name");
	    id_origin_param = (String) session.getAttribute("id_origin");

        }catch( Exception e){
                        salida.println("ERROR. What?.\n");
			return;
        }


	int id = -1;

	// Auth validation
	
            if( !(aut != null))
	    {
                salida.println("ERROR: Auth required. Please login <A HREF=\"/examples/jsp/LEI/login.jsp\">here</A> first");
		return;
	    }



        try{
		StringTokenizer mol = new StringTokenizer(param_id,"_");
		if( mol.hasMoreTokens() ){
			id = Integer.parseInt(mol.nextToken());
		}else{
	                salida.println("ERROR: 102. Error on parameters");
	                return;
		}
        }catch(Exception e){
                salida.println("ERROR: 102. Error on parameters");
                return;
        }

        if( id < 0)
        {
                salida.println("ERROR: 103. Error on parameters");
                return;
        }


	try{
        Connection conn = DriverManager.getConnection("jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname +"?user="+user+"&password="+pass);


          PreparedStatement stmt = conn.prepareStatement("DELETE FROM MOLECULE WHERE MOLECULE.ID_MOLECULE = ?", Statement.RETURN_GENERATED_KEYS);
          stmt.setInt(1, id);
          stmt.executeUpdate();
	  salida.print("ok");

	}catch( Exception e){
		salida.println("Errorcillo");
	StringWriter sw = new StringWriter();
	e.printStackTrace(new PrintWriter(sw));
		salida.println(sw.toString());
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
