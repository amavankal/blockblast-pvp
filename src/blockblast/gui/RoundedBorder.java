/**
 *	RoundedBorder.java is a rounded border paints a rounded border used on
 *		the WelcomePanel.
 *	
 *	TODO: move to more appropriate place in package (not w/ main pages)
 *
 * 	@author Asha Mavankal
 */

package blockblast.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;

import javax.swing.JButton;
import javax.swing.border.AbstractBorder;

class RoundedBorder extends AbstractBorder
{
	// serial version uid
	private static final long serialVersionUID = 6297978035299450249L;
	
	public static final Color DEFAULT_BORDER_COLOR = Color.BLACK;
	public static final float DEFAULT_THICKNESS = 1;
	public static final float DEFAULT_ROUNDED = .6f;
	
	private Color borderColor;
	private Color buttonColor;
	private float lineThickness;
	private float roundedPercentH;
	private float roundedPercentV;
	
	public RoundedBorder()
	{
		borderColor = DEFAULT_BORDER_COLOR;
		buttonColor = null;
		lineThickness = DEFAULT_THICKNESS;
		roundedPercentH = DEFAULT_ROUNDED;
		roundedPercentV = DEFAULT_ROUNDED;
	}
	
	public RoundedBorder(Color c)
	{
		borderColor = c;
		buttonColor = null;
		lineThickness = DEFAULT_THICKNESS;
		roundedPercentH = DEFAULT_ROUNDED;
		roundedPercentV = DEFAULT_ROUNDED;
	}
	
	public RoundedBorder(Color c, Color bg)
	{
		borderColor = c;
		buttonColor = bg;
		lineThickness = DEFAULT_THICKNESS;
		roundedPercentH = DEFAULT_ROUNDED;
		roundedPercentV = DEFAULT_ROUNDED;
	}
	
	public RoundedBorder(Color c, float lt)
	{
		borderColor = c;
		buttonColor = null;
		lineThickness = lt;
		roundedPercentH = DEFAULT_ROUNDED;
		roundedPercentV = DEFAULT_ROUNDED;
	}

	public RoundedBorder(Color c, Color bg, float lt)
	{
		borderColor = c;
		buttonColor = bg;
		lineThickness = lt;
		roundedPercentH = DEFAULT_ROUNDED;
		roundedPercentV = DEFAULT_ROUNDED;
	}

	public RoundedBorder(Color c, float lt, float rph, float rpv)
	{
		borderColor = c;
		buttonColor = null;
		lineThickness = lt;
		roundedPercentH = rph;
		roundedPercentV = rpv;
	}

	
	public RoundedBorder(Color c, Color bg, float lt, float rph, float rpv)
	{
		borderColor = c;
		buttonColor = bg;
		lineThickness = lt;
		roundedPercentH = rph;
		roundedPercentV = rpv;
	}
	
	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		// convert graphics to graphics 2d
		Graphics2D g2 = (Graphics2D) g;
		
		// smooth drawing
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// calculate roundWidth and round height
		int roundWidth = (int) (((width - lineThickness)* roundedPercentH) / 2);
		int roundHeight = (int) (((height - lineThickness)* roundedPercentV) / 2);
		
		// create Round Rectangle
		RoundRectangle2D.Double rect = new RoundRectangle2D.Double(x + lineThickness / 2, 
				y + lineThickness / 2, width - lineThickness, height - lineThickness * 1.5, 
				roundWidth, roundHeight);
		
		// fill the background
		g2.setColor(buttonColor == null ? c.getBackground() : buttonColor);
		g2.fill(rect);		
		
		// set g2 color & thickness
		g2.setColor(borderColor);
		g2.setStroke(new BasicStroke(lineThickness));
		
		// draw the border of the round rectangle
		g2.draw(rect);
		
		// fill the text - only works for JButton
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		g2.setColor(c.getForeground());
		g2.setFont(c.getFont());
		
		int stringWidth = g2.getFontMetrics().stringWidth(((JButton) c).getText());
		int stringHeight = g2.getFont().getSize();
		int xPos = x + (width - stringWidth) / 2;
		int yPos = y + (height - stringHeight) / 2 + stringHeight - 1;
		
		if (c instanceof JButton)
			g2.drawString(((JButton) c).getText(), xPos, yPos);
		else
			System.out.println("Not a JButton. Won't print text in RoundedBorder");
		
	}

	@Override
	public Insets getBorderInsets(Component c) {
		// TODO Auto-generated method stub
		int insets = (int) (lineThickness + 4);
		
		return new Insets(insets, insets, insets, insets);

	}

	@Override
	public boolean isBorderOpaque() {
		// TODO Auto-generated method stub
		return true;
	}	
}