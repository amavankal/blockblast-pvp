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
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
	
	// GUI constants
	public static final Color BORDER = new Color(46, 20, 148);
	public static final Color BACKGROUND = new Color(71, 57, 197, 200);
	
	public static final Font FONT1 = BigGUI.SECONDARY_FONT;
	public static final Font FONT2 = BigGUI.TITLE_FONT;
	public static final Font FONT3 = BigGUI.SECONDARY_FONT;

	public static final double FONT_HEIGHT_PROP = .60;
	
	// text constants
	public static final String DEFAULT_NAME = "PLAYER ";
	private static final String SEPARATOR = ": ";
	private static final String PTS = " pts";

	// variable to track player number
	private static int playerNum = 1;
	
	// instance variables
	private String playerName;
	private int score;
	
	// name change panel
	private JDialog dialogue;
	
	/** Constructor
	 */
	public Header()
	{
		// set player name to default, score to 0, and board active to true
		playerName = DEFAULT_NAME + playerNum++;
		score = 0;
		
		// set clear background & default border
		setOpaque(false);
		setBorder(new LineBorder(BORDER, 4));
	}

	/**	Update the score displayed by this header
	 *	@param newScore the new score to display
	 *	@return the old score that was previously displayed
	 */
	public int updateScore(int newScore)
	{
		int oldScore = score;
		score = newScore;
		repaint();
		
		return oldScore;
	}
	
	/**	Paint the background and text (name & score)
	 */
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		// draw background
		g2.setColor(BACKGROUND);
		g2.fill(new Rectangle2D.Double(0, 0, getBounds().width, getBounds().height));
		
		// define string chunks
		String name = playerName + SEPARATOR;
		String scoreStr = "" + score;
		String ptsStr = PTS;

		// define fonts
		Font font1 = FONT1.deriveFont((float) (getHeight() * FONT_HEIGHT_PROP));
		Font font2 = FONT2.deriveFont((float) (getHeight() * FONT_HEIGHT_PROP));
		Font font3 = FONT3.deriveFont((float) (getHeight() * FONT_HEIGHT_PROP));

		// set up g2 to draw text
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, 
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setColor(Color.WHITE);
		
		// get fontMetrics for fonts
		g2.setFont(font1);
		FontMetrics nameMetrics = g2.getFontMetrics();
		g2.setFont(font2);
		FontMetrics scoreMetrics = g2.getFontMetrics();
		g2.setFont(font3);
		FontMetrics ptsMetrics = g2.getFontMetrics();
		
		// calculate text widths and heights
		int width1 = nameMetrics.charsWidth(name.toCharArray(), 0, name.length());
		int width2 = scoreMetrics.charsWidth(scoreStr.toCharArray(), 0, scoreStr.length());
		int width3 = ptsMetrics.charsWidth(ptsStr.toCharArray(), 0, ptsStr.length());
		int width = width1 + width2 + width3;

		int height1 = nameMetrics.getAscent() + nameMetrics.getDescent();
		int height2 = scoreMetrics.getAscent() + scoreMetrics.getDescent();
		int height3 = ptsMetrics.getAscent() + ptsMetrics.getDescent();
		
		// calculate drawing starting points
		int startX1 = (getWidth() - width) / 2;
		int startX2 = startX1 + width1;
		int startX3 = startX2 + width2;

		int startY1 = (getHeight() - height1) / 2 + nameMetrics.getAscent();
		int startY2 = (getHeight() - height2) / 2 + scoreMetrics.getAscent();
		int startY3 = (getHeight() - height3) / 2 + ptsMetrics.getAscent();

		// draw strings
		g2.setFont(font1);
		g2.drawString(name, startX1, startY1);
		g2.setFont(font2);
		g2.drawString(scoreStr, startX2, startY2);
		g2.setFont(font3);
		g2.drawString(ptsStr, startX3, startY3);
	}
}
