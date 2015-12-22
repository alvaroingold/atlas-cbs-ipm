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


public class LEIupdateCompound extends HttpServlet {


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


        String id = request.getParameter("id");
        String value = request.getParameter("value");
        String columnName = request.getParameter("columnName");
        String columnId = request.getParameter("columnId");
        String columnPosition = request.getParameter("columnPosition");
        String rowId = request.getParameter("rowId");

        PrintWriter out = response.getWriter();

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

	int id_molecule = -1;
	int id_field = -1;
	int id_type = -1;
	int id_target = -1;
	int id_organism = -1;
	double k = 0.0;
	try{
		StringTokenizer mol = new StringTokenizer(id,"_");	
		if( mol.hasMoreTokens()){
        		id_molecule  = Integer.parseInt(mol.nextToken());
			id_type = Integer.parseInt(mol.nextToken());
                        id_target = Integer.parseInt(mol.nextToken());
                        id_organism = Integer.parseInt(mol.nextToken());
			k = Double.parseDouble(mol.nextToken());

		}else{
	                salida.println("ERROR: 101. Error on parameters");
			return;
		}
		id_field = Integer.parseInt(columnPosition);

	}catch(Exception e){
		salida.println("ERROR: 101. Error on parameters");
		return;
	}

	//salida.println("Target: " + idtarget + " - Organism: " + idorganism);

        if( id_molecule < 0 || id_field < 0 || id_type < -1)
        {
                salida.println("ERROR: 103. Error on parameters");
                return;
        }

