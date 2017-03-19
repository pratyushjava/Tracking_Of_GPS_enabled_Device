package Project;

import java.sql.Connection;
/*import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;*/

import javax.swing.JOptionPane;

public class Locator {

	Connection postGresConn = null;		
	Runtime r=Runtime.getRuntime(); 
	Process p=null;
	String location="";

	public Locator()
	{
		locate();
	}

	public void locate()
	{

		try 
		{ 
			String s="C:/Program Files/Google/Google Earth/client/googleearth.exe"; 
			

			//using driver manager for connecting particular database
	/*		
 			Class.forName("org.postgresql.Driver");			
			postGresConn = DriverManager.getConnection(
					"jdbc:postgresql://localhost:5432/Central_RPTRDatabase",
					"postgres", "password");*/

			/*//creating statement for executing queries
			Statement smt = postGresConn.createStatement();

			ResultSet rs=smt.executeQuery("select distinct intwt_id from tblcrptrdata");
*/

			location+="c:/DEVICES_LOCATION/alldevice.kml ";


			System.out.println(location);

			p=r.exec(s+" "+location);


		} 
		catch(Exception e){ 
			System.out.println("error==="+e.getMessage()); 
			JOptionPane.showMessageDialog(null, "CAN'T FIND GOOGLE EARTH ON MECHINE PLEASE INSTALL GOOGLE EARTH");
			e.printStackTrace(); 
		} 
	}
}

