package Project;

import gnu.io.*;
import java.io.*;
import java.util.*;

import javax.swing.JOptionPane;

public class WalkieTalkie {
	
	private static String Data;
	static Enumeration portList;
	static CommPortIdentifier portId;
	static SerialPort serialPort;
	static OutputStream outputStream;
	
	static boolean outputBufferEmptyFlag = false;
	static int counter;
	boolean portFound = false;
	String  defaultPort = "COM1";
	Thread[] th;
	
	public static String getData() {
		return Data;
	}
	
	public WalkieTalkie() {
		readFile();
	}
	
	public void readFile()
	{
		File dir=new File("d:/DEVICES_LOCATION");
		if(dir.isDirectory())
		{
			File[] activeDevice=dir.listFiles();
			int i=0;
			while (i<activeDevice.length) {
				GPS_DEVICE_GUI_HOME.getjTextArea0().append(activeDevice[i].toString()+"\n");
				i++;
			}
			th=new Thread[i];

			for(int j=0;j<activeDevice.length;j++)
			{
				th[j]=new Thread(new readThread(activeDevice[j]));
				th[j].start();
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, "No Active Devices");
		}
	}
	class readThread implements Runnable
	{
		String strLine;
		File activeDevice;
		FileInputStream fstream;
		DataInputStream in;
		BufferedReader br;
		public readThread() {
		}

		public readThread(File activeDevice) {	
			this.activeDevice=activeDevice;
		}
		@Override
		public void run() {
			try{
				// Open the file that is the first 
				// command line parameter
				// Get the object of DataInputStream
				fstream= new FileInputStream(activeDevice);
				in = new DataInputStream(fstream);
				br = new BufferedReader(new InputStreamReader(in));
				//Read File Line By Line
				while ((strLine = br.readLine()) != null)   {
					// Print the content on the console
					Thread.sleep(5000);
				
					sendData(strLine);
				}
				//Close the input stream
				in.close();
			}catch (Exception e){//Catch exception if any
				GPS_DEVICE_GUI_HOME.getjTextArea0().append("Error:DATA NOT FOUND");
			}
		}
	}
	public synchronized void sendData(String strLine)
	{
		this.Data=strLine;
		portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();

			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

				if (portId.getName().equals(defaultPort)) {
					System.out.println("Found port " + defaultPort + " counter " + counter);
					counter++;
					portFound = true;

					try {
						serialPort = (SerialPort) portId.open("WalkieTalkie", 2000);
					} catch (PortInUseException e) {
						System.out.println("Port in use.");
						continue;
					} 
					try {
						outputStream = serialPort.getOutputStream();
					} catch (IOException e) {}

					try {
						serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
					} catch (UnsupportedCommOperationException e) {}


					try {
						serialPort.notifyOnOutputEmpty(true);
					} catch (Exception e) {
						JOptionPane.showMessageDialog(null, "Error setting event notification");
					
					}
		
					try {
						outputStream.write(strLine.getBytes());
					
						serialPort.close();
					} catch (Exception e) {}
				} 
			} 
		} 
		if (!portFound) {
			JOptionPane.showMessageDialog(null, "port " + defaultPort + " not found.");
		} 
	}

	public static void setData(String data) {
		Data = data;
	} 
	
}

