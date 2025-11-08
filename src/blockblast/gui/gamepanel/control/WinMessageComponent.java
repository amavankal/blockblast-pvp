/**
 *	WinMessageComponent.java displays the message informing the user how many
 *		points they need to win.
 */


package blockblast.gui.gamepanel.control;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.DefaultCaret;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import blockblast.gui.GamePanel;
import blockblast.gui.gamepanel.ControlPanel;

public class WinMessageComponent extends JTextPane
{
	// serial version uid
	private static final long serialVersionUID = -8584371760498140575L;
	
	// TODO standardize fonts
	public static final double TEXT_HEIGHT_PROP = .2;
	public static final double ABOVE_SPACING_PROP = .064;
	
	public static final String FONT = "Juice ITC";
	public static final String FONT_2 = "Jokerman";
	
	private static final String MSG_1 = "First to\n";
	private static final String MSG_2 = "\nWins!";
	
	// instance variables
	private int winScore;
	
	public WinMessageComponent(GamePanel associatedPanel, ControlPanel control)
	{
		winScore = associatedPanel.getWinScore();
		
		// set message
		setText(MSG_1 + this.winScore + MSG_2);
		
		// set background & border
		setOpaque(false);
		setEditable(false);
		setBorder(new LineBorder(GamePanel.BORDER, 4)); // TODO magic number
		
		// TODO style constants set the style
		applyMessageStyles(control);
		
		// disappear cursor
		setCaretColor(new Color(0, 0, 0, 0));
	}
	
	private void applyMessageStyles(ControlPanel control)
	{
		// create style sets
		SimpleAttributeSet overallStyle = new SimpleAttributeSet();
		SimpleAttributeSet font2 = new SimpleAttributeSet();
		SimpleAttributeSet aboveSpacing = new SimpleAttributeSet();
		
		// font 1 style
		StyleConstants.setAlignment(overallStyle, StyleConstants.ALIGN_CENTER);
		double height = control.getChildSize(ControlPanel.WIN_MESSAGE_COMPONENT).getHeight();
		StyleConstants.setFontSize(overallStyle, (int) (height * TEXT_HEIGHT_PROP));
		StyleConstants.setFontFamily(overallStyle, FONT);
		StyleConstants.setForeground(overallStyle, Color.WHITE);
		
		// font 2 style
		StyleConstants.setFontFamily(font2, FONT_2);
		
		// above spacing
		StyleConstants.setSpaceAbove(aboveSpacing, (float) (height * ABOVE_SPACING_PROP));
		
		// set styles
		getStyledDocument().setParagraphAttributes(0, getStyledDocument().getLength(), 
				overallStyle, false);
		
		getStyledDocument().setParagraphAttributes(MSG_1.length(), 0, font2, false);
		getStyledDocument().setParagraphAttributes(0, 0, aboveSpacing, false);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		// draw background
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(GamePanel.BACKGROUND);
		g2.fill(new Rectangle2D.Double(0, 0, getBounds().width, getBounds().height));
		
		super.paintComponent(g);
	}
}