import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import org.json.*;


public class LEIgetMolecules extends HttpServlet {


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
        String param2 = request.getParameter("organism");
        String param3 = request.getParameter("database");
        String param_request = request.getParameter("json");



                try {
			String DRIVER = "org.gjt.mm.mysql.Driver";
			Class.forName(DRIVER).newInstance();
                        java.sql.Connection conn;
                        String conn_URL = "jdbc:mysql://"+dbhost+":"+dbport+"/"+dbname+"?user="+user+"&password="+pass;

                        conn = DriverManager.getConnection(conn_URL);

			ResultSet rs2=null;
			PreparedStatement stmt=null;

		if( param_request != null){

			String dir = "asc";
			String sStart = request.getParameter("iDisplayStart");
			String sAmount = request.getParameter("iDisplayLength");
			String sEcho = request.getParameter("sEcho");
			String sCol = request.getParameter("iSortCol_0");
			String sdir = request.getParameter("sSortDir_0");
			String id_search = "";
			String id_target_search = "";
			String id_organism_search = "";
			String name_search = "";
			String smiles_search = "";
			String type_search = "";
			String value_search = "";
     
			id_search = request.getParameter("sSearch_0");
			id_target_search = request.getParameter("sSearch_1");
			id_organism_search = request.getParameter("sSearch_2");
			name_search = request.getParameter("sSearch_3");
			smiles_search = request.getParameter("sSearch_4");
                        type_search = request.getParameter("sSearch_5");
                        value_search = request.getParameter("sSearch_6");
			String[] cols = {"MOLECULE.ID_MOLECULE","MOLECULE.ID_MOLECULE","MOLECULE.ID_MOLECULE","MOLECULE.NAME","MOLECULE.SMILES","PHARMACOLOGY.ID_TYPE","PHARMACOLOGY.VALUE"};
			String field_order = "";
			int ncol = -1;
			int nstart = -1;
			int namount = -1;
			int globalTotal = 0;
			try{
				ncol = Integer.parseInt(sCol);
				nstart = Integer.parseInt(sStart);
				namount = Integer.parseInt(sAmount);
			}catch(Exception e){}

			if (sdir != null) {
			        if (!sdir.equals("asc"))
			            dir = "desc";
			}

			if( ncol >= 0)
			   field_order = " ORDER BY " + cols[ncol] + " " + dir;

			String searchTerm = request.getParameter("sSearch");
			String globeSearch = "";
			if( searchTerm != "")
			{
	        		globeSearch =  " AND (MOLECULE.SMILES like '%"+searchTerm+"%'"
                                + " or MOLECULE.NAME like '%"+searchTerm+"%'"
                                + " or PHARMACOLOGY.VALUE like '%"+searchTerm+"%')";
			}

			JSONObject result = new JSONObject();
			JSONArray array = new JSONArray();
			String limit_stuff = "";
			if( nstart >= 0 && namount >= 10)
				limit_stuff = " limit " + nstart + ", " + namount;

                        if( param != null){
                                if( param2 != null){

					PreparedStatement st2 = conn.prepareStatement("select count(*) FROM MOLECULE,TARGORG,PHARMACOLOGY WHERE PHARMACOLOGY.ID_COMBINATION = TARGORG.ID_COMBINATION AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_MOLECULE = MOLECULE.ID_MOLECULE "+globeSearch);
                                        st2.setString(1, param);
                                        st2.setString(2, param2);
		                        ResultSet rs3=st2.executeQuery();
                        		rs3.next();
                                        globalTotal = rs3.getInt(1);

                                        stmt=conn.prepareStatement("select MOLECULE.ID_MOLECULE, MOLECULE.ID_ORIGINDB, MOLECULE.SMILES, MOLECULE.NAME, PHARMACOLOGY.VALUE, PHARMACOLOGY.ID_TYPE FROM MOLECULE,TARGORG,PHARMACOLOGY WHERE PHARMACOLOGY.ID_COMBINATION = TARGORG.ID_COMBINATION AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_MOLECULE = MOLECULE.ID_MOLECULE "+globeSearch + field_order + limit_stuff);	
                                        stmt.setString(1, param);
                                        stmt.setString(2, param2);
                                }else{
                                        stmt=conn.prepareStatement("select MOLECULE.ID_MOLECULE, MOLECULE.ID_ORIGINDB, MOLECULE.SMILES, MOLECULE.NAME FROM MOLECULE,TARGORG,PHARMACOLOGY WHERE PHARMACOLOGY.ID_COMBINATION = TARGORG.ID_COMBINATION AND TARGORG.ID_TARGET = ?  AND PHARMACOLOGY.ID_MOLECULE = MOLECULE.ID_MOLECULE"+globeSearch + field_order + limit_stuff);
                                        stmt.setString(1, param);
                                }
                        }else{
                                stmt=conn.prepareStatement("select MOLECULE.ID_MOLECULE, MOLECULE.ID_ORIGINDB, MOLECULE.SMILES, MOLECULE.NAME FROM MOLECULE"+field_order + limit_stuff);
                        }
                        rs2=stmt.executeQuery();
			int total = 0;
                        while (rs2.next()) {
                        JSONArray ja = new JSONArray();
                                        ja.put(rs2.getString(1) + "_" + rs2.getInt(6)+"_"+param.trim()+"_"+param2.trim()+"_"+rs2.getDouble(5));
					ja.put(param);
					ja.put(param2);
                                        ja.put(rs2.getString(4));
                                        ja.put(rs2.getString(3));
					if( rs2.getInt(6) == 1)
                                        ja.put( "Kd");
					else if( rs2.getInt(6) == 2)
                                        ja.put( "Ki");
                                        else if( rs2.getInt(6) == 3)
                                        ja.put( "IC50");
                                        else if( rs2.getInt(6) == 4)
                                        ja.put( "EC50/IC50 ratio");
                                        else if( rs2.getInt(6) == 5)
                                        ja.put( "dG<sub>0</sub>");

                                        ja.put( rs2.getString(5));
					array.put(ja);
			++total;
			}

                result.put("iTotalRecords", globalTotal);
                result.put("iTotalDisplayRecords", globalTotal);
                result.put("aaData", array);
                response.setContentType("application/json");
                response.setHeader("Cache-Control", "no-store");
                salida.print(result);


		}else{

                        if( param != null){
				if( param2 != null){
					stmt=conn.prepareStatement("select MOLECULE.ID_MOLECULE, MOLECULE.ID_ORIGINDB, MOLECULE.SMILES, MOLECULE.NAME FROM MOLECULE,TARGORG,PHARMACOLOGY WHERE PHARMACOLOGY.ID_COMBINATION = TARGORG.ID_COMBINATION AND TARGORG.ID_TARGET = ? AND TARGORG.ID_ORGANISM = ? AND PHARMACOLOGY.ID_MOLECULE = MOLECULE.ID_MOLECULE");
					stmt.setString(1, param);
                                        stmt.setString(2, param2);
				}else{
					stmt=conn.prepareStatement("select MOLECULE.ID_MOLECULE, MOLECULE.ID_ORIGINDB, MOLECULE.SMILES, MOLECULE.NAME FROM MOLECULE,TARGORG,PHARMACOLOGY WHERE PHARMACOLOGY.ID_COMBINATION = TARGORG.ID_COMBINATION AND TARGORG.ID_TARGET = ?  AND PHARMACOLOGY.ID_MOLECULE = MOLECULE.ID_MOLECULE");



                                        stmt.setString(1, param);
				}
                        }else{
                                stmt=conn.prepareStatement("select MOLECULE.ID_MOLECULE, MOLECULE.ID_ORIGINDB, MOLECULE.SMILES, MOLECULE.NAME FROM MOLECULE");
                        }
                        rs2=stmt.executeQuery();
		

                        while (rs2.next()) {
	                                salida.println(rs2.getString(1)+"|"+rs2.getString(2)+"|"+rs2.getString(3)+"|"+rs2.getString(4)+";");
                        }
                        conn.close();

		}

		salida.close();

                }catch (SQLException E) {
			salida.println("ERROR. 2434632.\n");
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
