/**
 * BigGUI.java is the driver class for the game, and also the
 * 		JFrame that holds all the game GUI.
 * 
 * @author Asha Mavankal, Amya Prophet
 */

package blockblast.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import java.awt.Color;


public class BigGUI extends JFrame implements ActionListener
{
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
		if (e.getSource() == welcome.start)
		{
			System.out.println("Start clicked");
			welcome.setVisible(false);
			
			// 2: add instructions panel, invisible for now
			instructions = new InstructionPanel();
			add(instructions);
			pack();
			setLocationRelativeTo(null);
			instructions.addOkListener(this);
		}
		if (e.getSource() == instructions.ok)
		{
			System.out.println("OK clicked");
			instructions.setVisible(false);
			
			// 3: now to play the freaking game...
			gamePage = new GamePanel();
			add(gamePage);
			pack();
			setLocationRelativeTo(null);
		}
	}

	/**	Main method
	 */
	public static void main(String[] args)
	{
		BigGUI gui = new BigGUI();
	}
}