import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class getRelated extends HttpServlet {


    ResourceBundle rb = ResourceBundle.getBundle("LocalStrings");
    

   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html");

        PrintWriter salida = response.getWriter();

        String param = request.getParameter("target");
        String param2 = request.getParameter("smiles");

        if (param2 != null ) {
	StringBuffer sb = new StringBuffer();
           
        	String[] command = { "/bin/bash", "-c", "echo \"" + param2 + "\" | babel -ismi -ofpt -xfMACCS -xs -xn" };
    		Runtime runtime = Runtime.getRuntime();
    		Process process = null;
    		try {
      			process = runtime.exec(command);
      			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));

		       String line = null;
      			while ((line = in.readLine()) != null) {
				sb.append(line);

      			}
    		}catch (Exception e) {
        		salida.println("Error#0.0#0.0;");
    		}


                try {
                        java.sql.Connection conn;
                        conn = DriverManager.getConnection("jdbc:mysql://localhost/BDB?user=root&password=alvaro");

                        Statement stmt = conn.createStatement();
                        Statement stmt2 = conn.createStatement();
			Random r = new Random();
  			String token = "search" + Long.toString(Math.abs(r.nextLong()), 36);
                        if( param != null){

                        stmt.executeUpdate("CREATE view " + token + " as select MOLECULE.NAME,PHARMACOLOGY.NSEI,PHARMACOLOGY.NBEI2,MolSearch_tanimoto(FINGERPRINTS.FINGERPRINT,'" + sb.toString() + "') as g from FINGERPRINTS,MOLECULE,PHARMACOLOGY WHERE FINGERPRINTS.ID_TYPE=1 AND MOLECULE.ID_MOLECULE = FINGERPRINTS.ID_MOLECULE AND PHARMACOLOGY.ID_MOLECULE=MOLECULE.ID_MOLECULE AND PHARMACOLOGY.ID_TARGET=" + param+";");
                        }else{
stmt.executeUpdate("CREATE view " + token + " as select MOLECULE.ID_MOLECULE,PHARMACOLOGY.NSEI,PHARMACOLOGY.NBEI2,MolSearch_tanimoto(FINGERPRINTS.FINGERPRINT,'" + sb.toString() + "') as g from FINGERPRINTS,MOLECULE,PHARMACOLOGY WHERE FINGERPRINTS.ID_TYPE=1 AND MOLECULE.ID_MOLECULE = FINGERPRINTS.ID_MOLECULE AND PHARMACOLOGY.ID_MOLECULE=MOLECULE.ID_MOLECULE;");

			}
                        ResultSet rs2 = stmt2.executeQuery("select * from " + token + " order by g desc limit 100");
			int rc = 0;
                        while (rs2.next()) {
                                salida.println(rs2.getString(1)+"#"+rs2.getString(2)+"#"+rs2.getString(3)+";");
                        }

                        stmt.executeUpdate("DROP VIEW " + token);
                        stmt.close();


                        conn.close();

                }catch (SQLException E) {
                }
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
