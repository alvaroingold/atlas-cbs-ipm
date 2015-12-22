/**
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

import java.lang.*;
import java.sql.*;

public class BDB{

      private String database;
      private String username;
      private String password;
      private String host;
      private String port;
      private String connString;
      private Connection conn;

      public BDB(String connString)
      {
          this.connString = connString;
      }

      public BDB(){}

      public BDB(String username, String password, String database, String host, String port)
      {
	 this.username = username;
         this.password = password;
         this.database = database;
         this.host = host;
         this.port = port;
         this.connString = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.database + "?user=" + this.username + "&password=" + this.password;
      }

      public void setconnString(String connString)
      {
         this.connString = connString;
      }

      public void connect()
      {
       try {
           this.conn = DriverManager.getConnection(this.connString);
       }catch (SQLException e){
           System.out.println("Error. Cant connect to the database.");
       }
      }
      public int existOrganism(String name)
      {
	Statement stmt;
        ResultSet rs;
        try{
        int result;
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM ORGANISM WHERE NAME='"+name+"';");
        //System.out.println("SELECT * FROM ORGANISM WHERE NAME='"+name+"';");
        if( rs.next() )
        {
        result = rs.getInt(1);
        }else{ result = -1;}
        rs.close();
        return result;
        }catch (SQLException e){ e.printStackTrace();}
         
        return -1;
      }

      public int existTarget(String name)
      {
	Statement stmt;
        ResultSet rs;
        try{
        int result;
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM TARGETS WHERE NAME='"+name+"';");
        //System.out.println("SELECT * FROM ORGANISM WHERE NAME='"+name+"';");
        if( rs.next() )
        {
        result = rs.getInt(1);
	System.out.println(result);
        }else{ result = -1;}
        rs.close();
        return result;
        }catch (SQLException e){ e.printStackTrace();}
         
        return -1;
      }


      public int existMolecule(String name)
      {
        Statement stmt;
        ResultSet rs;
        try{
        int result;
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM MOLECULE WHERE NAME='"+name+"';");
        rs.absolute(1);
        result = rs.getInt(1);
        rs.close();
        return result;
        }catch (SQLException e){ }

        return -1;
      }

      public int existMolecule(int origin, String name)
      {
        Statement stmt;
        ResultSet rs;
        try{
        int result;
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM MOLECULE WHERE NAME='"+name+"' AND ID_ORIGINDB="+origin+";");
        rs.absolute(1);
        result = rs.getInt(1);
        rs.close();
        return result;
        }catch (SQLException e){ }

        return -1;
      }


      public int existCombination(int target, int organism)
      {
	Statement stmt;
        ResultSet rs;
        try{
        int result;
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT * FROM TARGORG WHERE ID_TARGET="+target+" AND ID_ORGANISM="+organism);
        rs.absolute(1);
        result = rs.getInt(1);
        rs.close();
        return result;
        }catch (SQLException e){ }

        return -1;
      }
 
      public int existPharmacology(int id_combination,int id_mol,int pharma_type)
      {
        Statement stmt;
        ResultSet rs;
        try{
        int result;
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT ID_PHARMACOLOGY FROM PHARMACOLOGY WHERE ID_COMBINATION="+id_combination+" AND ID_MOLECULE="+id_mol+" AND ID_TYPE="+pharma_type);
        rs.absolute(1);
        result = rs.getInt(1);
        rs.close();
        return result;
        }catch (SQLException e){ }

        return -1;
      }

      public int existLEI(int id_pharmacology,int id_type)
      {
        Statement stmt;
        ResultSet rs;
        try{
        int result;
        stmt = conn.createStatement();
        rs = stmt.executeQuery("SELECT ID_PHARMACOLOGY FROM LEI WHERE ID_PHARMACOLOGY="+id_pharmacology+" AND ID_TYPE="+id_type);
        rs.absolute(1);
        result = rs.getInt(1);
        rs.close();
        return result;
        }catch (SQLException e){ }

        return -1;
      }

}