	try{
	if( id_field == 0){ // Just the name. ok. That's easy.

	        Connection conn;
	        PreparedStatement stmt = null;

	        conn = DriverManager.getConnection("jdbc:mysql://" + dbhost +":"+dbport + "/" + dbname +"?user="+user+"&password="+pass);

	        stmt = conn.prepareStatement("UPDATE MOLECULE SET NAME=? WHERE MOLECULE.ID_MOLECULE=?", Statement.RETURN_GENERATED_KEYS);
	        stmt.setInt(2, id_molecule);
	        stmt.setString(1, value);
	        stmt.executeUpdate();
                salida.print(value);

	}else if( id_field == 2){ // Ktype

	        int id_combination = -1;
	        int id_pharmacology = -1;
		int ktype = -1;
                Connection conn;
                PreparedStatement stmt = null;

                conn = DriverManager.getConnection("jdbc:mysql://" + dbhost +":"+dbport + "/" + dbname +"?user="+user+"&password="+pass);

	        BDB bind = new BDB("jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname +"?user="+user+"&password="+pass);
	        bind.connect();

	        if( value.indexOf("Kd") >= 0){
	                ktype = 1;
	        }else if ( value.indexOf("Ki") >= 0){
	                ktype = 2;
	        }else if ( value.indexOf("IC50") >= 0){
	                ktype = 3;
	        }else if ( value.indexOf("ratio") >= 0){
	                ktype = 4;
	        }else if ( value.indexOf("G") >= 0){
	                ktype = 5;
	        }

		if( ktype > 0){
	        id_combination = bind.existCombination(id_target, id_organism);
	        id_pharmacology = bind.existPharmacology(id_combination,id_molecule,id_type);

	        stmt = conn.prepareStatement("UPDATE PHARMACOLOGY SET ID_TYPE = ? WHERE ID_COMBINATION = ? AND ID_MOLECULE = ?");
		stmt.setInt(1, ktype);
		stmt.setInt(2, id_combination);
		stmt.setInt(3, id_molecule);
		stmt.executeUpdate();
                salida.print(value);
		}else{
			salida.print("ERROR: not a valid type. Accept: Ki, Kd, IC50, EC50/IC50 ratio and dG");
		}

	}else if( id_field == 3){ // Kvalue

        double logp = 0.0;
        double psa = 0.0;
        double mass = 0.0;
        int nhea = 0;
        int npol = 0;
        int natoms = 0;

        try{
		k = Double.parseDouble(value);

                Connection conn;
                PreparedStatement stmt = null;
		ResultSet SMset = null;
                conn = DriverManager.getConnection("jdbc:mysql://" + dbhost +":"+dbport + "/" + dbname +"?user="+user+"&password="+pass);
		stmt=conn.prepareStatement("select MOLECULE.SMILES FROM MOLECULE WHERE MOLECULE.ID_MOLECULE = ? LIMIT 1");
                stmt.setInt(1, id_molecule);
                SMset=stmt.executeQuery();
		String mysmiles = "";
                if( SMset.next() ){
                  mysmiles = SMset.getString(1);
		}else{
		  salida.print("ERROR: Cannot extract SMILES for the molecule");
		  return;
		}

	        SmilesParser smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
	        IAtomContainer ac = smilesParser.parseSmiles(mysmiles);

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
        }catch(Exception e){
                salida.print("ERROR: Not a valid value for a constant");
                return;
        }


        double bei, sei, nsei, nbei, nbei2, mbei;
        double pk = 0.0;
        bei = sei = nsei = nbei = nbei2 = mbei = 0.0;


        if( k > 0){
        pk = -Math.log10(k);
	}else if( k < 0){
	pk = -Math.log10(Math.abs(k));
        }else{
        pk = 0.0;
        }

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

        conn = DriverManager.getConnection("jdbc:mysql://" + dbhost +":"+dbport + "/" + dbname +"?user="+user+"&password="+pass);
        BDB bind = new BDB("jdbc:mysql://" + dbhost + ":"+dbport + "/" + dbname +"?user="+user+"&password="+pass);
        bind.connect();


        int id_combination = -1;
        int id_pharmacology = -1;

        id_combination = bind.existCombination(id_target, id_organism);
        id_pharmacology = bind.existPharmacology(id_combination,id_molecule,id_type);

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 2");
        stmt.setDouble(1, bei);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 3");
        stmt.setDouble(1, sei);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 4");
        stmt.setDouble(1, nsei);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 5");
        stmt.setDouble(1, nbei);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 6");
        stmt.setDouble(1, nbei2);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 7");
        stmt.setDouble(1, mbei);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();


        stmt = conn.prepareStatement("UPDATE PHARMACOLOGY SET VALUE = ? WHERE ID_COMBINATION = ? AND ID_MOLECULE = ?");
        stmt.setDouble(1, k);
        stmt.setInt(2, id_combination);
        stmt.setInt(3, id_molecule);
        stmt.executeUpdate();
        salida.print(value);

	}else if( id_field == 1){ // SMILES
	
	// 1. Validate SMILES, calc stuff and Update MOLECULE 

        double logp = 0.0;
        double psa = 0.0;
        double mass = 0.0;
        int nhea = 0;
        int npol = 0;
        int natoms = 0;

        try{
        SmilesParser smilesParser = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        IAtomContainer ac = smilesParser.parseSmiles(value);

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
        }catch(Exception e){
                salida.print("ERROR: Property or SMILES parsing have failed");
                return;
        }


        double bei, sei, nsei, nbei, nbei2, mbei;
        double pk = 0.0;
        bei = sei = nsei = nbei = nbei2 = mbei = 0.0;

	
	if( k != 0){
        pk = -Math.log10(k);
	}else{
	pk = 0.0;
	}

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

	conn = DriverManager.getConnection("jdbc:mysql://" + dbhost +":" + dbport + "/" + dbname +"?user="+user+"&password="+pass);
        BDB bind = new BDB("jdbc:mysql://" + dbhost + ":" + dbport + "/" + dbname +"?user="+user+"&password="+pass);
        bind.connect();

	stmt = conn.prepareStatement("UPDATE MOLECULE SET SMILES=? WHERE MOLECULE.ID_MOLECULE=?", Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(2, id_molecule);
        stmt.setString(1, value);
        stmt.executeUpdate();

	// 2. Update properties
	
        PreparedStatement stmt_mass = null;
        PreparedStatement stmt_npol = null;
        PreparedStatement stmt_natoms = null;
        PreparedStatement stmt_heavy = null;
        PreparedStatement stmt_psa = null;

        stmt_mass = conn.prepareStatement("UPDATE PROPS SET VALUE = ? WHERE ID_MOLECULE = ? AND ID_PROP_TYPE = 2");
        stmt_mass.setInt(2, id_molecule);
        stmt_mass.setDouble(1, mass);
        stmt_mass.executeUpdate();


	stmt_npol = conn.prepareStatement("UPDATE PROPS SET VALUE = ? WHERE ID_MOLECULE = ? AND ID_PROP_TYPE = 1");
        stmt_npol.setInt(2, id_molecule);
        stmt_npol.setDouble(1, (double) npol);
        stmt_npol.executeUpdate();

        stmt_natoms = conn.prepareStatement("UPDATE PROPS SET VALUE = ? WHERE ID_MOLECULE = ? AND ID_PROP_TYPE = 3");
        stmt_natoms.setInt(2, id_molecule);
        stmt_natoms.setDouble(1, (double) natoms);
        stmt_natoms.executeUpdate();

        stmt_heavy = conn.prepareStatement("UPDATE PROPS SET VALUE = ? WHERE ID_MOLECULE = ? AND ID_PROP_TYPE = 4");
        stmt_heavy.setInt(2, id_molecule);
        stmt_heavy.setDouble(1, (double) nhea);
        stmt_heavy.executeUpdate();

        stmt_psa = conn.prepareStatement("UPDATE PROPS SET VALUE = ? WHERE ID_MOLECULE = ? AND ID_PROP_TYPE = 5");
        stmt_psa.setInt(2, id_molecule);
        stmt_psa.setDouble(1, psa);
        stmt_psa.executeUpdate();


        // 3. UPDATE LEI records

        int id_combination = -1;
        int id_pharmacology = -1;

        id_combination = bind.existCombination(id_target, id_organism);
        id_pharmacology = bind.existPharmacology(id_combination,id_molecule,id_type);

	stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 2");
        stmt.setDouble(1, bei);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 3");
        stmt.setDouble(1, sei);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 4");
        stmt.setDouble(1, nsei);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 5");
        stmt.setDouble(1, nbei);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 6");
        stmt.setDouble(1, nbei2);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

        stmt = conn.prepareStatement("UPDATE LEI SET VALUE = ? WHERE ID_PHARMACOLOGY = ? AND ID_TYPE = 7");
        stmt.setDouble(1, mbei);
        stmt.setInt(2, id_pharmacology);
        stmt.executeUpdate();

		salida.print(value);
	}else{
		salida.print("Error: Unknow field to update");
	}


        }catch( Exception e){
                salida.println("Errorcillo");
	        StringWriter sw = new StringWriter();
	        e.printStackTrace(new PrintWriter(sw));
                salida.println(sw.toString());
        }



        salida.close();
        return;

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
