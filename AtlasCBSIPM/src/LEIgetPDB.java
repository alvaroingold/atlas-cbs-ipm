import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class LEIgetPDB extends HttpServlet {


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
	int combination = -1;
	int id_molecule = -1;
	String molname = "";
	String molsmi = "";
	int idt = -1;
	int ido = -1;
	int idd = -1;
	int idp = -1;
	int max = 0;
	int maxi = 0;
	int[] ph = new int[7];
	String organism = "";
	String target = "";

        String code = request.getParameter("code");
	int i = 0;


                try {
			String DRIVER = "org.gjt.mm.mysql.Driver";
			Class.forName(DRIVER).newInstance();
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+user+"&password="+pass;
                        conn = DriverManager.getConnection(conn_URL);

			ResultSet rs2=null;
			PreparedStatement stmt=null;


                        if( code != null){

                                stmt=conn.prepareStatement("select ID_COMBINATION,ID_MOLECULE from PDB_TRANSLATION WHERE CODE = ?");
                                stmt.setString(1, code);
	                        rs2=stmt.executeQuery();
				rs2.next();
				combination = rs2.getInt(1);
				id_molecule = rs2.getInt(2);

				stmt=conn.prepareStatement("select TARGORG.ID_TARGET,TARGETS.NAME,TARGORG.ID_ORGANISM,ORGANISM.NAME,TARGETS.ID_ORIGINDB FROM TARGORG,ORGANISM,TARGETS WHERE TARGORG.ID_COMBINATION=? AND TARGORG.ID_TARGET = TARGETS.ID_TARGET AND TARGORG.ID_ORGANISM = ORGANISM.ID_ORGANISM;");
                                stmt.setInt(1, combination);
                                rs2=stmt.executeQuery();
                                rs2.next();
                                idt = rs2.getInt(1);
                                ido = rs2.getInt(3);
				idd = rs2.getInt(5);
				target = rs2.getString(2);
				organism = rs2.getString(4);


                                for( i = 0;  i < 7; ++i)
                                {
                                stmt=conn.prepareStatement("select LEI.VALUE FROM LEI,PHARMACOLOGY,TARGORG WHERE TARGORG.ID_COMBINATION = PHARMACOLOGY.ID_COMBINATION AND PHARMACOLOGY.ID_PHARMACOLOGY = LEI.ID_PHARMACOLOGY AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_TYPE = ? AND LEI.ID_TYPE=2");
                                        stmt.setInt(1, idt);
                                        stmt.setInt(2, ido);
                                        stmt.setInt(3,i);
                                        rs2=stmt.executeQuery();
                                        rs2.last();
                                        ph[i] = rs2.getRow();
                                }

	
                                for( i = 0;  i < 7; ++i)
                                {
					if( ph[i] > max)
					{
					  max = ph[i];
					  maxi = i;
					}
                                }

				idp = maxi;

 				stmt=conn.prepareStatement("select MOLECULE.NAME,MOLECULE.SMILES FROM MOLECULE WHERE MOLECULE.ID_MOLECULE = ?;");
                                stmt.setInt(1, id_molecule);
                                rs2=stmt.executeQuery();
                                rs2.next();
                                molname = rs2.getString(1);
				molsmi = rs2.getString(2);


				salida.println(idt+";"+ido+";"+idd+";"+idp+";"+target+";"+organism+";"+molname+";"+molsmi+";");
                        }

                        conn.close();

                }catch (SQLException E) {
                        salida.println("0;0;0;0;null;null;null");
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
                        salida.println("0;0;0;0;null;null;null");
                        salida.println("ERROR. NoMySQL.\n");
		}catch( Exception E) {
                        salida.println("0;0;0;0;null;null;null");
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
