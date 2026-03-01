/**
 * Header holds the player's name and displays their score.
 * 
 * TODO
 *  - has play name
 *   - when you click on it, it opens dialogue prompt to enter name
 *    - make colors constant - rn they're copied from gameboard
 *     - when win score is reached make it bright
 *   
 * @author Asha Mavankal
 */

package blockblast.gui.gamepanel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import blockblast.gui.BigGUI;

// TODO use constant border color
// TODO refer back to owner board for board active state

public class Header extends JPanel
{
	// serial version uid
	private static final long serialVersionUID = 2967292054716773470L;

	public static final double FONT_HEIGHT_PROP = .85;
	
	public static final Color DARK_BLUE = new Color(46, 20, 148);
	public static final Color LESS_DARK_BLUE = new Color(71, 57, 197, 200);
	
	public static final String DEFAULT_NAME = "PLAYER ";
	public static final Font NAME_FONT = BigGUI.PLAIN_FONT;

	private static int playerNum = 1;
	
	private static final String SEPARATOR = ": ";
	private static final String PTS = " pts";
	
	// instance variables
	private String playerName;
	private int score;
	
	// name change panel
	private JDialog dialogue;
	
	// state
	private boolean boardActivated;
	
	public Header()
	{
		// set player name to default, score to 0, and board active to true
		playerName = DEFAULT_NAME + playerNum++;
		score = 0;
		boardActivated = true;
		
		// set clear background & border
		setOpaque(false);
		setBorder(new LineBorder(DARK_BLUE, 4));
	}

	public int updateScore(int newScore)
	{
		int oldScore = score;
		score = newScore;
		repaint();
		
		return oldScore;
	}

	public void setActive(boolean active)
	{
		boardActivated = active;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(LESS_DARK_BLUE);
		g2.fill(new Rectangle2D.Double(0, 0, getBounds().width, getBounds().height));
		
		super.paintComponent(g);
	}
}