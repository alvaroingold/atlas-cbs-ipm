import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.lang.*;


public class LEIgetMolInformation extends HttpServlet {


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
        String target = request.getParameter("target");
        String organism = request.getParameter("organism");
        String type = request.getParameter("type");


                try {
			String DRIVER = "org.gjt.mm.mysql.Driver";
			Class.forName(DRIVER).newInstance();
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+user+"&password="+pass;
                        conn = DriverManager.getConnection(conn_URL);

			ResultSet rs2=null;
			PreparedStatement stmt=null;
                        StringBuffer answ = new StringBuffer("");


                        if( param2 != null){
				int id_molecule = -1;
       		                stmt=conn.prepareStatement("select PROPS.ID_PROP_TYPE, PROPS.VALUE, ORIGINDB.NAME FROM PROPS,MOLECULE,ORIGINDB WHERE PROPS.ID_MOLECULE = MOLECULE.ID_MOLECULE AND MOLECULE.NAME = ? AND ORIGINDB.ID_ORIGINDB = MOLECULE.ID_ORIGINDB");
				stmt.setString(1, param2);
	                        rs2=stmt.executeQuery();
	                        while (rs2.next()) {
					answ.append(rs2.getString(1)+"#"+rs2.getString(2)+"#"+rs2.getString(3)+";");
	                        }
				rs2.close();
				stmt.close();

				if(  target != null && organism != null && type != null)
				{
					int idtarget = Integer.parseInt(target);
					int idorganism = Integer.parseInt(organism);
					int idtype = Integer.parseInt(type);
					stmt = conn.prepareStatement("select PHARMACOLOGY.VALUE FROM TARGORG,PHARMACOLOGY,MOLECULE WHERE TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND TARGORG.ID_COMBINATION = PHARMACOLOGY.ID_COMBINATION AND PHARMACOLOGY.ID_TYPE = ? AND PHARMACOLOGY.ID_MOLECULE = MOLECULE.ID_MOLECULE AND MOLECULE.NAME = ? LIMIT 1");
	                                stmt.setInt(1, idtarget);
	                                stmt.setInt(2, idorganism);
	                                stmt.setInt(3, idtype);
                                        stmt.setString(4, param2);


	                                rs2=stmt.executeQuery();
					float value = -1;
	                                while (rs2.next()) {
						value = rs2.getFloat(1);
						if( value > 0)
						{
						 value = (float) -Math.log10(value);
						 answ.append("|"+idtype+"#"+value);
						}
	                                }
					rs2.close();
					stmt.close();

                                        stmt = conn.prepareStatement("select PDB_TRANSLATION.CODE FROM TARGORG,PDB_TRANSLATION,MOLECULE WHERE TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND TARGORG.ID_COMBINATION = PDB_TRANSLATION.ID_COMBINATION AND PDB_TRANSLATION.ID_MOLECULE = MOLECULE.ID_MOLECULE AND MOLECULE.NAME = ? LIMIT 1");
                                        stmt.setInt(1, idtarget);
                                        stmt.setInt(2, idorganism);
                                        stmt.setString(3, param2);


                                        rs2=stmt.executeQuery();
                                        String code = "";
                                        while (rs2.next()) {
                                                code = rs2.getString(1);
                                                answ.append("|PDB#"+code);
                                        }
					rs2.close();
                                        stmt.close();

	
				}


			}
			salida.println(answ.toString());

                        conn.close();

                }catch (SQLException E) {
			salida.println("ERROR. 2434632.\n");

                          try {
                            StringWriter sw = new StringWriter();
                            PrintWriter pw = new PrintWriter(sw);
                            E.printStackTrace(pw);
                            salida.println(sw.toString());
                          }catch(Exception e2) {
                            salida.println("Failed even for printing stacktrace!!!");
                          }


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
