package Project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;

public class KmlCreator {
	
	//operational data members
	PrintWriter pw1;
	PrintWriter pw2;
	static File file1;
	File file2;
	
	//device data members
	int id;
	double longitude;
	double latitude;
	double altitude;
	double velocity;
	
	//method to generate kml file by id
	KmlCreator(int intwt_id,double dbllongitude,double dbllatitude,double dblaltitude,double dblvelocity)
	{
		
		this.id=intwt_id;
		this.longitude=dbllongitude;
		this.latitude=dbllatitude;
		this.altitude=dblaltitude;
		this.velocity=dblvelocity;
		try
		{
			file1=new File("c:/DEVICES_LOCATION");
			if(!file1.isDirectory())
			{
				file1.mkdir();
			}
			file1=new File("c:/DEVICES_LOCATION/"+id+".kml");
			file2=new File("c:/DEVICES_LOCATION/alldevice.kml");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}

		if(!file1.exists())
		{
			createKml();
		}
		else
		{
			updateKml();
		}
		if(!file2.exists())
		{
			createUniversalKml(intwt_id);
		}
		else
		{
			updateUniversalKml(intwt_id);
		}
	}

	//method to generate Universal kml file
	private void createUniversalKml(int id)
	{
		try {
			pw2=new PrintWriter(new FileWriter("c:/DEVICES_LOCATION/alldevice.kml"),true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String str="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<kml xmlns=\"http://earth.google.com/kml/2.1\">"+
				"<Document>"+
				"<NetworkLink>"+
				"<Link>"+
				"<href>"+id+".kml</href>"+
				"<refreshMode>onInterval</refreshMode>"+
				"<refreshInterval>1</refreshInterval>"+
				"</Link>"+
				"</NetworkLink>"+
				"</Document>"+
				"</kml>";
		pw2.println(str);
		pw2.flush();
		pw2.close();
	}

	private void updateUniversalKml(int id) {
		RandomAccessFile randfile;
			if(!PoliceStation.getAllWalTkiList().contains(id))
			{
				try {			
					randfile = new RandomAccessFile(file2,"rwd");
					randfile.seek(randfile.length()-19);
					byte[] b=new byte[19];
					randfile.readFully(b);

					randfile.seek(randfile.length()-19);
					String str="<NetworkLink>" +
							"<Link>"+
							"<href>"+id+".kml</href>"+
							"<refreshMode>onInterval</refreshMode>"+
							"<refreshInterval>1</refreshInterval>"+
							"</Link>"+
							"</NetworkLink>";
					randfile.writeBytes(str);
					randfile.write(b);
					randfile.close();
					System.out.println("1 file updated");
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		
		
		
	}
	
	//method to create new kml file
	void createKml()
	{
		try {
			pw1=new PrintWriter(new FileWriter("c:/DEVICES_LOCATION/"+id+".kml"),true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		String str="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
				"<kml xmlns=\"http://www.opengis.net/kml/2.2\" xmlns:gx=\"http://www.google.com/kml/ext/2.2\" xmlns:kml=\"http://www.opengis.net/kml/2.2\" xmlns:atom=\"http://www.w3.org/2005/Atom\">"+
				"<Document>"+
				"<name>"+id+".kml</name>"+
				"<Style id=\"sn_ylw-pushpin\">"+
				"<IconStyle>"+
				"<scale>1.1</scale>"+
				"<Icon>"+
				"<href>http://maps.google.com/mapfiles/kml/pushpin/ylw-pushpin.png</href>"+
				"</Icon>"+
				"<hotSpot x=\"20\" y=\"2\" xunits=\"pixels\" yunits=\"pixels\"/>"+
				"</IconStyle>"+
				"<LineStyle>"+
				"<color>ff13e1f3</color>"+
				"<width>5</width>"+
				"</LineStyle>"+
				"</Style>"+
				"<StyleMap id=\"msn_ylw-pushpin\">"+
				"<Pair>"+
				"<key>normal</key>"+
				"<styleUrl>#sn_ylw-pushpin</styleUrl>"+
				"</Pair>"+
				"</StyleMap>"+
				"<Placemark>"+
				"<name>"+id+"</name>"+
				"<description>Starting Point and Velocity:"+velocity+"</description>"+
				"<Point>"+													
				"<coordinates>"+longitude+","+latitude+","+altitude+"</coordinates>"+
				"</Point>"+
				"</Placemark>"+
				"<Placemark>"+
				"<styleUrl>#msn_ylw-pushpin</styleUrl>"+
				"<LineString>"+
				"<tessellate>1</tessellate>"+
				"<coordinates>"+longitude+","+latitude+","+altitude+"</coordinates>"+
				"</LineString>"+
				"</Placemark>"+			
				"</Document>"+
				"</kml>";
		pw1.println(str);
		pw1.flush();
		pw1.close();
		System.out.println("1 file created");

	}
	
	//method to update old kml file 
	void updateKml()
	{
		RandomAccessFile randfile;
		try {			
			randfile = new RandomAccessFile(file1,"rwd");
			randfile.seek(randfile.length()-58);
			byte[] b=new byte[58];
			randfile.readFully(b);
			randfile.seek(randfile.length()-58);
			randfile.writeBytes("		"+longitude+","+latitude+","+altitude);
			randfile.write(b);
			randfile.close();
			System.out.println("1 file updated");
		} catch (Exception e) {

			e.printStackTrace();
		}
	}
}