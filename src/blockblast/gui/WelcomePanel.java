/**
 *	WelcomePanel.java is the welcome panel
 *		that the user sees before they play the game.
 *
 *	improvement ideas
 *		 - use Box.RigidArea instead of buffers
 *		 - make start rainbow whenever you hover over it... would be SUPA cool
 *
 * @author Asha Mavankal
 * 
 */

package blockblast.gui;

import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JButton;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
//import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import blockblast.core.Block;

public class WelcomePanel extends JPanel
{
	// serial version uid
	private static final long serialVersionUID = -6673190543027070791L;
	
	// dimensions
	public static final int TEXT_WIDTH = 600;
	public static final int TEXT_HEIGHT = 295;
	public static final int START_WIDTH = 110;
	public static final int START_HEIGHT = 50;
	private static final int BUFFER_WIDTH = 5;
	private static final int BUFFER_1_HEIGHT = 0; // replace with Box.RigidArea !!!
	private static final int BUFFER_2_HEIGHT = 20;
	private static final Color BUTTON_PRESSED = new Color(46, 20, 148);
	
	public static final int COMPONENT_HEIGHT = TEXT_HEIGHT + START_HEIGHT 
			+ BUFFER_1_HEIGHT + BUFFER_2_HEIGHT + 10;
	public static final int COMPONENT_WIDTH = TEXT_WIDTH;
	
	
	public static final String TITLE_FONT = "Jokerman"; // FIXME: find alts OR download fonts
	public static final String PLAIN_FONT = "Juice ITC";
	public static final String START_FONT = TITLE_FONT;
	
	protected static final double WIDTH_SCREEN_PROP = .5;
	protected static final double HEIGHT_WIDTH_PROP = 2.0 / 5;
	
	private static final String START = "START!";
	
	private static final String[][] TEXT = {
			{"WELCOME TO\n"},
			{"BLOCK  BLAST PVP!\n"},
			{"Click ", "START ", "to proceed to instructions."}
	};
	
	private static final String[][] FONTS = {
			{PLAIN_FONT},
			{TITLE_FONT},
			{PLAIN_FONT, START_FONT}
	};
	private static final int[][] FONT_SIZES = {
			{20},
			{60},
			{30, 30}
	};
	private static final float[][] LINE_SPACINGS = {
			{0},
			{-.2f},
			{0, 0}
	};
	private static final float[][] ABOVE_SPACING = {
			{55f},
			{-10f},
			{16f, 16f}
	};
	private static final Color[][] COLORS = {
			{Color.BLACK},
			{Color.BLACK},
			{Color.BLACK, Block.BLOCK_COLORS[3]}
	};
	
	// instance variables
	protected JTextPane welcomeText;
	protected JPanel startContainer;
	protected JButton start;
	private WelcomePanelMouseAdapter mouseAdapter;
	
	public WelcomePanel()
	{
		super(true); // double buffered
		mouseAdapter = new WelcomePanelMouseAdapter(); // instantiate mouse adapter
		
		// 1: set layout of this panel
		BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);
		setLayout(layout);
		
		// 3: set background color
		setOpaque(false);
		
		// 3: add welcome text to JPanel
		initializeWelcomeText();
		add(welcomeText);
		
		// 5: add buffer component above start
		add(Box.createRigidArea(new Dimension(BUFFER_WIDTH, BUFFER_1_HEIGHT)));
		
		// 4: add start button to JPanel (no action listener yet)
		initializeStartButton();
		add(startContainer);
		
