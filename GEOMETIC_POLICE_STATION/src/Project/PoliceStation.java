package Project;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JOptionPane;

public class PoliceStation{
	
	//initialization of data members
	
	//connection related data member
	Connection postGresConn = null;	
	private ServerSocket ss;
	String ip_to_connect;
	int port_on_run;
	int port_to_connect;
	Socket s = null;
	Socket s1=null;

	
	
	//Operational data member
	StringTokenizer sz=null;
	BufferedWriter bw;
	BufferedReader br;
	String text=null;
	private static int psid=2100;
	ResultSet rs=null;
	
	//device data member
	double dbllongitude ;
	double dblaltitude ;
	double dblvelocity ;
	double dbllatitude;
	int wt_id;
	
	//data for record of device
	private static HashSet<Integer> waltkiHSet=new HashSet<>(); 
	private static HashSet<Integer> allWalTkiList=new HashSet<>();
	
	//setters and getters
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public static int getPsid() {
		return psid;
	}

	public static void setWaltkiHSet(HashSet<Integer> waltkiHSet) {
		PoliceStation.waltkiHSet = waltkiHSet;
	}
	public static HashSet<Integer> getAllWalTkiList() {
		return allWalTkiList;
	}

	public static HashSet<Integer> getWaltkiList() {

		return allWalTkiList;
	}

	//constructor definition
	PoliceStation() 
	{
		try 
		{
			port_on_run=Integer.parseInt(JOptionPane.showInputDialog("ENTER PORT NO. ON RUN"));
			ip_to_connect=JOptionPane.showInputDialog("ENTER IP CONNECT");
			port_to_connect=Integer.parseInt(JOptionPane.showInputDialog("ENTER PORT NO. TO CONNECT"));
		}
		catch (Exception e) 
		{
			port_on_run=7000;
			ip_to_connect="127.0.0.3";
			port_to_connect=8000;
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
		s1=new Socket(ip_to_connect,port_to_connect);		//it will connect to Repeater station
		ss = new ServerSocket(port_on_run);			//police station runs on this server socket
		JOptionPane.showMessageDialog(null, "PoliceStation started");
		waitForClients();
	}

	//responding request to server
	public void waitForClients() 
	{

		while (true) 
		{
			try 
			{
				s = ss.accept();								//waiting for new client request
				br = new BufferedReader(new InputStreamReader(
						s.getInputStream()));					//buffer reader to receive data from client
				bw=new BufferedWriter(new OutputStreamWriter(s1.getOutputStream()));
				new recData_Thread(br);	
			}
			catch (IOException e) 
			{
				e.printStackTrace();		//exception if server not able to handle more request
			}
		}
	}

	class recData_Thread extends Thread			//recData thread class to handle client message/data
	{
		BufferedReader in;

		public recData_Thread(BufferedReader in) {
			System.out.println("one connection established");
			this.in = in;
			start();
		}


		public void run() 
		{

			//run method called when start method is called implicitely
			try {
				postGresConn = DriverManager.getConnection(
						"jdbc:postgresql://localhost:5432/PS_Database",
						"postgres", "password");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}		
			while (true) 
			{
				try 
				{
					System.out.println(text);
					text = in.readLine();
					System.out.println(text);
					if (text != null) 
					{
						System.out.println("not null" + text);
						Class.forName("org.postgresql.Driver");		//loading PostGres drivers
						//using driver manager for connecting particular database

						Statement stGetCount = postGresConn.createStatement();	//creating statement for executing queries
						System.out.println("In thread"+text);
						sz = new StringTokenizer(text, ",");


						int os_id=Integer.parseInt(sz.nextToken());
						wt_id=Integer.parseInt(sz.nextToken());

						
						waltkiHSet.add(wt_id);// Add walkietalkie ID to the Hash Set containing current WalkieTalkie ID

						// Add Add walkietalkie ID to the ArrayList containing all WalkieTalkie ID
						Statement getWlkTkiId=postGresConn.createStatement();
						rs = getWlkTkiId.executeQuery("select distinct intwt_id from tblpsdata");
						while(rs.next())
						{
							allWalTkiList.add(rs.getInt("intwt_id"));
						}

						dbllongitude = Double.parseDouble(sz.nextToken());
						dbllatitude = Double.parseDouble(sz.nextToken());
						dblaltitude = Double.parseDouble(sz.nextToken());
						dblvelocity = Double.parseDouble(sz.nextToken());
						stGetCount.execute("insert into tblpsdata values("+psid+","+os_id +","+wt_id+","+dbllongitude+","+dbllatitude+","+dblaltitude+","+dblvelocity+")");							//inserting string in database

						System.out.println("Client: " + text + "\n");
						sendData();

					}
				} 
				catch (Exception e) 
				{
					e.printStackTrace();
					break;
				}
				new KmlCreator(wt_id, dbllongitude, dbllatitude, dblaltitude, dblvelocity);
			}
		}
	}
	public void sendData() {

		try {
			if(text!=null)
			{
				bw.write(psid+","+text);
				bw.newLine();
				bw.flush();
				text=null;
			}
		} catch (Exception e) {
			System.out.println("Connection Terminated");

		}


	}
	public static HashSet<Integer> getWaltkiHSet() {
		return waltkiHSet;
	}

}
