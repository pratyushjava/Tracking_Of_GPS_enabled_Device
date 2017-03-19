package Project;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;


public class OutSource {
	Thread th[];
	String strLine;
	Connection postGresConn = null;
	Socket s1;
	BufferedWriter bw = null;
	//BufferedReader br=null;
	//Thread th=null;
	OutSource() {
		try {
					connectToServer();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void connectToServer() throws Exception {
		s1 = new Socket("localhost", 7000);				//it will connect to policeStation
		bw = new BufferedWriter(new OutputStreamWriter(s1.getOutputStream()));

	}

	public void sendData(String strLine) {	
		int os_id=1000;
		try {
			System.out.println(os_id+","+strLine);
			bw.write(os_id+","+strLine);
			bw.newLine();
			bw.flush();

		} catch (Exception m) {
			m.printStackTrace();
		}
	}

/*	public static void main(String[] args) {
		new OutSource();
	}*/
}
