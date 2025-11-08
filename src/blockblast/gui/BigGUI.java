/**
 * BigGUI.java is the driver class for the game, and also the
 * 		JFrame that holds all the game GUI.
 * 
 * 	instructions for compiling: javac blockblast\/**\/*.java
 * 		- compiles them into correct package structure too. default
 * 			into root of current directory (so result is a blockblast
 * 			directory with associated subirectories w/ class files). if
 * 			you specify -d dir, it'll put the result of compilation in dir
 * 
 * 	instructions for running (from root of project): java -cp bin:resources blockblast.gui.BigGUI
 * 		- basically want to set classpath to whichever directory holds compiled class files
 * 			(bin in the case of this project, but basically dir from compiling instructions)
 * 			and to whichever directory holds the resources
 * 				so the "some directory" in [somedirectory]/blockblast/images/ex.png
 * 
 * @author Asha Mavankal
 * 
 */

package blockblast.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;


public class BigGUI extends JFrame implements ActionListener
{
	// serial version uid
	private static final long serialVersionUID = -5893723168507829339L;

	// color constants
	public static final Color BACKGROUND = new Color(148, 217, 228);
	
	// instance variables
	protected WelcomePanel welcome;
	protected InstructionPanel instructions;
	protected GamePanel gamePage;
	
	/**	Constructor
	 */
	public BigGUI()
	{
		super("Block Blast PVP!");
		
		// configure JFrame (not resizable)
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);

		// 1: add welcome panel
		welcome = new WelcomePanel();
		add(welcome);
		welcome.addStartListener(this);
		pack();
		
		// pack JFrame & put it in middle of screen
		setLocationRelativeTo(null); // put GUI in middle of screen
		
		// make JFrame visible
		setVisible(true);
	}
	
	/**	Respond to button clicks from the panels
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == welcome.start) // START button clicked
		{
			// hide welcome panel
			welcome.setVisible(false);
			
			// 2: add instructions panel, invisible for now
			instructions = new InstructionPanel();
			add(instructions);
			
			// center the frame based on new dimensions
			pack();
			setLocationRelativeTo(null);
			instructions.addOkListener(this);
		}
		if (e.getSource() == instructions.ok) // OK button clicked
		{
			// hide instructions panel
			instructions.setVisible(false);
			
			// 3: now to play the actual game...
			gamePage = new GamePanel(this);
			add(gamePage);
		}
	}

	/**	Main method
	 */
	public static void main(String[] args)
	{		
		BigGUI gui = new BigGUI();
	}
}