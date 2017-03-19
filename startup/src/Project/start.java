package Project;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class start {
	public static void main(String[] args) {
		Connection postGresConn=null;
		try 
		{
			//CREATE DATABASE Central_RPTRDatabase;
			
			postGresConn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/Central_RPTRDatabase",
					"postgres", "password");
			Statement smt = postGresConn.createStatement();

			ResultSet rs=smt.executeQuery("CREATE TABLE tblcrptrdata(intcrptr_id integer NOT NULL,  intrptr_id integer,  intps_id integer,  intos_id integer,  intwt_id integer,  dbllongitude double precision,  dbllatitude double precision,  dblaltitude double precision,  dblvelocity double precision)");
			
			//CREATE DATABASE RPTRDatabase;
			
			postGresConn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/RPTRDatabase",
					"postgres", "password");
			
			rs=smt.executeQuery("CREATE TABLE tblrptrdata(intrptr_id integer NOT NULL,intps_id integer,			  intos_id integer,intwt_id integer,dbllongitude double precision,dbllatitude double precision,dblaltitude double precision,dblvelocity double precision)");
			
			postGresConn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/RPTRDatabase",
					"postgres", "password");
			
			rs=smt.executeQuery("CREATE TABLE tblpsdata(intPS_ID integer NOT NULL,intOS_ID integer,  intwt_id integer,  dblLONGITUDE double precision,  dblLATITUDE double precision,  dblALTITUDE double precision,  dblVELOCITY double precision)");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}			

	}
}
