package Project;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class DeviceReader implements Runnable, SerialPortEventListener {

	static CommPortIdentifier portId;
	static Enumeration portList;
	InputStream inputStream;
	SerialPort serialPort;
	Thread readThread;
	boolean portFound = false;
	String defaultPort = "COM1";
	boolean flag = false;
	Socket s1;
	BufferedWriter bw = null;
	OutSource os;

	public DeviceReader() {
		connectToPort();
	}

	public void connectToPort() {
		try {
			os = new OutSource();
		} catch (Exception e) {
			e.printStackTrace();
		} // it will connect to policeStation

		portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(defaultPort)) {
					System.out.println("Found port: " + defaultPort);
					portFound = true;
					readData();
				}
			}
		}
		if (!portFound) {
			System.out.println("port " + defaultPort + " not found.");
		}

	}

	public void readData() {
		try {
			serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
		} catch (PortInUseException e) {
		}

		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
		}

		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
		}

		serialPort.notifyOnDataAvailable(true);

		try {
			serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
		}

		readThread = new Thread(this);

		readThread.start();
	}

	public void run() {
		/*
		 * try { // Thread.sleep(20000); } catch (InterruptedException e) {}
		 */
	}

	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {

		case SerialPortEvent.BI:

		case SerialPortEvent.OE:

		case SerialPortEvent.FE:

		case SerialPortEvent.PE:

		case SerialPortEvent.CD:

		case SerialPortEvent.CTS:

		case SerialPortEvent.DSR:

		case SerialPortEvent.RI:

		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;

		case SerialPortEvent.DATA_AVAILABLE:

			byte[] readBuffer = new byte[53];
			try {

				if (inputStream.available() > 52) {
					inputStream.read(readBuffer);
					System.out.println("data" + new String(readBuffer));
					os.sendData(new String(readBuffer));
				}

			} catch (IOException e) {
			}

			break;
		}
	}

	public void sendData(String strLine) {
		int os_id = 1000;
		try {
			System.out.println(os_id + "," + strLine);
			if (strLine != null) {
				bw.write(os_id + "," + strLine);
				bw.newLine();
				bw.flush();
			}
		} catch (Exception m) {
			m.printStackTrace();
		}
	}

	public static void main(String[] args) {

		new DeviceReader();

	}
}