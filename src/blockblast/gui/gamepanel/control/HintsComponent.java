/**
 *	HintsComponent.java
 *
 */


package blockblast.gui.gamepanel.control;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.Timer;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import blockblast.gui.GamePanel;
import blockblast.gui.gamepanel.ControlPanel;

import javax.swing.JTextPane;

public class HintsComponent extends JTextPane implements ActionListener
{
	// serial version uid
	private static final long serialVersionUID = 7793618607893387568L;

	public static final String FONT_STYLE = "Rockwell"; // TODO
	
	// message constants
	public static final String HINT_STR = "HINT: \n";
	public static final String[] HINTS = {
			"Clearing a line allows you to start a streak!",
			"The longer your streak, the more bonus points you get each turn!",
			"Each line cleared is worth 400 points!",
			"Clearing horizontal and vertical lines at the same time gets you a bonus!",
			"Be aware of all the blocks in your block bank so you don't run out of space",
			"If you clear the board, you get 200 bonus points!"
	};
	
	public static final double FONT_HEIGHT_PROP = .12;
	public static final double SPACE_HEIGHT_PROP = .07;
	private static final double LINE_SPACE_PROP = -.001;
	
	private static final int WAIT = 3000;
	
	// instance variable - track which message is displayed
	private int currMsg;
	private Timer timer;
	
	// instance variables - style
	SimpleAttributeSet font;
	SimpleAttributeSet aboveSpacing;
	
	public HintsComponent(ControlPanel control)
	{
		// set background & border
		setOpaque(false);
		setEditable(false);
		setBorder(new LineBorder(GamePanel.BORDER, 4)); // TODO magic number
		
		// set current message index
		currMsg = (int) (Math.random() * HINTS.length);
		
		// set formatting
		font = new SimpleAttributeSet();
		aboveSpacing = new SimpleAttributeSet();
		double height = control.getChildSize(ControlPanel.HINTS_COMPONENT).getHeight();
		
		StyleConstants.setAlignment(font, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontFamily(font, FONT_STYLE);
		StyleConstants.setFontSize(font, (int) (height * FONT_HEIGHT_PROP));
		StyleConstants.setForeground(font, Color.WHITE);
		StyleConstants.setLineSpacing(font, (float) (height * LINE_SPACE_PROP));
		
		StyleConstants.setSpaceAbove(aboveSpacing, (float) (height * SPACE_HEIGHT_PROP));
		getStyledDocument().setParagraphAttributes(0, 0, aboveSpacing, false);
		
		// set original text
		updateMessage();
		
		// disappear caret
		setCaretColor(new Color(0, 0, 0, 0));
		
		// start timer for slideshow
		timer = new Timer(WAIT, this);
		timer.start();
	}

	private void updateMessage()
	{
		// change text
		setText(HINT_STR + HINTS[currMsg]);
		
		// formatting
		getStyledDocument().setParagraphAttributes(0, getStyledDocument().getLength(), font, false);
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// every time the timer hits, 
		// increase currMsg, and update message
		currMsg = currMsg >= HINTS.length - 1 ? 0 : currMsg + 1;
		updateMessage();
	}
	
	/** Paints the background of the component
	 * 
	 */
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