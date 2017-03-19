package Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import java.sql.Statement;

import java.util.HashSet;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

public class Repeater {
	//initialization of data members

	//connection related data member
	Connection postGresConn = null;	
	String ip_to_connect;
	int port_on_run;
	int port_to_connect;
	Socket s1 = null,s2=null;
	private ServerSocket ss;
	
	//Operational data member
	BufferedWriter bw;
	BufferedReader br;
	String text=null;
	ResultSet rs=null;
	
	//device data member
	private static int	rptrid=3000;
	double dbllongitude ;
	double dblaltitude ;
	double dblvelocity ;
	double dbllatitude;
	int intwt_id;
	
	//data for record of device
	private static HashSet<Integer> PoliceStnHset=new HashSet<>();
	private static HashSet<Integer> allWalTkiList=new HashSet<>();
	private static HashSet<Integer> getWalTkiList=new HashSet<>();
	
	
	//setters and getters
	public static int getRptrid() {
		return rptrid;
	}
	
	
	public static HashSet<Integer> getPoliceStnHset() {
		return PoliceStnHset;
	}
	
	public static void setPoliceStnHset(HashSet<Integer> policeStnHset) {
		PoliceStnHset = policeStnHset;
	}

	public static HashSet<Integer> getGetWalTkiList() {
		return getWalTkiList;
	}
	public static HashSet<Integer> getAllWalTkiList() {
		return allWalTkiList;
	}
	public static void setAllWalTkiList(HashSet<Integer> allWalTkiList) {
		Repeater.allWalTkiList = allWalTkiList;
	}
	
	//constructor definition
	Repeater() 
	{
		try 
		{
			port_on_run=Integer.parseInt(JOptionPane.showInputDialog("ENTER PORT NO. ON RUN"));
			ip_to_connect=JOptionPane.showInputDialog("ENTER IP CONNECT");
			port_to_connect=Integer.parseInt(JOptionPane.showInputDialog("ENTER PORT NO. TO CONNECT"));
		}
		catch (Exception e) 
		{
			JOptionPane.showMessageDialog(null, "RUNNING ON DEFAULT");
			port_on_run=8000;
			ip_to_connect="127.0.0.3";
			port_to_connect=9000;
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
		s2=new Socket(ip_to_connect, port_to_connect);		//it will connect to CentralRepeater station
		ss = new ServerSocket(port_on_run);			//Repeater station runs on this server socket
		JOptionPane.showMessageDialog(null, "Repeater started");
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

				bw=new BufferedWriter(new OutputStreamWriter(s2.getOutputStream()));
				new recData_Thread(br);	
				//buffer writer to send data from server
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
						System.out.println("not null" + text);
						//loading PostGres drivers
						Class.forName("org.postgresql.Driver");		

						//using driver manager for connecting particular database
						postGresConn = DriverManager.getConnection(
								"jdbc:postgresql://localhost:5432/RPTR_Database",
								"postgres", "password");		
						//creating statement for executing queries

						Statement receivedStr = postGresConn.createStatement();	

						//tokeninzing received string
						StringTokenizer sz = new StringTokenizer(text, ",");

						int intps_id = Integer.parseInt(sz.nextToken());
						int intos_id = Integer.parseInt(sz.nextToken());
						intwt_id = Integer.parseInt(sz.nextToken());
						PoliceStnHset.add(intps_id);
						

						Statement getWlkTkiId=postGresConn.createStatement();
						rs = getWlkTkiId.executeQuery("select distinct intwt_id from tblrptrdata");
						while(rs.next())
						{
							allWalTkiList.add(rs.getInt("intwt_id"));
						}
						rs.close();

						Statement WlkTkiId=postGresConn.createStatement();
						rs = WlkTkiId.executeQuery("select distinct intwt_id from tblrptrdata");
						while(rs.next())
						{
							getWalTkiList.add(rs.getInt("intwt_id"));
						}

						dbllongitude =Double.parseDouble(sz.nextToken());
						dbllatitude = Double.parseDouble(sz.nextToken());
						dblaltitude = Double.parseDouble(sz.nextToken());
						dblvelocity = Double.parseDouble(sz.nextToken());

						//inserting tokens in database 
						receivedStr.execute("insert into tblRPTRDATA values("
								+ rptrid + ","
								+ intps_id + ","
								+ intos_id + ","
								+ intwt_id + ","
								+ dbllongitude + ","
								+ dbllatitude + ","
								+ dblaltitude + ","
								+ dblvelocity + ")");	

						System.out.println("Client: " + text + "\n");

					}
					sendData();
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

	public void sendData() {
		try 
		{
			if(text!=null)
			{
				bw.write(rptrid+","+text);
				bw.newLine();
				bw.flush();
				text=null;
			}
		} 
		catch (Exception e) 
		{

			System.out.println("Connection Terminated");
		}
	}
}
