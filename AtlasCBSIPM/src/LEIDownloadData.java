import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;


public class LEIDownloadData extends HttpServlet {


    ResourceBundle rb = ResourceBundle.getBundle("LocalStrings");
    

public static int countOccurrences(String haystack, char needle)
{
    int count = 0;
    for (int i=0; i < haystack.length(); i++)
    {
        if (haystack.charAt(i) == needle)
        {
             count++;
        }
    }
    return count;
}
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


        String paramx = request.getParameter("x");
        String paramy = request.getParameter("y");
        String param_sources = request.getParameter("sources");

	boolean last_one = false;
	int nsources = 0;
	int i = 0;
	

                try {
			String DRIVER = "org.gjt.mm.mysql.Driver";
			Class.forName(DRIVER).newInstance();
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+user+"&password="+pass;
                        conn = DriverManager.getConnection(conn_URL);

			ResultSet rs=null;
			ResultSet rs2=null;
			PreparedStatement stmt=null;
			PreparedStatement stmt2=null;

		if( param_sources == null || paramx == null || paramy == null)
		{
                        response.setContentType("text/csv");
                        response.setHeader("Cache-Control", "no-store");
			salida.print("Error; No parameters found");
			salida.close();
			return;
		}


			nsources = this.countOccurrences(param_sources, ';');

			if( nsources <= 0)
	                {
                         response.setContentType("text/csv");
                         response.setHeader("Cache-Control", "no-store");
                         salida.print("Error; 0 number of sources");
       	                 salida.close();
       	                 return;
	       	        }

			String current_token = "";
			int[] targets = new int[nsources];
                        int[] organisms = new int[nsources];
                        int[] types = new int[nsources];
			int current_ntoken = 0;
			try{
				StringTokenizer st = new StringTokenizer(param_sources,";");
				while( st.hasMoreTokens())
				{
					current_token = st.nextToken();
					StringTokenizer stin = new StringTokenizer(current_token,"#");
					targets[current_ntoken] = Integer.parseInt(stin.nextToken());
                                        organisms[current_ntoken] = Integer.parseInt(stin.nextToken());
                                        types[current_ntoken] = Integer.parseInt(stin.nextToken());

					++current_ntoken;
				}

			}catch(Exception e){
	                        response.setContentType("text/csv");
	                        response.setHeader("Cache-Control", "no-store");
	                        salida.print("Error; Error parsing sources");
				salida.close();
				return;
			}

				String x_name = "";
				String y_name = "";

                                stmt=conn.prepareStatement("SELECT LEI_TYPE.NAME FROM LEI_TYPE WHERE ID_TYPE = ?");
                                stmt.setString(1, paramx);
                                rs=stmt.executeQuery();
				if( rs.next())
				{
					x_name = rs.getString(1);
				}
				
                                stmt=conn.prepareStatement("SELECT LEI_TYPE.NAME FROM LEI_TYPE WHERE ID_TYPE = ?");
                                stmt.setString(1, paramy);
                                rs=stmt.executeQuery();
                                if( rs.next())
                                {
                                        y_name = rs.getString(1);
                                }

                                salida.println("Name,SMILES,Type,Value,pK,"+x_name+","+y_name);



			for( i = 0; i < nsources; i++)
			{

				if( i+1 == nsources)
					last_one = true;


                                        stmt=conn.prepareStatement("select MOLECULE.NAME,MOLECULE.SMILES, LEI.VALUE,PHARMA_TYPE.NAME, PHARMACOLOGY.VALUE,LEI_TYPE.NAME  FROM MOLECULE,TARGORG,PHARMACOLOGY,LEI,PHARMA_TYPE,LEI_TYPE WHERE PHARMACOLOGY.ID_COMBINATION = TARGORG.ID_COMBINATION AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_MOLECULE = MOLECULE.ID_MOLECULE AND PHARMACOLOGY.ID_TYPE = ? AND LEI.ID_TYPE = ? AND PHARMACOLOGY.ID_PHARMACOLOGY = LEI.ID_PHARMACOLOGY AND PHARMA_TYPE.ID_TYPE = PHARMACOLOGY.ID_TYPE AND LEI_TYPE.ID_TYPE = LEI.ID_TYPE");	
                                        stmt.setInt(1, targets[i]);
                                        stmt.setInt(2, organisms[i]);
                                        stmt.setInt(3, types[i]);
                    			stmt.setString(4, paramx);

					stmt2=conn.prepareStatement("select MOLECULE.NAME,MOLECULE.SMILES, LEI.VALUE, PHARMA_TYPE.NAME, PHARMACOLOGY.VALUE,LEI_TYPE.NAME  FROM MOLECULE,TARGORG,PHARMACOLOGY,LEI,PHARMA_TYPE,LEI_TYPE WHERE PHARMACOLOGY.ID_COMBINATION = TARGORG.ID_COMBINATION AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_MOLECULE = MOLECULE.ID_MOLECULE AND PHARMACOLOGY.ID_TYPE = ? AND LEI.ID_TYPE = ? AND PHARMACOLOGY.ID_PHARMACOLOGY = LEI.ID_PHARMACOLOGY AND PHARMA_TYPE.ID_TYPE = PHARMACOLOGY.ID_TYPE AND LEI_TYPE.ID_TYPE = LEI.ID_TYPE");	
                                        stmt2.setInt(1, targets[i]);
                                        stmt2.setInt(2, organisms[i]);
                                        stmt2.setInt(3, types[i]);
                    			stmt2.setString(4, paramy);
					
	                        rs=stmt.executeQuery();
	                        rs2=stmt2.executeQuery();
	                        int total = 0;

                                response.setContentType("text/csv");
                                response.setHeader("Cache-Control", "no-store");

	                        while (rs.next()) 
				{
					String type = "";
		                        rs2.next();
/*	                                salida.println(rs.getFloat(3)+";"+rs2.getFloat(3)+";"+rs.getString(1)+";"+rs.getString(2));*/
	                                salida.println("\""+rs.getString(1)+"\","+rs.getString(2)+","+rs.getString(4)+","+rs.getFloat(5)+","+(-Math.log10(rs.getFloat(5)))+","+rs.getFloat(3)+","+rs2.getFloat(3));

		                        ++total;
	       	                }

		                response.setContentType("text/csv");
		                response.setHeader("Cache-Control", "no-store");


			}




		salida.close();

                }catch (SQLException E) {
			salida.println("ERROR. 2434632.\n");
			E.printStackTrace();
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
