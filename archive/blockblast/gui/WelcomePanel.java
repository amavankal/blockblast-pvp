/**
 *	WelcomePanel.java is the welcome panel
 *		that the user sees before they play the game.
 *
 * @author Asha Mavankal, Amya Prophet
 */

package blockblast.gui;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JButton;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class WelcomePanel extends JPanel
{
	protected static final double FRAME_WIDTH_PROPORTION = 2.0 / 5;
	private static final String WELCOME = "Welcome to our\n"
			+ "Block Blast PVP Game!\n"
			+ "Click START to proceed to instructions.";
	
	// instance variables
	protected JTextPane welcomeText;
	protected JButton start;
	
	public WelcomePanel()
	{
		super(true); // double buffered
		
		// 1: set layout of this panel
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		
		// 2: set size of this panel
		initializePanelSize();
		
		// 3: set background color
		setBackground(BigGUI.BACKGROUND);
		
		// 3: add welcome text to JPanel
		initializeWelcomeText();
		add(welcomeText);
		
		// 4: add start button to JPanel (no action listener yet)
		start = new JButton("START!");
		start.setBackground(BigGUI.BACKGROUND);
		add(start);
		
		// good practice
		repaint();
	}
	
	public void addStartListener(ActionListener a)
	{
		start.addActionListener(a);
	}
	
	private void initializePanelSize()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.getWidth() * FRAME_WIDTH_PROPORTION);
		int rem = (int) screenSize.getWidth() - width;
		int height = (int) screenSize.getHeight() - rem;
		
    	setSize(width, height);
	}
	
	private void initializeWelcomeText()
	{
		// 1: create a JTextPane
		welcomeText = new JTextPane();
		
		// set background color
		welcomeText.setBackground(BigGUI.BACKGROUND);
				
		// 2: create SimpleAttributeSet to style text
		SimpleAttributeSet welcAttribs = new SimpleAttributeSet();
		StyleConstants.setAlignment(welcAttribs, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontFamily(welcAttribs, "Agency FB");
		StyleConstants.setFontSize(welcAttribs, 50);
		
		// 3: add attributes to text
		StyledDocument doc = welcomeText.getStyledDocument();
	    doc.setParagraphAttributes(0, doc.getLength(), welcAttribs, false);
		
		// 4: add text
		welcomeText.setText(WELCOME);
	}
}