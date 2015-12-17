/****************************************
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 *
 */

package atlas2;
import java.io.*;
import java.util.*;
import java.lang.*;
import java.sql.*;

public class insertMolecules_caz {


  public static void main(String[] args) {

    Connection conn;
    Statement stmt = null;
    ResultSet rs = null;

    FileInputStream fis = null;
    BufferedReader br = null;

    String tmp;
    StringBuffer sb1;


    double ki, kd, ph, temp, sei,bei,nsei,nbei,nbei2,mbei;
    double pk;
    int id_pharmacology = -1, id_combination = -1;
    int id_target = -1,id_org2 = -1;
    double pharma_value = 0.0;
    int pharma_type = -1;
    ki = 0.0f;
    kd = 0.0f;
    ph = 0.0f;
    temp = 0.0f;
    pk = 0.0f;
    sei = 0.0;
    bei = 0.0;
    nsei = 0.0;
    nbei = 0.0;
    nbei2 = 0.0;
    mbei = 0.0;
    double leh = 0.0;
    double gascont = 0.00198; // R constant Kcal -caz
    double tkelvin = 300.0; // Absolute temp=300 -caz
    double pkh = 0.0; 

    if( args.length != 4)
    {
	  System.out.println("insertMolecules <LIGANDS> <TARGETS> <PROPERTIES> <PHARMACOLOGY>");
          System.out.println("Error. Incorrect number of parameters");
          System.exit(-1);
    }


   
    try {
       conn = DriverManager.getConnection("jdbc:mysql://clairecita/LEI_OX?user=root&password=alvaro");
          
       PBIND bind = new PBIND( args[1], args[0], args[2], args[3] ); 
       bind.setconnString("jdbc:mysql://clairecita/LEI_OX?user=root&password=alvaro");
       bind.connect();

       fis = new FileInputStream(args[0]);
       br = new BufferedReader(new InputStreamReader(fis));
       while ((tmp = br.readLine()) != null) {

	StringTokenizer line = new StringTokenizer(tmp,";");
	String pdbcode = line.nextToken();
	String curr_smiles;
	String curr_name;
	int id_mol2 = 0;
        curr_smiles = line.nextToken().trim();
        curr_name = line.nextToken().trim();

	System.out.println(curr_smiles);
        String mass = "";
        String polars = "";
	String natom = "";
	String heavy = "";
	String psa_line ="";
	int mol_index = -1;


	if ( (id_mol2 = bind.existMolecule(curr_name)) < 0)
	{
                   sb1 = new StringBuffer("");
		   StringBuffer sb2 = new StringBuffer("");
                   stmt = conn.createStatement();
                   sb1.append("INSERT INTO MOLECULE(SMILES,NAME,ID_ORIGINDB) VALUES ('"+ curr_smiles + "','" + curr_name + "',2)");
                   stmt.executeUpdate(sb1.toString(),Statement.RETURN_GENERATED_KEYS);
                   ResultSet rs_m = null;
                   rs_m = stmt.getGeneratedKeys();
                   if (rs_m.next()) {
                       id_mol2 = rs_m.getInt(1);
                   }
                   rs_m.close();


		mol_index = bind.getIndex(pdbcode);
		mass = bind.getProperty(mol_index,7);
                polars = bind.getProperty(mol_index,5);
                heavy = bind.getProperty(mol_index,4);
                psa_line = bind.getProperty(mol_index,1);
                natom = bind.getProperty(mol_index,8);

		// Insert properties

                   Statement stmt_mass = null;
                   Statement stmt_npol = null;
                   Statement stmt_natoms = null;
                   Statement stmt_heavy = null;
                   Statement stmt_psa = null;


                   sb2 = new StringBuffer("");
                   sb2.append("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES ("+ id_mol2 + ",2," + mass+")");
                   stmt_mass = conn.createStatement();
                   stmt_mass.execute(sb2.toString());

                   sb2 = new StringBuffer("");
                   sb2.append("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES ("+ id_mol2 + ",1," + polars+")");
                   stmt_npol = conn.createStatement();
                   stmt_npol.execute(sb2.toString());

                   sb2 = new StringBuffer("");
                   sb2.append("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES ("+ id_mol2 + ",3," + natom+")");
                   stmt_natoms = conn.createStatement();
                   stmt_natoms.execute(sb2.toString());

                   sb2 = new StringBuffer("");
                   sb2.append("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES ("+ id_mol2 + ",4," + heavy +")");
                   stmt_heavy = conn.createStatement();
                   stmt_heavy.execute(sb2.toString());

                   sb2 = new StringBuffer("");
                   sb2.append("INSERT INTO PROPS(ID_MOLECULE,ID_PROP_TYPE,VALUE) VALUES ("+ id_mol2 + ",5," + psa_line+")");
                   stmt_psa = conn.createStatement();
                   stmt_psa.execute(sb2.toString());

        }else{
                   System.out.println(curr_name+": already in the database.");
        }


                mol_index = bind.getIndex(pdbcode);
                mass = bind.getProperty(mol_index,7);
                polars = bind.getProperty(mol_index,5);
                heavy = bind.getProperty(mol_index,4);
                psa_line = bind.getProperty(mol_index,1);
                natom = bind.getProperty(mol_index,8);


		

		id_org2 = bind.existOrganism("Default");
		id_target = bind.existTarget(bind.getTargetName(pdbcode));

                id_combination = bind.existCombination(id_target, id_org2);

                if( id_combination < 0)
                {
                   sb1 = new StringBuffer("");
                   stmt = conn.createStatement();
                   sb1.append("INSERT INTO TARGORG(ID_TARGET,ID_ORGANISM) VALUES ("+id_target+","+id_org2+")");
                   stmt.executeUpdate(sb1.toString(),Statement.RETURN_GENERATED_KEYS);
                   ResultSet rs_m = null;
                   id_combination = -1;
                   rs_m = stmt.getGeneratedKeys();
                   if (rs_m.next()) {
                        id_combination = rs_m.getInt(1);
                   }

                  rs_m.close();
                }

                System.out.println("Combination is: " + id_combination);
		String[] pharma_res = bind.getPharmacology(pdbcode);

		// CAZ- added lines to get values of ki,kd for LEHs
		// and to compute the corresponding LEH (from kd, or ki)
		// Not sure this is the right place to get everything right

                if( Integer.valueOf(pharma_res[0]) == 1){
                  kd = Double.valueOf(pharma_res[1]);
			pkh = -Math.log(kd);
                	pkh = gascont*tkelvin*pkh;
                	leh = pkh/Double.valueOf(heavy);
                  pk = -Math.log10( Double.valueOf(pharma_res[1]));
                  pharma_value = Double.valueOf(pharma_res[1]);
                  pharma_type = 1;
                }else if( Integer.valueOf(pharma_res[0]) == 0){
                  ki = Double.valueOf(pharma_res[1]);
			pkh = -Math.log(ki);
                	pkh = gascont*tkelvin*pkh;
                	leh = pkh/Double.valueOf(heavy);
                  pk = -Math.log10( Double.valueOf(pharma_res[1]));
                  pharma_value = Double.valueOf(pharma_res[1]);
                  pharma_type = 2;
                }else if( Integer.valueOf(pharma_res[0]) == 2){
                  pk = -Math.log10( Double.valueOf(pharma_res[1]));
                  pharma_value = Double.valueOf(pharma_res[1]);
                  pharma_type = 3;
		  leh = 0.0;
                }


		// Pharmacology

		ph = 0.0;
		temp = 0.0;
                id_pharmacology = bind.existPharmacology(id_combination,id_mol2,pharma_type);
                if( id_pharmacology  < 0)
                {
                   sb1 = new StringBuffer("");
                   stmt = conn.createStatement();
                   sb1.append("INSERT INTO PHARMACOLOGY(ID_COMBINATION,ID_MOLECULE,ID_TYPE,PH,TEMP,VALUE) VALUES ("+id_combination+","+id_mol2+","+pharma_type+","+ph+","+temp+","+pharma_value+")");
                   stmt.executeUpdate(sb1.toString(),Statement.RETURN_GENERATED_KEYS);
                   ResultSet rs_m = null;
                   id_pharmacology = -1;
                   rs_m = stmt.getGeneratedKeys();
                   if (rs_m.next()) {
                        id_pharmacology = rs_m.getInt(1);
                   }
                  rs_m.close();
                }


		// Finally, LEIs


                sei = bei = nsei = nbei = nbei2 = mbei = 0;

                if( Float.valueOf(psa_line) != 0.0)
                sei = pk / (Double.valueOf(psa_line).floatValue()/ 100.0);
                if( Float.valueOf(mass) != 0.0)
                bei = pk / (Double.valueOf(mass)/1000.0);
                if( Double.valueOf(polars) > 0)
                nsei = pk / Double.valueOf(polars);
                if( Double.valueOf(heavy) > 0){
                nbei = pk / Double.valueOf(heavy);
                nbei2 = pk + Math.log10( Integer.valueOf(heavy));
                mbei = pk + Math.log10( Double.valueOf(mass));
		}
		// Later addition Hopkins' LE: LEH; I am not certain that
            // at this point in the code kd, ki are defined to     		// calculate the corresponding LEH as defined by Hopkins.
/*		    pkh = -Math.log(kd);
                pkh = gascont*tkelvin*pkh;
                leh = pkh/heavy;*/

                System.out.println("SEI:"+ sei + " BEI: "+ bei + " NSEI: " + nsei + " NBEI: " + nbei + " nBEI: " + nbei2 + " mBEI:" + mbei + " LEH: " + leh);


		// LEH
                if( bind.existLEI(id_pharmacology,1) < 0)
                {
                   sb1 = new StringBuffer("");
                   stmt = conn.createStatement();
                   sb1.append("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES ("+id_pharmacology+",1,"+Math.abs(leh)+")");
                   stmt.executeUpdate(sb1.toString(),Statement.RETURN_GENERATED_KEYS);


		// BEI
		}
                if( bind.existLEI(id_pharmacology,2) < 0)
                {
                   sb1 = new StringBuffer("");
                   stmt = conn.createStatement();
                   sb1.append("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES ("+id_pharmacology+",2,"+Math.abs(bei)+")");
                   stmt.executeUpdate(sb1.toString(),Statement.RETURN_GENERATED_KEYS);
                }

		// SEI
		
                if( bind.existLEI(id_pharmacology,3) < 0)
                {
                   sb1 = new StringBuffer("");
                   stmt = conn.createStatement();
                   sb1.append("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES ("+id_pharmacology+",3,"+Math.abs(sei)+")");
                   stmt.executeUpdate(sb1.toString(),Statement.RETURN_GENERATED_KEYS);
                }
                if( bind.existLEI(id_pharmacology,4) < 0)
                {
                   sb1 = new StringBuffer("");
                   stmt = conn.createStatement();
                   sb1.append("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES ("+id_pharmacology+",4,"+Math.abs(nsei)+")");
                   stmt.executeUpdate(sb1.toString(),Statement.RETURN_GENERATED_KEYS);
                }
                if( bind.existLEI(id_pharmacology,5) < 0)
                {
                   sb1 = new StringBuffer("");
                   stmt = conn.createStatement();
                   sb1.append("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES ("+id_pharmacology+",5,"+Math.abs(nbei)+")");
                   stmt.executeUpdate(sb1.toString(),Statement.RETURN_GENERATED_KEYS);
                }
                if( bind.existLEI(id_pharmacology,6) < 0)
                {
                   sb1 = new StringBuffer("");
                   stmt = conn.createStatement();
                   sb1.append("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES ("+id_pharmacology+",6,"+Math.abs(nbei2)+")");
                   stmt.executeUpdate(sb1.toString(),Statement.RETURN_GENERATED_KEYS);
                }
                if( bind.existLEI(id_pharmacology,7) < 0)
                {
                   sb1 = new StringBuffer("");
                   stmt = conn.createStatement();
                   sb1.append("INSERT INTO LEI(ID_PHARMACOLOGY,ID_TYPE,VALUE) VALUES ("+id_pharmacology+",7,"+Math.abs(mbei)+")");
                   stmt.executeUpdate(sb1.toString(),Statement.RETURN_GENERATED_KEYS);
                }

                ki = 0.0f;
                kd = 0.0f;
                ph = 0.0f;
                temp = 0.0f;
                pk = 0.0f;
                sei = 0.0;
                bei = 0.0;
                nsei = 0.0;
                nbei = 0.0;
                nbei2 = 0.0;



       }

       br.close();

    }catch (NullPointerException e){
      e.printStackTrace();
    }catch (NumberFormatException e){
      e.printStackTrace();
    }catch (FileNotFoundException e){
       System.out.println("Error. File not found.");
    }catch (IOException e){
      e.printStackTrace();
    }catch (SQLException e){
      System.out.println("#### This is a main error.");
      e.printStackTrace();
    }

  }
}
