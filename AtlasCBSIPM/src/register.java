/**
 *
 *
 * register.java
 *
 *
 * @author: Alvaro Cortes
 *
 * @brief: This is part of ATLAS CBS web application
 *         This module add a new user
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



public class register extends HttpServlet {

    private static final long serialVersionUID = 1L;

    public void doGet (HttpServletRequest request,
            HttpServletResponse response) {

       try {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        ServletContext servletContext = getServletContext();

        String userpass = servletContext.getInitParameter("user").trim();
        String dbpass = servletContext.getInitParameter("password").trim();
        String dbname = servletContext.getInitParameter("dbname").trim();
        String host = servletContext.getInitParameter("dbhost").trim();
        String dbport = servletContext.getInitParameter("dbport").trim();


        String url = "jdbc:mysql://"+host+":"+dbport+"/"+dbname+"?user="+userpass+"&password="+dbpass; 
        
        String user = (String)request.getParameter("username");
        String password = (String)request.getParameter("password");

	
        ResultSet rst=null;
        PreparedStatement stmt=null;

        try{
           Class.forName("org.gjt.mm.mysql.Driver").newInstance();

           Connection con = DriverManager.getConnection(url);

           MessageDigest md = null;
           md = MessageDigest.getInstance("SHA"); 
           md.update(password.getBytes("UTF-8"));
           byte raw[] = md.digest(); 
           String value = (new BASE64Encoder()).encode(raw); 

           stmt = con.prepareStatement("select * from USERS WHERE NAME = ?");
           stmt.setString(1, user);
           rst = stmt.executeQuery();


           if( !rst.next() )
           {
		// New user

		// 1. Create user name in the database
		

            stmt = con.prepareStatement("INSERT INTO USERS (NAME,PASSWORD) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, user.trim());
            stmt.setString(2, value);
	    int userid = -1;
            stmt.executeUpdate();
            ResultSet rs_m = null;
            rs_m = stmt.getGeneratedKeys();
            if (rs_m.next()) {
                       userid = rs_m.getInt(1);
            }
            rs_m.close();


		// 2. Create an empty design in the database

		// 2a. Create ORIGINDB User 

            stmt = con.prepareStatement("INSERT INTO ORIGINDB (NAME,URL,DESCRIPTION,ID_USER) VALUES ('UserSet','ATLAS CBS','This is the personal set of a user',?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, userid);
            int origindb;
            stmt.executeUpdate();
            rs_m = null;
            rs_m = stmt.getGeneratedKeys();
	    int dbid = -1;
            if (rs_m.next()) {
                       dbid = rs_m.getInt(1);
            }
            rs_m.close();

		// 2a. Create a Default Target,default organism and a default combination for this user

	    out.println("Before TARGETS");
            stmt = con.prepareStatement("INSERT INTO TARGETS (NAME,DESCRIPTION,ID_ORIGINDB) VALUES ('Default','Default Target',?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, dbid);
            int id_target = -1;
            stmt.executeUpdate();
            rs_m = stmt.getGeneratedKeys();
            if (rs_m.next()) {
                       id_target = rs_m.getInt(1);
            }
            rs_m.close();


           stmt = con.prepareStatement("select ID_ORGANISM from ORGANISM WHERE NAME = 'Default';");
           rst = stmt.executeQuery();
	   int id_default_org = -1;
           if( rst.next() )
           {
		id_default_org = rst.getInt(1);
	   }

            stmt = con.prepareStatement("INSERT INTO TARGORG (ID_TARGET, ID_ORGANISM) VALUES (?,?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id_target);
            stmt.setInt(2, id_default_org);

            stmt.executeUpdate();


		// Finally, login the new user so he/she can start to operate inmediatly
 
            HttpSession session = request.getSession(true);
            session.setAttribute("authorized","yes");
            session.setAttribute("id_user",String.valueOf(userid));
            session.setAttribute("name",user);
            session.setAttribute("id_origin",String.valueOf(dbid));



            response.sendRedirect("viewer.jsp");
           }else{
            rst.close();
	    response.sendRedirect("already.jsp");  // duplicate user name
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
