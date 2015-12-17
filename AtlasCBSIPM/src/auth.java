/**
 *
 *
 * auth.java
 *
 *
 * @author: Alvaro Cortes
 *
 * @brief: This is part of ATLAS CBS web application
 *         This module authenticates the registered users in order to keep private
 *         their data from other users.
 * 
 *
 *
 *
 */

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.*;
import java.sql.*;
import java.security.*;
import javax.servlet.*;
import javax.servlet.http.*;
import sun.misc.BASE64Encoder;



public class auth extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet (HttpServletRequest request,
            HttpServletResponse response) {

       try {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title></title></head>");

//           String url = getServletConfig().getServletContext().getInitParameter("dbURL");

        ServletContext servletContext = getServletContext();

        String userpass = servletContext.getInitParameter("user").trim();
        String dbpass = servletContext.getInitParameter("password").trim();
        String dbname = servletContext.getInitParameter("dbname").trim();
        String host = servletContext.getInitParameter("dbhost").trim();
	String dbport = servletContext.getInitParameter("dbport").trim();



         String url = "jdbc:mysql://"+host+":"+dbport+"/"+dbname+"?user="+userpass+"&password="+dbpass; 
        
//           String user = (String)request.getAttribute("user");
 //          String password = (String)request.getAttribute("password");

           String user = (String)request.getParameter("username");
           String password = (String)request.getParameter("password");


           ResultSet rst=null;
           PreparedStatement stmt=null;

           try{
           Class.forName("org.gjt.mm.mysql.Driver").newInstance();

           Connection con = DriverManager.getConnection(url);

           MessageDigest md = null;
           md = MessageDigest.getInstance("SHA"); //step 2
           md.update(password.getBytes("UTF-8")); //step 3
           byte raw[] = md.digest(); //step 4
           String value = (new BASE64Encoder()).encode(raw); //step 5



           out.println("select * from USERS WHERE NAME = " + user + " AND PASSWORD = " + value);

           stmt = con.prepareStatement("select * from USERS WHERE NAME = ? AND PASSWORD = ?");
           stmt.setString(1, user);
           stmt.setString(2, value);
           rst = stmt.executeQuery();


           if( !rst.next() )
           {
            response.sendRedirect("login.jsp");

           }else{
               out.println("YOU'RE LOGGED!"+rst.getString(1));


           PreparedStatement stmt2 = con.prepareStatement("select ID_ORIGINDB from ORIGINDB WHERE ID_USER = ?");
           stmt2.setInt(1, Integer.parseInt(rst.getString(1)));
           ResultSet rst2 = stmt2.executeQuery();
           rst2.next();

            HttpSession session = request.getSession(true);
            session.setAttribute("authorized","yes");
            session.setAttribute("id_user",rst.getString(1));
            session.setAttribute("name",rst.getString(2));

	    rst.close();

            session.setAttribute("id_origin",rst2.getString(1));
	    rst2.close();
            con.close();

// Show URL
            response.sendRedirect("viewer.jsp");
// Hide URL
//            getServletContext().getRequestDispatcher("/jsp/VSDMIP/main.jsp").forward(request,response);


//RequestDispatcher dispatcher =
//request.getRequestDispatcher(address);
//dispatcher.forward(request, response);




//           request.setAttribute ("authorize", "yes");
//           getServletConfig().getServletContext().getRequestDispatcher("/jsp/jsptoserv/hello.jsp").forward(request, response);
           }



           }catch(SQLException e){
                 e.printStackTrace();
                 out.println("SQL fail");
           }

       } catch (Exception ex) {
           ex.printStackTrace ();
       }
    }

    public void doPost(HttpServletRequest request,
                      HttpServletResponse response)
    {
        doGet(request, response);
    }

}
