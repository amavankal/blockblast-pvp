/**
 *	InformationComponent.java
 *
 */


package blockblast.gui.gamepanel.control;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import blockblast.gui.GamePanel;

public class InfoComponent extends JPanel
{
	// serial version uid
	private static final long serialVersionUID = 3907171909247848349L;
	
	// constants
	public static final double ICON_FRAME_PROP = .55;
	
	// instance variables
	private BufferedImage infoIcon;
	private Rectangle2D.Double bounds;
	
	private InfoComponentListener listener;
	
	public InfoComponent()
	{
		// FIXME: fill homeIcon; OS-independent file path?
		// FIXME: try nio.file ? to get over OS-dependency
		try
		{
			InputStream infoIconStream = getClass()
				.getResourceAsStream("/images/infoIcon.png");
			infoIcon = ImageIO.read(infoIconStream);
		}
		catch (IOException io)
		{
			System.out.println(io.getMessage() + " Could not read info icon.");
		}
		
		// set bounds
		bounds = null;
		
		// set bg to transparent, and add border
		setOpaque(false);
		setBorder(new LineBorder(GamePanel.BORDER, 4)); // TODO magic number
		
		// add listener
		listener = new InfoComponentListener();
		addMouseListener(listener);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		// 1: draw background
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(GamePanel.BACKGROUND);
		g2.fill(new Rectangle2D.Double(0, 0, getBounds().width, getBounds().height));
		
		// 2 : draw image, if infoIcon exists
		if (infoIcon != null)
		{
			int width = (int) getPreferredSize().getWidth();
			int height = (int) getPreferredSize().getHeight();
			
			int size = (int) (width > height ? height * ICON_FRAME_PROP : width * ICON_FRAME_PROP);
			
			int startX = (width - size) / 2;
			int endX = width - startX;
			
			int startY = (height - size) / 2;
			int endY = height - startY;
			
			// set bounds, then draw
			bounds = new Rectangle2D.Double(startX, startY, endX - startX, endY - startY);
			
			g2.drawImage((Image) infoIcon, startX, startY, endX, endY,
					0, 0, infoIcon.getWidth(), infoIcon.getHeight(), null);
		}
	}

	/**
	 *	HomeComponentListener responds to clicks on this panel
	 */
	private class InfoComponentListener extends MouseAdapter
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
				// TODO display info
				System.out.println("Info button pressed");
			}
			
			inBoundsPress = false;
		}
	}
}