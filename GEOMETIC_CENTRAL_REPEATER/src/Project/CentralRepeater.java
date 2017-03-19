package Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.Statement;

import java.util.HashSet;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class CentralRepeater {

	//initialization of data members

	//connection related data member
	int portno;
	Connection postGresConn = null;		
	Socket s1 = null;
	private ServerSocket ss;

	//device data member
	private static int crptrid=4000;
	int intwt_id;
	double dbllongitude ;
	double dblaltitude ;
	double dblvelocity ;
	double dbllatitude;

	//Operational data member
	String text;
	BufferedWriter bw;
	BufferedReader br;
	ResultSet rs=null;

	//data for record of device
	private static HashSet<Integer> repeaterHset=new HashSet<>();
	private static HashSet<Integer> allWalTkiList=new HashSet<>();


	//setter and getters
	public static int getCrptrid() {
		return crptrid;
	}
	public static HashSet<Integer> getRepeaterHset() {
		return repeaterHset;
	}
	public static void setRepeaterHset(HashSet<Integer> repeaterHset) {
		CentralRepeater.repeaterHset = repeaterHset;
	}
	public static HashSet<Integer> getAllWalTkiList() {
		return allWalTkiList;
	}

	//constructor definition
	CentralRepeater() 
	{
		try 
		{
			portno=Integer.parseInt(JOptionPane.showInputDialog("ENTER PORT NO. ON RUN"));
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "RUNNING ON DEFAULT.");
			portno=9000;
		}
		try 
		{
			startServer();
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();	
			JOptionPane.showMessageDialog(null, "ERROR IN CONNECTION");
			System.exit(0);
		}
	}

	//connecting and creating servers
	public void startServer() throws Exception 
	{

		ss = new ServerSocket(portno);
		JOptionPane.showMessageDialog(null, "CENTRAL REPEATER STARTED");
		waitForClients();
	}

	//responding request to server
	public void waitForClients() 
	{

		while (true) 
		{
			try 
			{
				//waiting for new client request
				s1 = ss.accept();
				//buffer reader to receive data from client
				br = new BufferedReader(new InputStreamReader(
						s1.getInputStream()));	

				new recData_Thread(br);	
			}
			catch (IOException e) 
			{
				//exception if server not able to handle more request
				e.printStackTrace();		
			}
		}

	}
	//recData thread class to handle client message/data
	class recData_Thread extends Thread			
	{
		BufferedReader in;

		public recData_Thread(BufferedReader in) {
			System.out.println("one connection established");
			this.in = in;
			start();

		}

		//run method called when start method is called implicitly
		public void run() {									
			while (true) 
			{
				try 
				{
					System.out.println(text);
					text = in.readLine();
					System.out.println(text);
					if (text != null) 
					{
						//loading PostGres drivers
						Class.forName("org.postgresql.Driver");
						//using driver manager for connecting particular database
						postGresConn = DriverManager.getConnection(
								"jdbc:postgresql://localhost:5432/Central_RPTRDatabase",
								"postgres", "password");
						//creating statement for executing queries
						Statement receivedStr = postGresConn.createStatement();	

						//tokenizing received string
						StringTokenizer sz = new StringTokenizer(text, ",");

						int intrptr_id = Integer.parseInt(sz.nextToken());
						int intps_id = Integer.parseInt(sz.nextToken());
						int intos_id = Integer.parseInt(sz.nextToken());

						repeaterHset.add(intrptr_id);	//to maintain list of current repeater station

						Statement getWlkTkiId=postGresConn.createStatement();
						rs = getWlkTkiId.executeQuery("select distinct intwt_id from tblcrptrdata");
						while(rs.next())
						{
							allWalTkiList.add(rs.getInt("intwt_id"));
						}

						intwt_id = Integer.parseInt(sz.nextToken());
						dbllongitude = Double.parseDouble(sz.nextToken());
						dbllatitude = Double.parseDouble(sz.nextToken());
						dblaltitude = Double.parseDouble(sz.nextToken());
						dblvelocity = Double.parseDouble(sz.nextToken());

						//inserting tokens in database 
						receivedStr.execute("insert into tblCRPTRDATA values("
								+ crptrid + ","
								+ intrptr_id + ","
								+ intps_id + ","
								+ intos_id + ","
								+ intwt_id + ","
								+ dbllongitude + ","
								+ dbllatitude + ","
								+ dblaltitude + ","
								+ dblvelocity + ")");	

						System.out.println("Client: " + text + "\n");
					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					System.out.println("one connection terminated");
					break;
				}
				new KmlCreator(intwt_id, dbllongitude, dbllatitude, dblaltitude, dblvelocity);

			}
		}
	}
}
