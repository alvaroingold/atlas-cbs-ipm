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


public class LEIaddCompound extends HttpServlet {


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
        String param_idtarget = request.getParameter("idtarget").trim();
        String param_idorganism = request.getParameter("idorganism").trim();
        String param_compound = request.getParameter("compound").trim();
        String param_smiles = request.getParameter("smiles").trim();
        String param_type = request.getParameter("type").trim();
        String param_kvalue = request.getParameter("kvalue").trim();



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


	// Auth validation
	
            if( !(aut != null))
	    {
                salida.println("ERROR: Auth required. Please login <A HREF=\"/examples/jsp/LEI/login.jsp\">here</A> first");
		return;
	    }


	// Validate parameters

	double k = 1;
	try{
	k = Double.parseDouble(param_kvalue);
	}catch(Exception e){
		salida.println("ERROR: 101. Error on parameters");
		return;
	}

	int idtarget = -1;
	int idorganism = -1;

        try{
                idtarget = Integer.parseInt(param_idtarget);
                idorganism = Integer.parseInt(param_idorganism);
                id_origin = Integer.parseInt(id_origin_param);
        }catch(Exception e){
                salida.println("ERROR: 102. Error on parameters");
                return;
        }

	//salida.println("Target: " + idtarget + " - Organism: " + idorganism);

        if( id_origin < 0 || idtarget < 0 || idorganism < 0)
        {
                salida.println("ERROR: 103. Error on parameters");
                return;
        }


	if( param_smiles == null || param_compound == null || param_type == null)
	{
                salida.println("ERROR: 104. Error on parameters");
                return;
	}

	int ktype = -1;

	if( param_type.indexOf("Kd") >= 0){
		ktype = 1;
	}else if ( param_type.indexOf("Ki") >= 0){
		ktype = 2;
	}else if ( param_type.indexOf("IC50") >= 0){
                ktype = 3;
        }else if ( param_type.indexOf("ratio") >= 0){
                ktype = 4;
        }else if ( param_type.indexOf("G") >= 0){
                ktype = 5;
        }

	
	double logp = 0.0;
	double psa = 0.0;
	double mass = 0.0;
	int nhea = 0;
	int npol = 0;
	int natoms = 0;
	int molid = -1;

