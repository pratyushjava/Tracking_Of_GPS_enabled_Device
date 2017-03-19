package Project;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.dyno.visual.swing.layouts.Bilateral;
import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;


//VS4E -- DO NOT REMOVE THIS LINE!
public class GPS_DEVICE_GUI_HOME extends JFrame {

	private static final long serialVersionUID = 1L;
	private static  JTextArea jTextArea0;
	private JScrollPane jScrollPane0;
	private JButton jButton0;
	private JPanel jPanel0;
	private JPanel jPanel1;
	private JLabel jLabel0;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	public static JTextArea getjTextArea0()
	{
		return jTextArea0;
	}
	public GPS_DEVICE_GUI_HOME() {
		super("SIMULATION OF DATA VIA SERIAL COMMUNICATION");
		initComponents();
	}

	private void initComponents() {
		setTitle("SIMULATION OF DATA VIA SERIAL COMMUNICATION");
		setContentPane(new BackGroundImage());
		setLayout(new GroupLayout());
		add(getJScrollPane0(), new Constraints(new Leading(20, 279, 10, 10), new Leading(18, 175, 10, 10)));
		add(getJLabel0(), new Constraints(new Leading(75, 10, 10), new Leading(194, 10, 10)));
		add(getJButton0(), new Constraints(new Leading(118, 10, 10), new Leading(213, 10, 10)));
		add(getJPanel1(), new Constraints(new Leading(309, 199, 10, 10), new Bilateral(18, 12, 0)));
		setSize(539, 327);
	}
	private JLabel getJLabel0() {
		if (jLabel0 == null) {
			jLabel0 = new JLabel();
			jLabel0.setText("GPS ENABLED DEVICE DATA");
		}
		return jLabel0;
	}
	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new ImagePanel();
			jPanel1.setLayout(new GroupLayout());
		}
		return jPanel1;
	}
	private JPanel getJPanel0() {
		if (jPanel0 == null) {
			jPanel0 = new ImagePanel();
			jPanel0.setLayout(new GroupLayout());
		}
		return jPanel0;
	}
	private JButton getJButton0() {
		if (jButton0 == null) {
			jButton0 = new JButton();
			jButton0.setText("Start");
			jButton0.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					new WalkieTalkie();
					
				}
			});
		}
		return jButton0;
	}

	private JScrollPane getJScrollPane0() {
		if (jScrollPane0 == null) {
			jScrollPane0 = new JScrollPane();
			jScrollPane0.setViewportView(getJTextArea0());
		}
		return jScrollPane0;
	}

	private static JTextArea getJTextArea0() {
		if (jTextArea0 == null) {
			jTextArea0 = new JTextArea();
			jTextArea0.setEditable(false);
			Thread th=new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(true)
					{
						if(WalkieTalkie.getData()!=null)
						{
						jTextArea0.append(WalkieTalkie.getData()+"\n");
						WalkieTalkie.setData(null);
						}
						
					}
					
				}
			});
			th.start();
		}
		return jTextArea0;
	}

	private static void installLnF() {
		try {
			String lnfClassname = PREFERRED_LOOK_AND_FEEL;
			if (lnfClassname == null)
				lnfClassname = UIManager.getCrossPlatformLookAndFeelClassName();
			UIManager.setLookAndFeel(lnfClassname);
		} catch (Exception e) {
			System.err.println("Cannot install " + PREFERRED_LOOK_AND_FEEL
					+ " on this platform:" + e.getMessage());
		}
	}

	/**
	 * Main entry of the class.
	 * Note: This class is only created so that you can easily preview the result at runtime.
	 * It is not expected to be managed by the designer.
	 * You can modify it as you like.
	 */
	public static void main(String[] args) {
		installLnF();
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				GPS_DEVICE_GUI_HOME frame = new GPS_DEVICE_GUI_HOME();
				frame.setDefaultCloseOperation(GPS_DEVICE_GUI_HOME.EXIT_ON_CLOSE);
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
		
		
	}
}
class ImagePanel extends JPanel			
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	public ImagePanel() 
	{
		try {
			image = ImageIO.read(new File("IMAGES/WirelessModem.jpg"));
		}
		catch (IOException ex) 
		{
			// handle exception...       
		} 
	} 
	@Override
	public void paintComponent(Graphics g) 
	{
		g.drawImage(image, 0, 0, null);      
	}
} 

