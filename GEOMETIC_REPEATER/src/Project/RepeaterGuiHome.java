package Project;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.dyno.visual.swing.layouts.Constraints;
import org.dyno.visual.swing.layouts.GroupLayout;
import org.dyno.visual.swing.layouts.Leading;


//VS4E -- DO NOT REMOVE THIS LINE!
public class RepeaterGuiHome extends JFrame {
	
	private static final long serialVersionUID = 1L;
	private int pstid=1000;
	private JLabel jLabel0;
	private JButton jButton0;
	private JLabel jLabel1;
	private JList jList0;
	private JScrollPane jScrollPane0;
	private JList jList1;
	private JScrollPane jScrollPane1;
	private JLabel jLabel2;
	private JPanel jPanel0;
	private JPanel jPanel1;
	private JDesktopPane jDesktopPane0;
	private JInternalFrame jInternalFrame0;
	private static final String PREFERRED_LOOK_AND_FEEL = "javax.swing.plaf.metal.MetalLookAndFeel";
	public RepeaterGuiHome() {
		super("SIMULATION OF DATA VIA SERIAL COMMUNICATION");
		initComponents();
	}

	private void initComponents() {
		setTitle("simulation of data via serial communication");
		setContentPane(new BackGroundImage());
		setLayout(new GroupLayout());
		add(getJScrollPane1(), new Constraints(new Leading(43, 100, 10, 10), new Leading(268, 128, 10, 10)));
		add(getJPanel0(), new Constraints(new Leading(41, 447, 12, 12), new Leading(43, 200, 12, 12)));
		add(getJPanel1(), new Constraints(new Leading(197, 148, 12, 12), new Leading(253, 146, 10, 10)));
		add(getJButton0(), new Constraints(new Leading(182, 12, 12), new Leading(398, 10, 10)));
		add(getJScrollPane0(), new Constraints(new Leading(386, 100, 12, 12), new Leading(263, 135, 12, 12)));
		add(getJLabel0(), new Constraints(new Leading(41, 178, 10, 10), new Leading(16, 10, 10)));
		add(getJLabel2(), new Constraints(new Leading(44, 12, 12), new Leading(247, 13, 10, 10)));
		add(getJLabel1(), new Constraints(new Leading(386, 12, 12), new Leading(244, 12, 12)));
		setSize(529, 453);
		setResizable(false);
	}

	private JInternalFrame getJInternalFrame0() {
		if (jInternalFrame0 == null) {
			jInternalFrame0 = new JInternalFrame();
			jInternalFrame0.setVisible(true);
			jInternalFrame0.setLayout(new GroupLayout());
		}
		return jInternalFrame0;
	}

	private JDesktopPane getJDesktopPane0() {
		if (jDesktopPane0 == null) {
			jDesktopPane0 = new JDesktopPane();
			jDesktopPane0.setDoubleBuffered(false);
		}
		return jDesktopPane0;
	}

	private JPanel getJPanel1() {
		if (jPanel1 == null) {
			jPanel1 = new GoogleEarthLogo();
			jPanel1.setLayout(new GroupLayout());
		}
		return jPanel1;
	}

	private JPanel getJPanel0() {
		if (jPanel0 == null) {
			jPanel0 = new RImagePanel(); 
			jPanel0.setLayout(new GroupLayout());
		}
		return jPanel0;
	}

	private JLabel getJLabel2() {
		if (jLabel2 == null) {
			jLabel2 = new JLabel();
			jLabel2.setText("ACTIVE POLICE STATIONS");
		}
		return jLabel2;
	}

	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new JScrollPane();
			jScrollPane1.setViewportView(getJList1());
		}
		return jScrollPane1;
	}

	private JList getJList1() {
		if (jList1 == null) {
			jList1 = new JList();
			
			Thread th=new Thread(new Runnable() {
				
				@Override
				public void run() {
					DefaultListModel listModel=new DefaultListModel<>();
					while(true)
					{
						try
						{
							System.out.println("In thread");
							listModel.clear();
							for(int i:Repeater.getPoliceStnHset())
							{
								listModel.addElement(Integer.toString(i));
							}
							jList1.setModel(listModel);
							HashSet<Integer> hs=new HashSet<>();
							Repeater.setPoliceStnHset(hs);
							Thread.sleep(10000);
						}catch(Exception e)
						{e.printStackTrace();}
					}
					
				}
			});
			th.start();
		}
		return jList1;
	}

	private JScrollPane getJScrollPane0() {
		if (jScrollPane0 == null) {
			jScrollPane0 = new JScrollPane();
			jScrollPane0.setViewportView(getJList0());
		}
		return jScrollPane0;
	}

	private JList getJList0() {
		if (jList0 == null) {
			jList0 = new JList();
			Thread th=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					DefaultListModel listModel=new DefaultListModel<>();
					while(true)
					{
						try
						{
						listModel.clear();
						for(int i:Repeater.getAllWalTkiList())
						{
							listModel.addElement(Integer.toString(i));
						}
						jList0.setModel(listModel);
						Thread.sleep(10000);
						}catch(Exception e)
						{
							
						}
						
					}
				}
			});
			th.start();
		}
		return jList0;
	}

	private JLabel getJLabel1() {
		if (jLabel1 == null) {
			jLabel1 = new JLabel();
			jLabel1.setText("ALL DEVICES");
		}
		return jLabel1;
	}

	private JButton getJButton0() {
		if (jButton0 == null) {
			jButton0 = new JButton();
			jButton0.setText("VIEW DEVICES LOCATION");
			jButton0.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					new Locator();
				}
			});
		}
		return jButton0;
	}

	private JLabel getJLabel0() {
		if (jLabel0 == null) {
			jLabel0 = new JLabel();
			jLabel0.setText("REPEATER STATION ID:"+Repeater.getRptrid());
		}
		return jLabel0;
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
				RepeaterGuiHome frame = new RepeaterGuiHome();
				frame.setDefaultCloseOperation(RepeaterGuiHome.EXIT_ON_CLOSE);
				frame.getContentPane().setPreferredSize(frame.getSize());
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				
			}
		});
	new Repeater();
	}
}

class RImagePanel extends JPanel			
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	public RImagePanel() 
	{
		try {
			image = ImageIO.read(new File("IMAGES/rstation.png"));
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

