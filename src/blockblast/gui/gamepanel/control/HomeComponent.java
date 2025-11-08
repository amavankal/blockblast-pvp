/**
 *	HomeComponent.java contains the home button and takes the user to the home
 *		panel when the home button is clicked (home not implemented yet).
 *
 */


package blockblast.gui.gamepanel.control;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import blockblast.gui.GamePanel;

public class HomeComponent extends JPanel
{
	// serial version uid
	private static final long serialVersionUID = 70002292630264072L;
	
	// size constants
	public static final double ICON_FRAME_PROP = .55;
	
	// instance vars 
	private BufferedImage homeIcon;
	private Rectangle2D.Double bounds;
	
	private HomeComponentListener listener;
	
	public HomeComponent()
	{
		// FIXME: fill homeIcon. make OS-independent filepath
		try
		{
			InputStream homeIconStream = getClass()
				.getResourceAsStream("/blockblast/images/homeIcon2.png");
			homeIcon = ImageIO.read(homeIconStream);
		}
		catch (IOException io)
		{
			System.out.println(io.getMessage());
		}
		
		// set bounds
		bounds = null;
		
		// set background & border
		setOpaque(false);
		setBorder(new LineBorder(GamePanel.BORDER, 4)); // TODO magic number
		
		// add listener
		listener = new HomeComponentListener();
		addMouseListener(listener);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		// 1: draw background
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(GamePanel.BACKGROUND);
		g2.fill(new Rectangle2D.Double(0, 0, getBounds().width, getBounds().height));
		
		// 2 : draw image
		int width = (int) getPreferredSize().getWidth();
		int height = (int) getPreferredSize().getHeight();
		
		int size = (int) (width > height ? height * ICON_FRAME_PROP : width * ICON_FRAME_PROP);
		
		int startX = (width - size) / 2;
		int endX = width - startX;
		
		int startY = (height - size) / 2;
		int endY = height - startY;
		
		// set bounds, then draw
		bounds = new Rectangle2D.Double(startX, startY, endX - startX, endY - startY);
		
		g2.drawImage((Image) homeIcon, startX, startY, endX, endY,
				0, 0, homeIcon.getWidth(), homeIcon.getHeight(), null);
	}
	
	/**
	 *	HomeComponentListener responds to clicks on this panel
	 */
	private class HomeComponentListener extends MouseAdapter
	{
		private boolean inBoundsPress;
		
		@Override
		public void mousePressed(MouseEvent e)
		{
			if (bounds.contains(e.getPoint()))
				inBoundsPress = true;
		}
		
		@Override
		public void mouseReleased(MouseEvent e)
		{
			if (bounds.contains(e.getPoint()) && inBoundsPress)
			{
				// TODO: go home
				System.out.println("Home button pressed");
			}
			
			inBoundsPress = false;
		}
	}
	
}