import java.io.IOException; 
import javax.servlet.ServletException; 
import javax.servlet.http.HttpServlet; 
import javax.servlet.http.HttpServletRequest; 
import javax.servlet.http.HttpServletResponse; 
import javax.servlet.http.HttpSession; 


public class login extends HttpServlet 
{ 

  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException
  {
	doGet(request,response);
  }
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException 
  { 
   try{ 
        String user = request.getParameter("un");
//	 UserBean user = new UserBean(); 
//	user.setUserName(request.getParameter("un")); 
//	user.setPassword(request.getParameter("pw")); 
//	user = UserDAO.login(user); 
//	if (user.isValid()) 
//	{ 
		HttpSession session = request.getSession(true); 
		session.setAttribute("currentSessionUser",user); 
		response.sendRedirect("userLogged.jsp"); //logged-in page 
//	} else response.sendRedirect("invalidLogin.jsp"); //error page 
      } catch (Throwable theException) { System.out.println(theException); } 
  } 
} 