		// 5: add buffer component below start
		add(Box.createRigidArea(new Dimension(BUFFER_WIDTH, BUFFER_2_HEIGHT)));
	}
	
	/**	Adds the given action listener to this start button.
	 *	@param a The ActionListener to associate with the start button
	 */
	public void addStartListener(ActionListener a)
	{
		start.addActionListener(a);
	}

	/**	Initializes the welcome text
	 */
	private void initializeWelcomeText()
	{
		// 1: create a JTextPane
		welcomeText = new JTextPane();
		
		// 2: set background color & editable
		welcomeText.setOpaque(false);
		welcomeText.setEditable(false);
		
		// set welcomeText dimensions
		welcomeText.setPreferredSize(new Dimension(TEXT_WIDTH, TEXT_HEIGHT)); //295
		
		// 3: create and configure SimpleAttributeSets for each text chunk
		SimpleAttributeSet[][] styles = fillTextStyles();

		// 4: set the welcomeText and retrieve its styled document
		welcomeText.setText(TEXT[0][0] + TEXT[1][0] + TEXT[2][0] + TEXT[2][1] + TEXT[2][2]);
		StyledDocument styledDoc = welcomeText.getStyledDocument();
		
		// apply the styles to the styled document that was just retrieved
		applyStylesToStyledDocument(styledDoc, styles);
	}

	/** Helper method for initializeWelcomeText. Fills the styles for
	 * 		each chunk of text in a SimpleAttributeSet 2d array based
	 * 		on the declared values in the constants and returns the 
	 * 		array. Each row is a line of text, and each column is a 
	 * 		different style within the row.
	 *	@return the styles
	 */
	private static SimpleAttributeSet[][] fillTextStyles()
	{
		// create an array with as many rows as the constant array of values
		SimpleAttributeSet[][] styles = new SimpleAttributeSet[FONTS.length][];
		
		// fill the array with the constants
		for (int line = 0; line < FONTS.length; line++)
		{
			styles[line] = new SimpleAttributeSet[FONTS[line].length];
			
			for (int style = 0; style < styles[line].length; style++)
			{
				styles[line][style] = new SimpleAttributeSet();
				
				// style the set
				StyleConstants.setAlignment(styles[line][style], StyleConstants.ALIGN_CENTER);
				StyleConstants.setFontFamily(styles[line][style], FONTS[line][style]);
				StyleConstants.setFontSize(styles[line][style], FONT_SIZES[line][style]);
				StyleConstants.setLineSpacing(styles[line][style], LINE_SPACINGS[line][style]);
				StyleConstants.setSpaceAbove(styles[line][style], ABOVE_SPACING[line][style]);
				StyleConstants.setForeground(styles[line][style], COLORS[line][style]);
			}
		}
		
		// return the styles
		return styles;
	}
	
	/**	Helper method for initializeWelcomeText. Applies the given styles
	 * 		to the styledDocument of the welcomeText. 
	 * @param styledDoc the StyledDocument to style
	 * @param styles the styles to apply to the Document
	 */
	private static void applyStylesToStyledDocument(StyledDocument styledDoc, 
			SimpleAttributeSet[][] styles)
	{
		// style line 1
		styledDoc.setParagraphAttributes(0, 1, styles[0][0], false);
		
		// style line 2 - alignment
		styledDoc.setParagraphAttributes(TEXT[0][0].length(), 1, styles[1][0], false);
		
		// style line 2 - rainbow
		for (int letter = 0; letter < TEXT[1][0].length() - 1; letter++)
		{
			StyleConstants.setForeground(styles[1][0], 
					Block.BLOCK_COLORS[letter % Block.BLOCK_COLORS.length]);
			
			styledDoc.setCharacterAttributes(TEXT[0][0].length() + letter,
					1, styles[1][0], false);
		}
		
		// style line 3
		styledDoc.setParagraphAttributes(TEXT[0][0].length() + TEXT[1][0].length(), 1, styles[2][0], false);
		
		// style START text
		styledDoc.setCharacterAttributes(TEXT[0][0].length() + TEXT[1][0].length() + TEXT[2][0].length(), 
				TEXT[2][1].length(), styles[2][1], false);
	}

	/**	Initializes the start button
	 */
	private void initializeStartButton()
	{
		// create and configure the startContainer
		startContainer = new JPanel();
		startContainer.setOpaque(false);
		
		// create JButton and set clear + content area not filled
		start = new JButton(START);
		start.setOpaque(false);
		start.setContentAreaFilled(false);
		
		// set preferred button size and initialize the text
		start.setPreferredSize(new Dimension(START_WIDTH, START_HEIGHT));
		start.setText(START);
		
		// set text color and font for start button
		start.setForeground(Block.BLOCK_COLORS[3]);
		start.setFont(new Font(START_FONT, Font.PLAIN, 20));
		
		// add new RoundedBorder
		RoundedBorder border = new RoundedBorder(Block.BLOCK_COLORS[3], 
				new Color(143, 212, 238), 1.5f, .80f, 1f);
		start.setBorder(border);

		
		// configure  button pressed
		start.addMouseListener(mouseAdapter);
		
		// add start to this panel
		startContainer.add(start);
	}
	
	/**	PAINTS THE BOARD
	 */
	@Override
	public void paintComponent(Graphics g)
	{	
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g);
		
		BufferedImage background;
		
		// FIXME: use OS-independent filepath
		//File f = new File("");
		try
		{
			setOpaque(false);
			InputStream in = getClass().getResourceAsStream("/images/background2.png");
			background = ImageIO.read(in);
			g2.drawImage(background, 0, 0, getWidth(), getHeight(), 0, 0,
					background.getWidth(), background.getHeight(), null);
		}
		catch (IOException io)
		{
			System.out.println(io.getMessage() + " Unable to draw Welcome screen background");

			// just set BG to blue if input file couldn't be read
			setOpaque(true);
			setBackground(BigGUI.BACKGROUND);
		}
	}

	private class WelcomePanelMouseAdapter extends MouseAdapter {
		/**	Changes the color of the word START in welcome text and the color of the Start
		 * 		button when mouse hovers over the Start button.
		 * @param e the mouse
		 */
		@Override
		public void mouseEntered(MouseEvent e) {
			if (e.getSource() == start)
			{
				start.setForeground(BUTTON_PRESSED);
				start.setBorder(new RoundedBorder(BUTTON_PRESSED, 
					new Color(143, 212, 238, 150), 1.5f, .80f, 1f));
				
				// change the color of the word START too
				changeStartWordColor(BUTTON_PRESSED);
			}
		}
		
		/**	Changes beck the color of the word START in welcome text and the color of the 
		 * 		Start button when mouse stops hovering over the Start button.
		 * @param e the mouse
		 */
		@Override
		public void mouseExited(MouseEvent e) {
			if (e.getSource() == start)
			{
				start.setForeground(Block.BLOCK_COLORS[3]);
				start.setBorder(new RoundedBorder(Block.BLOCK_COLORS[3], 
					new Color(143, 212, 238), 1.5f, .80f, 1f));
				
				// change the color of the word START too
				changeStartWordColor(Block.BLOCK_COLORS[3]);
			}
		}
		
		/**	Helper method for mouseEntered & mouseExited. Changes color of word Start
		 * 		in the welcome text.
		 * @param c the color to change the text to
		 */
		private void changeStartWordColor(Color c)
		{
			//change the color of the word START
			StyledDocument styledDoc = welcomeText.getStyledDocument();
			
			SimpleAttributeSet newStart = new SimpleAttributeSet();
			StyleConstants.setForeground(newStart, c);
			
			styledDoc.setCharacterAttributes(TEXT[0][0].length() + TEXT[1][0].length()
				+ TEXT[2][0].length(), TEXT[2][1].length(), newStart, false);
		}
	}
}