	try{
        SmilesParser smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer ac = smilesParser.parseSmiles(param_smiles);

        TPSADescriptor psa_desc = new TPSADescriptor();
        ALOGPDescriptor logp_desc  = new ALOGPDescriptor();
        CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(ac.getBuilder());
        adder.addImplicitHydrogens(ac);

        DescriptorValue retval = logp_desc.calculate(ac);
        IDescriptorResult result = retval.getValue();
        DoubleArrayResult array = (DoubleArrayResult)result;
        logp = array.get(1);

        retval = psa_desc.calculate(ac);
        result = retval.getValue();
        psa = Double.parseDouble(result.toString());

        nhea = ac.getAtomCount();

		SMARTSQueryTool querytool = new SMARTSQueryTool("[N,n,O,o]");
        boolean status = querytool.matches(ac);
        if (status) { npol = querytool.countMatches(); }


        org.openscience.cdk.tools.manipulator.AtomContainerManipulator.convertImplicitToExplicitHydrogens(ac);
        mass = org.openscience.cdk.tools.manipulator.AtomContainerManipulator.getNaturalExactMass(ac);
	natoms = ac.getAtomCount();

	//salida.println("K: " + k);
	//salida.println("PSA: " + psa + " - LogP: " + logp + " - NHEA: " + nhea + " - NPOL: " + npol + " - Mass: " + mass);


	double bei, sei, nsei, nbei, nbei2, mbei;
	double pk = 0.0;
	bei = sei = nsei = nbei = nbei2 = mbei = 0.0;

	pk = -Math.log10(k);

	if( mass > 0.0)
	{
		bei = pk / (mass / 1000.0);
		mbei = - Math.log10 ( k / mass);
	}

	if( psa != 0.0)
	{
		sei = pk / (psa / 100);
	}

	if( npol > 0)
	{
		nsei = pk / npol;
	}

	if( nhea > 0)
	{
		nbei = pk / nhea;
		nbei2 = - Math.log10( k / nhea);
	}


        Connection conn;
        PreparedStatement stmt = null;


	//salida.println("End of CDK stuff and starting DB stuff");

        conn = DriverManager.getConnection("jdbc:mysql://" + dbhost + ":"+dbport+"/" + dbname +"?user="+user+"&password="+pass);

        BDB bind = new BDB("jdbc:mysql://" + dbhost +":"+dbport+ "/" + dbname +"?user="+user+"&password="+pass);
        bind.connect();


//        if( (molid = bind.existMolecule(id_origin,param_compound)) < 0)
//        {
          stmt = conn.prepareStatement("INSERT INTO MOLECULE (ID_ORIGINDB, SMILES, NAME) VALUES (?,?,?)", Statement.RETURN_GENERATED_KEYS);
          stmt.setInt(1, id_origin);
          stmt.setString(2, param_smiles);
          stmt.setString(3, param_compound);
          stmt.executeUpdate();
          ResultSet rs_m = null;
          rs_m = stmt.getGeneratedKeys();
          if (rs_m.next()) {
                   molid = rs_m.getInt(1);
          }
          rs_m.close();

          PreparedStatement stmt_mass = null;
          PreparedStatement stmt_npol = null;
          PreparedStatement stmt_natoms = null;
          PreparedStatement stmt_heavy = null;
          PreparedStatement stmt_psa = null;

          //salida.println("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES ("+ molid + ",2," + mass+")");

          stmt_mass = conn.prepareStatement("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES (?,2,?)", Statement.RETURN_GENERATED_KEYS);
          stmt_mass.setInt(1, molid);
          stmt_mass.setDouble(2, mass);
          stmt_mass.executeUpdate();

          stmt_npol = conn.prepareStatement("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES (?,1,?)", Statement.RETURN_GENERATED_KEYS);
          stmt_npol.setInt(1, molid);
          stmt_npol.setDouble(2, (double) npol);
          stmt_npol.executeUpdate();

          stmt_natoms = conn.prepareStatement("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES (?,3,?)", Statement.RETURN_GENERATED_KEYS);
          stmt_natoms.setInt(1, molid);
          stmt_natoms.setDouble(2, (double) natoms);
          stmt_natoms.executeUpdate();

          stmt_heavy = conn.prepareStatement("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES (?,4,?)", Statement.RETURN_GENERATED_KEYS);
          stmt_heavy.setInt(1, molid);
          stmt_heavy.setDouble(2, (double) nhea);
          stmt_heavy.executeUpdate();

          stmt_psa = conn.prepareStatement("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES (?,5,?)", Statement.RETURN_GENERATED_KEYS);
          stmt_psa.setInt(1, molid);
          stmt_psa.setDouble(2, psa);
          stmt_psa.executeUpdate();
//	}


	int id_combination = -1;
        id_combination = bind.existCombination(idtarget, idorganism);

        if( id_combination < 0)
        {

		//salida.println("Adding combination");
        	stmt = conn.prepareStatement("INSERT INTO TARGORG(ID_TARGET,ID_ORGANISM) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
	        stmt.setInt(1, idtarget);
                stmt.setInt(2, idorganism);
	        stmt.executeUpdate();
	        ResultSet rs_m2 = null;
	        rs_m2 = stmt.getGeneratedKeys();
	        if (rs_m2.next()) {
                   id_combination = rs_m2.getInt(1);
 	        }
      	    	rs_m2.close();
       }

       int id_pharmacology = -1;
       id_pharmacology = bind.existPharmacology(id_combination,molid,ktype);
       if( id_pharmacology  < 0)
       {
	    stmt = conn.prepareStatement("INSERT INTO PHARMACOLOGY(ID_COMBINATION,ID_MOLECULE,ID_TYPE,PH,TEMP,VALUE) VALUES (?,?,?,7.0,298.0,?)", Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, id_combination);
            stmt.setInt(2, molid);
            stmt.setInt(3, ktype);
            stmt.setDouble(4, k);
            stmt.executeUpdate();
            ResultSet rs_m3 = null;
            rs_m3 = stmt.getGeneratedKeys();
            if (rs_m3.next()) {
                 id_pharmacology = rs_m3.getInt(1);
            }
            rs_m3.close();
       }

       if( bind.existLEI(id_pharmacology,2) < 0)
       {
                   stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,2,?)");
	           stmt.setInt(1, id_pharmacology);
                   stmt.setDouble(2, bei);
                   stmt.executeUpdate();
       }

       if( bind.existLEI(id_pharmacology,3) < 0)
       {
                   stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,3,?)");
                   stmt.setInt(1, id_pharmacology);
                   stmt.setDouble(2, sei);
                   stmt.executeUpdate();
       }

       if( bind.existLEI(id_pharmacology,4) < 0)
       {
                   stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,4,?)");
                   stmt.setInt(1, id_pharmacology);
                   stmt.setDouble(2, nsei);
                   stmt.executeUpdate();
       }

       if( bind.existLEI(id_pharmacology,5) < 0)
       {
                   stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,5,?)");
                   stmt.setInt(1, id_pharmacology);
                   stmt.setDouble(2, nbei);
                   stmt.executeUpdate();
       }

       if( bind.existLEI(id_pharmacology,6) < 0)
       {
                   stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,6,?)");
                   stmt.setInt(1, id_pharmacology);
                   stmt.setDouble(2, nbei2);
                   stmt.executeUpdate();
       }

       if( bind.existLEI(id_pharmacology,7) < 0)
       {
                   stmt = conn.prepareStatement("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES (?,7,?)");
                   stmt.setInt(1, id_pharmacology);
                   stmt.setDouble(2, mbei);
                   stmt.executeUpdate();
       }


	//salida.println("BEI: " + bei + " - SEI: " + sei + " - NSEI: " + nsei + " - NBEI " + nbei + " - nBEI: " + nbei2 + " - mBEI: " + mbei);


	}catch( Exception e){
		salida.println("Errorcillo");
	StringWriter sw = new StringWriter();
	e.printStackTrace(new PrintWriter(sw));
		salida.println(sw.toString());
	}


	salida.print(molid+"_"+ktype+"_"+idtarget+"_"+idorganism+"_"+k);
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
