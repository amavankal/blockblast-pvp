/**
 *  IconComponent.java -- used for image and home icon components
 */
package blockblast.gui.gamepanel.control;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import blockblast.gui.GamePanel;

public class ResponsiveImageComponent extends JPanel {
    // serial version uid
	private static final long serialVersionUID = 3107171929247848349L;
	
	// size constants
	public static final double ICON_FRAME_PROP = .65;
    public static final int BORDER_THICKNESS = 4;

    // instance variables
    private BufferedImage image;
    private Runnable onClick;

    private Rectangle2D.Double bounds;
    private ImageListener listener;

    public ResponsiveImageComponent(BufferedImage image, Runnable onClick) {
        // set image & onClick method
        this.image = image;
        this.onClick = onClick;

        // set bounds
		bounds = null;
		
		// set bg to transparent, and add border
		setOpaque(false);
		setBorder(new LineBorder(GamePanel.BORDER, BORDER_THICKNESS));

        // create & add listener
        listener = new ImageListener();
        addMouseListener(listener);
    }

    @Override
	public void paintComponent(Graphics g)
	{
		// 1: fill background
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(GamePanel.BACKGROUND);
		g2.fill(new Rectangle2D.Double(0, 0, getBounds().width, getBounds().height));
		
		// 2 : draw image, if it exists
		if (image != null)
		{
			int width = getWidth();
			int height = getHeight();
			
			int size = (int) (width > height ? height * ICON_FRAME_PROP : width * ICON_FRAME_PROP);
			
			int startX = (width - size) / 2;
			int endX = width - startX;
			
			int startY = (height - size) / 2;
			int endY = height - startY;
			
			// set bounds, then draw
			bounds = new Rectangle2D.Double(startX, startY, endX - startX, endY - startY);
			
			g2.drawImage((Image) image, startX, startY, endX, endY,
					0, 0, image.getWidth(), image.getHeight(), null);
		}
	}

    /**
	 *	ImageListener responds to clicks on the image
	 */
	private class ImageListener extends MouseAdapter
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
                onClick.run();
			
			inBoundsPress = false;
		}
	}
}
