import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.tools.*;
import org.openscience.cdk.qsar.*;
import org.openscience.cdk.qsar.descriptors.molecular.*;
import org.openscience.cdk.qsar.result.*;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smiles.smarts.*;

//
//public class LEIuploadCSV extends HttpServlet {
//
//
//    ResourceBundle rb = ResourceBundle.getBundle("LocalStrings");
//    
//
//   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//    throws ServletException, IOException {
//        response.setContentType("text/html");
//
//        PrintWriter salida = response.getWriter();
//
//        ServletContext servletContext = getServletContext();
//        String user = servletContext.getInitParameter("user").trim();
//        String pass = servletContext.getInitParameter("password").trim();
//        String dbname = servletContext.getInitParameter("dbname").trim();
//        String dbhost = servletContext.getInitParameter("dbhost").trim();
//        String dbport = servletContext.getInitParameter("dbport").trim();
//
//
//        String param = request.getParameter("target");
//        String param2 = request.getParameter("organism");
//
//        InputStream is = null;
//
//	int id_target = -1;
//	int id_organism = -1;
//	int id_origin = -1;
//
//        HttpSession session;
//	String aut = "";
//	String iduser_session ="";
//	String username_session ="";
//	String id_origin_param = "";
//	
//
//        try{
//            session = request.getSession(true);
//            aut = (String) session.getAttribute("authorized");
//            iduser_session = (String) session.getAttribute("id_user");
//            username_session = (String) session.getAttribute("name");
//            id_origin_param = (String) session.getAttribute("id_origin");
//
//        }catch( Exception e){
//                salida.print("{success: false}");
//        StringWriter sw = new StringWriter();
//        e.printStackTrace(new PrintWriter(sw));
//                salida.println(sw.toString());
//                return;
//        }
//
//           if( !(aut != ""))
//            {
//                salida.print("{success: false}");
//                salida.println("AUT:" + aut);
//                return;
//            }
//
//
//
//
//	try{
//		id_target = Integer.parseInt(param);
//                id_organism = Integer.parseInt(param2);
//		id_origin = Integer.parseInt(id_origin_param);
//	}catch(Exception e){
//		salida.print("{success: false}"); 
//        StringWriter sw = new StringWriter();
//        e.printStackTrace(new PrintWriter(sw));
//                salida.println(sw.toString());
//		return;
//	}
//
//
//	if( id_target < 0 || id_organism < 0 || id_origin < 0)
//	{
//                salida.print("{success: false}");
//		salida.println("Target: " +id_target+ " Organism: " +id_organism + " id_origin: " + id_origin);
//                return;
//	}
//
//        try {
//            is = request.getInputStream();
//            BufferedReader bis= new BufferedReader( new InputStreamReader(is));
//	    String line = "";
//	    while ((line = bis.readLine()) != null) {
//	      if( line.indexOf(";") > 0 && !(line.indexOf("Content-Disposition:") >= 0) && !( line.indexOf("Content-Type: ") >= 0))
//	      {
//		  
////		if( line.indexOf(";") <= 0) { salida.print("{success: false}"); salida.println("No ;"); salida.println("Linea: " + line); return; }
//
//		String name = "";
//		String smiles = "";
//		String ktype = "";
//		String kvalue = "";
//
//		int type = -1;
//		double value = 0.0;
//
//		try {
//			StringTokenizer st = new StringTokenizer(line,";");
//			if( st.hasMoreTokens() ){
//				name = st.nextToken();
//			}else{
//	                        salida.print("{success: false}");
//				salida.print(line);
//	                        continue;
//			}
//                        if( st.hasMoreTokens() ){
//                                smiles = st.nextToken();
//                        }else{
//                                salida.print("{success: false}");
//                                salida.print(line);
//                                continue;
//                        }
//                        if( st.hasMoreTokens() ){
//                                ktype = st.nextToken();
//                        }else{
//                                salida.print("{success: false}");
//                                salida.print(line);
//                                continue;
//                        }
//                        if( st.hasMoreTokens() ){
//                                kvalue = st.nextToken();
//                        }else{
//                                salida.print("{success: false}");
//                                salida.print(line);
//                                continue;
//                        }
//
//		        if( ktype.compareToIgnoreCase("Kd") == 0){
//       			        type = 1;
//		        }else if ( ktype.compareToIgnoreCase("Ki") == 0){
//		                type = 2;
//		        }else if ( ktype.compareToIgnoreCase("ic50") == 0){
//		                type = 3;
//		        }else if ( ktype.compareToIgnoreCase("ratio") == 0){
//		                type = 4;
//		        }else if ( ktype.compareToIgnoreCase("dG0") == 0){
//		                type = 5;
//		        }
//
//			value = Double.parseDouble( kvalue );
//
//			if( type < 0 || value == 0.0)
//			{
//	                        salida.print("{success: false},");
//                                salida.print("type: "+type+" value: "+value);
//
//				continue;
//			}
//
//
//		        double logp = 0.0;
//		        double psa = 0.0;
//		        double mass = 0.0;
//			  double rconst = 0.00198; //R-constant (Kilocal/mol)
//                    double tkelvin = 300.;   //Temperature for DeltaG
//		        int nhea = 0;
//		        int npol = 0;
//		        int natoms = 0;
//		        int molid = -1;
//
//		        SmilesParser smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
//		        IAtomContainer ac = smilesParser.parseSmiles(smiles);
//		
//		        TPSADescriptor psa_desc = new TPSADescriptor();
//		        ALOGPDescriptor logp_desc  = new ALOGPDescriptor();
//		        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(ac.getBuilder());
//		        adder.addImplicitHydrogens(ac);
//		        DescriptorValue retval = logp_desc.calculate(ac);
//		        IDescriptorResult result = retval.getValue();
//		        DoubleArrayResult array = (DoubleArrayResult)result;
//		        logp = array.get(1);
//
//		        retval = psa_desc.calculate(ac);
//		        result = retval.getValue();
//		        psa = Double.parseDouble(result.toString());
//
//		        nhea = ac.getAtomCount();
//
//		        SMARTSQueryTool querytool = new SMARTSQueryTool("[O,N]");
//		        boolean status = querytool.matches(ac);
//		        if (status) { npol = querytool.countMatches(); }
//
//		        org.openscience.cdk.tools.manipulator.AtomContainerManipulator.convertImplicitToExplicitHydrogens(ac);
//		        mass = org.openscience.cdk.tools.manipulator.AtomContainerManipulator.getNaturalExactMass(ac);
//		        natoms = ac.getAtomCount();
//
//		        double bei, sei, nsei, nbei, nbei2, mbei, leh;
//		        double pk = 0.0;
//                    double pkh = 0.0;
//		        bei = sei = nsei = nbei = nbei2 = mbei = leh = 0.0;
//
//			value = value / 1000000000.0; /* From nM to M */
//
//			if( value > 0){
//			        pk = - Math.log10(value);
//                          pkh = - Math.log(value);
//                          leh = rconst*tkelvin*pkh;
//			}else if( value < 0){
//				  pkh = - Math.log(Math.abs(value));
//				  leh = rconst*tkelvin*pkh;
//	                    pk = - Math.log10(Math.abs(value));
//			}else{
//				pk = 0.0;
//				value = 1.0;
//			}
//		        if( mass > 0.0)
//		        {
//		                bei = pk / (mass / 1000.0);
//		                mbei = - Math.log10 ( value / mass);
//		        }
//
//		        if( psa != 0.0)
//		        {
//		                sei = pk / (psa / 100);
//		        }
//
//		        if( npol > 0)
//		        {
//		                nsei = pk / npol;
//		        }
//
//		        if( nhea > 0)
//		        {
//		                nbei = pk / nhea;
//		                nbei2 = - Math.log10( value / nhea);
//				    leh = pkh / nhea;
//		        }
//
//		        Connection conn;
//		        PreparedStatement stmt = null;
//
//		        conn = DriverManager.getConnection("jdbc:mysql://" + dbhost + ":" +dbport+ "/" + dbname +"?user="+user+"&password="+pass);
//		        BDB bind = new BDB("jdbc:mysql://" + dbhost + ":" + dbport+ "/" + dbname +"?user="+user+"&password="+pass);
//		        bind.connect();
//
//                        stmt = conn.prepareStatement("INSERT INTO MOLECULE (ID_ORIGINDB, SMILES, NAME) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
//                        stmt.setInt(1, id_origin);
//                        stmt.setString(2, smiles);
//                        stmt.setString(3, name);
//                        stmt.executeUpdate();
//                        ResultSet rs_m = null;
//                        rs_m = stmt.getGeneratedKeys();
//                        if (rs_m.next()) {
//                                 molid = rs_m.getInt(1);
//                        }
//                        rs_m.close();
//
//                        PreparedStatement stmt_mass = null;
//                        PreparedStatement stmt_npol = null;
//                        PreparedStatement stmt_natoms = null;
//                        PreparedStatement stmt_heavy = null;
//                        PreparedStatement stmt_psa = null;
//
//                        stmt_mass = conn.prepareStatement("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES (?,2,?)", Statement.RETURN_GENERATED_KEYS);
//                        stmt_mass.setInt(1, molid);
//                        stmt_mass.setDouble(2, mass);
//                        stmt_mass.executeUpdate();
//
//                        stmt_npol = conn.prepareStatement("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES (?,1,?)", Statement.RETURN_GENERATED_KEYS);
//                        stmt_npol.setInt(1, molid);
//                        stmt_npol.setDouble(2, (double) npol);
//                        stmt_npol.executeUpdate();
//
//                        stmt_natoms = conn.prepareStatement("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES (?,3,?)", Statement.RETURN_GENERATED_KEYS);
//                        stmt_natoms.setInt(1, molid);
//                        stmt_natoms.setDouble(2, (double) natoms);
//                        stmt_natoms.executeUpdate();
//
//                        stmt_heavy = conn.prepareStatement("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES (?,4,?)", Statement.RETURN_GENERATED_KEYS);
//                        stmt_heavy.setInt(1, molid);
//                        stmt_heavy.setDouble(2, (double) nhea);
//                        stmt_heavy.executeUpdate();
//
//                        stmt_psa = conn.prepareStatement("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES (?,5,?)", Statement.RETURN_GENERATED_KEYS);
//                        stmt_psa.setInt(1, molid);
//                        stmt_psa.setDouble(2, psa);
//                        stmt_psa.executeUpdate();
//
//                      int id_combination = -1;
//                      id_combination = bind.existCombination(id_target, id_organism);
//
//                      if( id_combination < 0)
//                      {
//                              stmt = conn.prepareStatement("INSERT INTO TARGORG(ID_TARGET,ID_ORGANISM) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
//                              stmt.setInt(1, id_target);
//                              stmt.setInt(2, id_organism);
//                              stmt.executeUpdate();
//                              ResultSet rs_m2 = null;
//                              rs_m2 = stmt.getGeneratedKeys();
//                              if (rs_m2.next()) {
//                                 id_combination = rs_m2.getInt(1);
//                              }
//                              rs_m2.close();
//                     }
//                     int id_pharmacology = -1;
//                     id_pharmacology = bind.existPharmacology(id_combination,molid,type);
//                     if( id_pharmacology  < 0)
//                     {
//                          stmt = conn.prepareStatement("INSERT INTO PHARMACOLOGY(ID_COMBINATION,ID_MOLECULE,ID_TYPE,PH,TEMP,VALUE) VALUES (?,?,?,7.0,298.0,?)", Statement.RETURN_GENERATED_KEYS);
//                          stmt.setInt(1, id_combination);
//                          stmt.setInt(2, molid);
//                          stmt.setInt(3, type);
//                          stmt.setDouble(4, value);
//                          stmt.executeUpdate();
//                          ResultSet rs_m3 = null;
//                          rs_m3 = stmt.getGeneratedKeys();
//                          if (rs_m3.next()) {
//                               id_pharmacology = rs_m3.getInt(1);
//                          }
//                          rs_m3.close();
//
//                     }
//
///*
//			leh = Math.abs(leh);
//			bei = Math.abs(bei);
//			sei =Math.abs(sei);
//			nbei = Math.abs(nbei);
//			nbei2 = Math.abs(nbei2);
//			mbei = Math.abs(mbei);
//			nsei = Math.abs(nsei);*/
//
//			   if( bind.existLEI(id_pharmacology,1) < 0)
//                     {
//                                 stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,1,?)");
//                                 stmt.setInt(1, id_pharmacology);
//                                 stmt.setDouble(2, leh);
//                                 stmt.executeUpdate();
//                     }
//
//
//                     if( bind.existLEI(id_pharmacology,2) < 0)
//                     {
//                                 stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,2,?)");
//                                 stmt.setInt(1, id_pharmacology);
//                                 stmt.setDouble(2, bei);
//                                 stmt.executeUpdate();
//                     }
//
//		       if( bind.existLEI(id_pharmacology,3) < 0)
//		       {
//		                   stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,3,?)");
//		                   stmt.setInt(1, id_pharmacology);
//		                   stmt.setDouble(2, sei);
//		                   stmt.executeUpdate();
//		       }
//	
//		       if( bind.existLEI(id_pharmacology,4) < 0)
//		       {
//       		            stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,4,?)");
//       		            stmt.setInt(1, id_pharmacology);
//       		            stmt.setDouble(2, nsei);
//       		            stmt.executeUpdate();
//       		       }
//
//       		       if( bind.existLEI(id_pharmacology,5) < 0)
//		       {
//		                   stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,5,?)");
//		                   stmt.setInt(1, id_pharmacology);
//		                   stmt.setDouble(2, nbei);
//		                   stmt.executeUpdate();
//		       }
//
//		       if( bind.existLEI(id_pharmacology,6) < 0)
//		       {
//		                   stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,6,?)");
//		                   stmt.setInt(1, id_pharmacology);
//		                   stmt.setDouble(2, nbei2);
//		                   stmt.executeUpdate();
//		       }
//
//		       if( bind.existLEI(id_pharmacology,7) < 0)
//		       {
//		                   stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,7,?)");
//		                   stmt.setInt(1, id_pharmacology);
//		                   stmt.setDouble(2, mbei);
//		                   stmt.executeUpdate();
//		       }
//
//		}catch (Exception e){
//            		salida.print("{success: false}");
//        StringWriter sw = new StringWriter();
//        e.printStackTrace(new PrintWriter(sw));
//                salida.println(sw.toString());
//
//			continue;
//		}
//
//              } // End IF
//            } //End While
//
//            salida.print("{success: true}");
//        } catch (FileNotFoundException e) {
//            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
//            salida.print("{success: false}");
//        StringWriter sw = new StringWriter();
//        e.printStackTrace(new PrintWriter(sw));
//                salida.println(sw.toString());
//        } catch (IOException e) {
//            response.setStatus(response.SC_INTERNAL_SERVER_ERROR);
//            salida.print("{success: false}");
//        StringWriter sw = new StringWriter();
//        e.printStackTrace(new PrintWriter(sw));
//                salida.println(sw.toString());
//        } finally {
//            try {
//                is.close();
//            } catch (IOException ignored) {
//            }
//        }
//
//	salida.close();
//    }
//
//
//    public void doGet(HttpServletRequest request,
//                      HttpServletResponse response)
//        throws IOException, ServletException
//    {
//        processRequest(request, response);
//    }
//
//    public void doPost(HttpServletRequest request,
//                      HttpServletResponse response)
//        throws IOException, ServletException
//    {
//        doGet(request, response);
//    }
//
//}
