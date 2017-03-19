package Project;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

class BackGroundImage extends JPanel			
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	public BackGroundImage() 
	{
		try {
			image = ImageIO.read(new File("images/img.jpg"));
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