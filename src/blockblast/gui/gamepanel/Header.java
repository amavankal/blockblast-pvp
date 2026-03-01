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
import javax.swing.JTextPane;
import javax.swing.border.LineBorder;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import blockblast.gui.BigGUI;

public class Header extends JTextPane
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
	private StyledDocument styledDoc;
	
	// name change panel
	private JDialog dialogue;
	
	// state
	private boolean boardActivated;
	
	public Header()
	{
		playerName = DEFAULT_NAME + playerNum++;
		score = 0;
		boardActivated = true;

		// set specs
		setEditable(false);
		setCaretColor(new Color(0, 0, 0, 0)); // TODO
		setText(playerName + SEPARATOR + score + PTS);
		
		setOpaque(false);
		setBorder(new LineBorder(DARK_BLUE, 4));
		
		styledDoc = getStyledDocument();
		SimpleAttributeSet overallStyle = new SimpleAttributeSet();
		
		StyleConstants.setFontFamily(overallStyle, NAME_FONT.getFontName());
		StyleConstants.setAlignment(overallStyle, StyleConstants.ALIGN_CENTER);
		StyleConstants.setForeground(overallStyle, Color.WHITE);
		StyleConstants.setFontSize(overallStyle, 
				(int) (getPreferredSize().getHeight() * FONT_HEIGHT_PROP));
		StyleConstants.setSpaceAbove(overallStyle, -2.5f);
		
		styledDoc.setParagraphAttributes(0, 1, overallStyle, true);
		
		// set up dialog
	}

	public int updateScore(int newScore)
	{
		int oldScore = score;
		score = newScore;
		setText(playerName + SEPARATOR + score);
		
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