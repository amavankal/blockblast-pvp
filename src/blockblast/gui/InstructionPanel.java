/**
 *	InstructionPanel.java is the instruction panel
 *		that the user sees before they play the game.
 *
 * ideas
 *  -- be careful not to run out of space! 3 blocks will generate before every 3 turns
 *  		so you must optimize not to run out of space!
 *
 * @author Asha Mavankal
 * 
 */

package blockblast.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class InstructionPanel extends JPanel
{
	// serial version uid
	private static final long serialVersionUID = 950048594988179608L;
	
	// constants
	protected static final double FRAME_WIDTH_PROPORTION = 2.0 / 5;
	protected static final double FRAME_HEIGHT_PROPORTION = 3.0 / 5;
	protected static final String INSTRUCTIONS = 
		  "Instructions:\n"
		+ " - This is a 2-player game\n"
		+ " - Player 1 will go first; they select a block then click spot to add it\n"
		+ " - then Player 2 does same. There are options for power ups, such as"
		+ "undo and destroy.\n"
		+ " - The winner is the first to 5,000. If a player runs out of space in their grid,"
		+ " the other player automatically wins.";
	
	// instance variables
	protected JTextPane instructions; // panel that holds instructions
	protected JButton ok; // button to go to next panel
	
	/**	Constructor
	 */
	public InstructionPanel()
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
		initializeInstructions();
		add(instructions);
			
		// 4: add start button to JPanel (no action listener yet)
		ok = new JButton("OK");
		ok.setBackground(BigGUI.BACKGROUND);
		add(ok);
		
		// good practice
		repaint();
	}
	
	/**	Adds the given action listener to the ok button
	 * 		(Allows to move on to next game panel)
	 *	@param a The action listener that will listen to this game
	 */
	public void addOkListener(ActionListener a)
	{
		ok.addActionListener(a);
	}
	
	/**	Initializes the panel size
	 */
	private void initializePanelSize()
	{
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) (screenSize.getWidth() * FRAME_WIDTH_PROPORTION);
		int height = (int) (screenSize.getHeight() * FRAME_HEIGHT_PROPORTION);
		Dimension thisSize = new Dimension(width, height);
		
    	setPreferredSize(thisSize);
	}

	/**	Initializes the Instruction panel
	 */
	private void initializeInstructions()
	{
		// 1: create a JTextPane
		instructions = new JTextPane();
		
		// set background color, make scrollable
		instructions.setBackground(BigGUI.BACKGROUND);
		instructions.setEditable(false);
				
		// 2: create SimpleAttributeSet to style text
		SimpleAttributeSet instrAttribs = new SimpleAttributeSet();
		StyleConstants.setLeftIndent(instrAttribs, 4); // FIXME: make constant
		StyleConstants.setFontFamily(instrAttribs, "Agency FB"); // FIXME: make constant
		StyleConstants.setFontSize(instrAttribs, 25); // FIXME: make constant
		
		// 3: add attributes to text
		StyledDocument doc = instructions.getStyledDocument();
	    doc.setParagraphAttributes(0, doc.getLength(), instrAttribs, false);
		
		// 4: add text
		instructions.setText(INSTRUCTIONS);
	}
}